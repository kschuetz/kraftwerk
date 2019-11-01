package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.*;
import dev.marksman.composablerandom.util.Labeling;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.random.RandomUtils.*;

public class Primitives {

    public static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        // TODO: withMetadata
        return operand;
    }

    public static <A> ConstantGenerator<A> constant(A value) {
        return new ConstantGenerator<A>(value);
    }

    public static Generator<Integer> generateInt() {
        return IntGenerator.INSTANCE;
    }

    public static Generator<Integer> generateInt(int min, int max) {
        checkMinMax(min, max);
        if (max == Integer.MAX_VALUE) {
            if (min == Integer.MIN_VALUE) {
                return generateInt();
            } else {
                return generateIntExclusive(min - 1, max)
                        .fmap(n -> n + 1);
            }
        } else {
            return generateIntExclusive(min, max + 1);
        }
    }

    public static Generator<Integer> generateIntExclusive(int bound) {
        checkBound(bound);
        Maybe<String> label = Maybe.just(Labeling.intInterval(0, bound, true));

        if ((bound & -bound) == bound) { // bound is a power of 2
            return buildGenerator(label, input -> {
                long s1 = getNextSeed(input.getSeedValue());
                int n = bitsFrom(31, s1);
                return result(input.setNextSeedValue(s1), (int) ((bound * (long) n) >> 31));
            });

        } else {
            return buildGenerator(label, input -> {
                long bits, val;
                long nextSeed = input.getSeedValue();
                do {
                    nextSeed = getNextSeed(nextSeed);
                    bits = bitsFrom(31, nextSeed);
                    val = bits % bound;
                } while (bits - val + (bound - 1) < 0);
                return result(input.setNextSeedValue(nextSeed), (int) val);
            });
        }

    }

    public static Generator<Integer> generateIntExclusive(int origin, int bound) {
        checkOriginBound(origin, bound);
        if (origin == 0) {
            return generateIntExclusive(bound);
        } else {
            long n = (long) bound - origin;
            long m = n - 1;
            if (n < Integer.MAX_VALUE) {
                return generateIntExclusive((int) n).fmap(r -> origin + r);
            } else if ((n & m) == 0) {
                // power of two
                return generateInt().fmap(r -> (r & (int) m) + origin);
            } else {
                return buildGenerator(nothing(), input -> {
                    Result<Seed, Integer> rg1 = nextInt(input);
                    Seed current = rg1.getNextState();
                    int r = rg1._2();
                    for (int u = r >>> 1;
                         u + m - (r = u % (int) n) < 0; ) {
                        Result<Seed, Integer> next = nextInt(current);
                        u = next._2() >>> 1;
                        current = next._1();
                    }
                    r += origin;

                    return result(current, r);
                });

            }
        }
    }

    public static Generator<Integer> generateIntIndex(int bound) {
        return generateIntExclusive(bound);  // TODO: make generateIntIndex unbiased
    }

    public static Generator<Boolean> generateBoolean() {
        return BooleanGenerator.INSTANCE;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ConstantGenerator<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("constant");

        private final A value;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            return result(input, value);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Mapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

        private final Fn1<In, A> fn;
        private final Generator<In> operand;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, In> run = operand.run(context, input);
            return run.fmap(fn);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class FlatMapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("flatMap");

        private final Generator<In> operand;
        private final Fn1<? super In, ? extends Generator<A>> fn;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, In> result1 = operand.run(context, input);
            return fn.apply(result1.getValue())
                    .run(context, result1.getNextState());
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class BooleanGenerator extends Generator<Boolean> {
        private static Maybe<String> LABEL = Maybe.just("boolean");

        private static final BooleanGenerator INSTANCE = new BooleanGenerator();

        @Override
        public Result<? extends Seed, Boolean> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class DoubleGenerator extends Generator<Double> {
        private static Maybe<String> LABEL = Maybe.just("double");

        private static final DoubleGenerator INSTANCE = new DoubleGenerator();

        @Override
        public Result<? extends Seed, Double> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static DoubleGenerator nextDouble() {
        return DoubleGenerator.INSTANCE;
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class FloatGenerator extends Generator<Float> {
        private static Maybe<String> LABEL = Maybe.just("float");

        private static final FloatGenerator INSTANCE = new FloatGenerator();

        @Override
        public Result<? extends Seed, Float> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static FloatGenerator nextFloat() {
        return FloatGenerator.INSTANCE;
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class IntGenerator extends Generator<Integer> {
        private static Maybe<String> LABEL = Maybe.just("int");

        private static final IntGenerator INSTANCE = new IntGenerator();

        @Override
        public Result<? extends Seed, Integer> run(GeneratorContext context, Seed input) {
            return nextInt(input);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class IntBoundedGenerator extends Generator<Integer> {
        private final int bound;

        @Override
        public Result<? extends Seed, Integer> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.intInterval(0, bound, true));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class IntExclusiveGenerator extends Generator<Integer> {
        private final int origin;
        private final int bound;

        @Override
        public Result<? extends Seed, Integer> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.intInterval(origin, bound, true));
        }

    }

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class IntBetweenGenerator extends Generator<Integer> {
        private final int min;
        private final int max;

        @Override
        public Result<? extends Seed, Integer> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.intInterval(min, max, false));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class IntIndexGenerator extends Generator<Integer> {
        private final int bound;

        @Override
        public Result<? extends Seed, Integer> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.interval("index", 0, bound, true));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LongGenerator extends Generator<Long> {
        private static Maybe<String> LABEL = Maybe.just("long");

        private static final LongGenerator INSTANCE = new LongGenerator();

        @Override
        public Result<? extends Seed, Long> run(GeneratorContext context, Seed input) {
            long s1 = getNextSeed(input.getSeedValue());
            long s2 = getNextSeed(s1);
            int i1 = bitsFrom(32, s1);
            int i2 = bitsFrom(32, s2);
            long result = ((long) i1 << 32) + i2;
            return result(input.setNextSeedValue(s2), result);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static LongGenerator nextLong() {
        return LongGenerator.INSTANCE;
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LongBoundedGenerator extends Generator<Long> {
        private final long bound;

        @Override
        public Result<? extends Seed, Long> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(0, bound, true));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LongExclusiveGenerator extends Generator<Long> {
        private final long origin;
        private final long bound;

        @Override
        public Result<? extends Seed, Long> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(origin, bound, true));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LongBetweenGenerator extends Generator<Long> {
        private final long min;
        private final long max;

        @Override
        public Result<? extends Seed, Long> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(min, max, false));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LongIndexGenerator extends Generator<Long> {
        private final long bound;

        @Override
        public Result<? extends Seed, Long> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(0, bound, true));
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class GaussianGenerator extends Generator<Double> {
        private static Maybe<String> LABEL = Maybe.just("gaussian");

        private static final GaussianGenerator INSTANCE = new GaussianGenerator();

        @Override
        public Result<? extends Seed, Double> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static GaussianGenerator nextGaussian() {
        return GaussianGenerator.INSTANCE;
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class ByteGenerator extends Generator<Byte> {
        private static Maybe<String> LABEL = Maybe.just("byte");

        private static final ByteGenerator INSTANCE = new ByteGenerator();

        @Override
        public Result<? extends Seed, Byte> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static ByteGenerator nextByte() {
        return ByteGenerator.INSTANCE;
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class ShortGenerator extends Generator<Short> {
        private static Maybe<String> LABEL = Maybe.just("short");

        private static final ShortGenerator INSTANCE = new ShortGenerator();

        @Override
        public Result<? extends Seed, Short> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static ShortGenerator nextShort() {
        return ShortGenerator.INSTANCE;
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class BytesGenerator extends Generator<Byte[]> {
        private final int count;

        @Override
        public Result<? extends Seed, Byte[]> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just("bytes[" + count + "]");
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class SizedGenerator<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("sized");

        private final Fn1<Integer, Generator<A>> fn;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, Integer> sizeResult = context.getSizeSelector().selectSize(input);
            return fn.apply(sizeResult.getValue())
                    .run(context, sizeResult.getNextState());
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class WithMetadata<A> extends Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> operand;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            return operand.run(context, input);
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Aggregate<Elem, Builder, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("aggregate");

        private final Fn0<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generator<Elem>> elements;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Seed current = input;
            Builder builder = initialBuilderSupplier.apply();

            for (Generator<Elem> element : elements) {
                Result<? extends Seed, Elem> next = element.run(context, current);
                builder = addFn.apply(builder, next.getValue());
                current = next.getNextState();
            }
            return result(current, buildFn.apply(builder));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product2<A, B, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product2");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Fn2<A, B, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            return result(rb.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product3<A, B, C, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product3");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Fn3<A, B, C, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            Result<? extends Seed, C> rc = c.run(context, rb.getNextState());
            return result(rc.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue(),
                            rc.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product4<A, B, C, D, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product4");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Fn4<A, B, C, D, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            Result<? extends Seed, C> rc = c.run(context, rb.getNextState());
            Result<? extends Seed, D> rd = d.run(context, rc.getNextState());
            return result(rc.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue(),
                            rc.getValue(),
                            rd.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product5<A, B, C, D, E, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product5");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Fn5<A, B, C, D, E, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            Result<? extends Seed, C> rc = c.run(context, rb.getNextState());
            Result<? extends Seed, D> rd = d.run(context, rc.getNextState());
            Result<? extends Seed, E> re = e.run(context, rd.getNextState());
            return result(rc.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue(),
                            rc.getValue(),
                            rd.getValue(),
                            re.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product6<A, B, C, D, E, F, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product6");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Fn6<A, B, C, D, E, F, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            Result<? extends Seed, C> rc = c.run(context, rb.getNextState());
            Result<? extends Seed, D> rd = d.run(context, rc.getNextState());
            Result<? extends Seed, E> re = e.run(context, rd.getNextState());
            Result<? extends Seed, F> rf = f.run(context, re.getNextState());
            return result(rc.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue(),
                            rc.getValue(),
                            rd.getValue(),
                            re.getValue(),
                            rf.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product7<A, B, C, D, E, F, G, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product7");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Fn7<A, B, C, D, E, F, G, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            Result<? extends Seed, C> rc = c.run(context, rb.getNextState());
            Result<? extends Seed, D> rd = d.run(context, rc.getNextState());
            Result<? extends Seed, E> re = e.run(context, rd.getNextState());
            Result<? extends Seed, F> rf = f.run(context, re.getNextState());
            Result<? extends Seed, G> rg = g.run(context, rf.getNextState());
            return result(rc.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue(),
                            rc.getValue(),
                            rd.getValue(),
                            re.getValue(),
                            rf.getValue(),
                            rg.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product8<A, B, C, D, E, F, G, H, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product8");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Generator<H> h;
        private final Fn8<A, B, C, D, E, F, G, H, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            Result<? extends Seed, A> ra = a.run(context, input);
            Result<? extends Seed, B> rb = b.run(context, ra.getNextState());
            Result<? extends Seed, C> rc = c.run(context, rb.getNextState());
            Result<? extends Seed, D> rd = d.run(context, rc.getNextState());
            Result<? extends Seed, E> re = e.run(context, rd.getNextState());
            Result<? extends Seed, F> rf = f.run(context, re.getNextState());
            Result<? extends Seed, G> rg = g.run(context, rf.getNextState());
            Result<? extends Seed, H> rh = h.run(context, rg.getNextState());
            return result(rc.getNextState(),
                    combine.apply(ra.getValue(),
                            rb.getValue(),
                            rc.getValue(),
                            rd.getValue(),
                            re.getValue(),
                            rf.getValue(),
                            rg.getValue(),
                            rh.getValue()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Tap<A, B> extends Generator<B> {
        private static Maybe<String> LABEL = Maybe.just("tap");

        private final Generator<A> inner;
        private final Fn2<GeneratorImpl<A>, LegacySeed, B> fn;

        @Override
        public Result<? extends Seed, B> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static void checkBound(long bound) {
        if (bound < 1) throw new IllegalArgumentException("bound must be > 0");
    }

    private static void checkOriginBound(long origin, long bound) {
        if (origin >= bound) throw new IllegalArgumentException("bound must be > origin");
    }

    private static void checkMinMax(long min, long max) {
        if (min > max) throw new IllegalArgumentException("max must be >= min");
    }

    private static void checkCount(int count) {
        if (count < 0) throw new IllegalArgumentException("count must be >= 0");
    }

    private static <A> Generator<A> buildGenerator(Maybe<String> label, Fn2<GeneratorContext, Seed, Result<? extends Seed, A>> runFn) {
        return new Generator<A>() {
            @Override
            public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
                return runFn.apply(context, input);
            }

            @Override
            public Maybe<String> getLabel() {
                return label;
            }
        };
    }

    private static <A> Generator<A> buildGenerator(Maybe<String> label, Fn1<Seed, Result<? extends Seed, A>> runFn) {
        return new Generator<A>() {
            @Override
            public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
                return runFn.apply(input);
            }

            @Override
            public Maybe<String> getLabel() {
                return label;
            }
        };
    }
}
