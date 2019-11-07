package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.*;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.kraftwerk.random.BuildingBlocks;
import dev.marksman.kraftwerk.util.Labeling;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static dev.marksman.kraftwerk.Result.result;
import static dev.marksman.kraftwerk.SizeSelectors.sizeSelector;
import static dev.marksman.kraftwerk.random.BuildingBlocks.*;

class Primitives {

    static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        // TODO: withMetadata
        return operand;
    }

    static <A, B> Generator<B> mapped(Fn1<? super A, ? extends B> fn, Generator<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    static <A, B> Generator<B> flatMapped(Fn1<? super A, ? extends Generator<B>> fn, Generator<A> operand) {
        return new FlatMapped<>(operand, fn::apply);
    }

    static <A> ConstantGenerator<A> constant(A value) {
        return new ConstantGenerator<A>(value);
    }

    static Generator<Integer> generateInt() {
        return IntGenerator.INSTANCE;
    }

    static Generator<Integer> generateInt(int min, int max) {
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

    static Generator<Integer> generateIntExclusive(int bound) {
        checkBound(bound);
        Maybe<String> label = Maybe.just(Labeling.intInterval(0, bound, true));

        if ((bound & -bound) == bound) { // bound is a power of 2
            return simpleGenerator(label, input -> unsafeNextIntBoundedPowerOf2(bound, input));
        } else {
            return simpleGenerator(label, input -> unsafeNextIntBounded(bound, input));
        }

    }

    static Generator<Integer> generateIntExclusive(int origin, int bound) {
        checkOriginBound(origin, bound);
        if (origin == 0) {
            return generateIntExclusive(bound);
        } else {
            long range = (long) bound - origin;
            long m = range - 1;
            if (range < Integer.MAX_VALUE) {
                return simpleGenerator(nothing(), input -> unsafeNextIntExclusive(origin, (int) range, input));
            } else if ((range & m) == 0) {
                // power of two
                return simpleGenerator(nothing(), input -> unsafeNextIntExclusivePowerOf2(origin, range, input));
            } else {
                return simpleGenerator(nothing(), input -> unsafeNextIntExclusiveWide(origin, range, input));
            }
        }
    }

    static Generator<Integer> generateIntIndex(int bound) {
        return generateIntExclusive(bound);  // TODO: make generateIntIndex unbiased
    }

    static Generator<Boolean> generateBoolean() {
        return BooleanGenerator.INSTANCE;
    }

    static Generator<Double> generateDouble() {
        return DoubleGenerator.INSTANCE;
    }

    static Generator<Float> generateFloat() {
        return FloatGenerator.INSTANCE;
    }

    static Generator<Long> generateLong() {
        return LongGenerator.INSTANCE;
    }

    static Generator<Long> generateLong(long min, long max) {
        checkMinMax(min, max);
        if (max == Long.MAX_VALUE) {
            if (min == Long.MIN_VALUE) {
                return generateLong();
            } else {
                return generateLongExclusive(min - 1, max)
                        .fmap(n -> n + 1);
            }
        } else {
            return generateLongExclusive(min, max + 1);
        }
    }

    static Generator<Long> generateLongExclusive(long bound) {
        checkBound(bound);
        if (bound <= Integer.MAX_VALUE) {
            return generateIntExclusive((int) bound).fmap(Integer::longValue);
        } else {
            return generateLongExclusive(0, bound);
        }
    }

    static Generator<Long> generateLongExclusive(long origin, long bound) {
        checkOriginBound(origin, bound);

        if (origin < 0 && bound > 0 && bound > Math.abs(origin - Long.MIN_VALUE)) {
            return simpleGenerator(nothing(), input -> unsafeNextLongExclusiveWithOverflow(origin, bound, input));
        }

        long range = bound - origin;
        long m = range - 1;

        if ((range & m) == 0L) {
            // power of two
            return simpleGenerator(nothing(), input -> unsafeNextLongExclusivePowerOf2(origin, range, input));
        } else {
            return simpleGenerator(nothing(), input -> unsafeNextLongExclusive(origin, range, input));
        }
    }

    static Generator<Long> generateLongIndex(long bound) {
        checkBound(bound);
        return generateLongExclusive(bound);
    }

    static ByteGenerator generateByte() {
        return ByteGenerator.INSTANCE;
    }

    static ShortGenerator generateShort() {
        return ShortGenerator.INSTANCE;
    }

    static Generator<Double> generateGaussian() {
        return GaussianGenerator.INSTANCE;
    }

    static Generator<Byte[]> generateBytes(int count) {
        checkCount(count);
        return new BytesGenerator(count);
    }

    static <A> Generator<A> sized(Fn1<Integer, Generator<A>> fn) {
        return new SizedGenerator<>(fn);
    }

    static <A, B, Out> Generator<Out> product(Generator<A> a,
                                              Generator<B> b,
                                              Fn2<A, B, Out> combine) {
        return new Product2<>(a, b, combine);
    }

    static <A, B, C, Out> Generator<Out> product(Generator<A> a,
                                                 Generator<B> b,
                                                 Generator<C> c,
                                                 Fn3<A, B, C, Out> combine) {
        return new Product3<>(a, b, c, combine);
    }


    static <A, B, C, D, Out> Generator<Out> product(Generator<A> a,
                                                    Generator<B> b,
                                                    Generator<C> c,
                                                    Generator<D> d,
                                                    Fn4<A, B, C, D, Out> combine) {
        return new Product4<>(a, b, c, d, combine);
    }


    static <A, B, C, D, E, Out> Generator<Out> product(Generator<A> a,
                                                       Generator<B> b,
                                                       Generator<C> c,
                                                       Generator<D> d,
                                                       Generator<E> e,
                                                       Fn5<A, B, C, D, E, Out> combine) {
        return new Product5<>(a, b, c, d, e, combine);
    }


    static <A, B, C, D, E, F, Out> Generator<Out> product(Generator<A> a,
                                                          Generator<B> b,
                                                          Generator<C> c,
                                                          Generator<D> d,
                                                          Generator<E> e,
                                                          Generator<F> f,
                                                          Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6<>(a, b, c, d, e, f, combine);
    }

    static <A, B, C, D, E, F, G, Out> Generator<Out> product(Generator<A> a,
                                                             Generator<B> b,
                                                             Generator<C> c,
                                                             Generator<D> d,
                                                             Generator<E> e,
                                                             Generator<F> f,
                                                             Generator<G> g,
                                                             Fn7<A, B, C, D, E, F, G, Out> combine) {
        return new Product7<>(a, b, c, d, e, f, g, combine);
    }

    static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                Generator<B> b,
                                                                Generator<C> c,
                                                                Generator<D> d,
                                                                Generator<E> e,
                                                                Generator<F> f,
                                                                Generator<G> g,
                                                                Generator<H> h,
                                                                Fn8<A, B, C, D, E, F, G, H, Out> combine) {
        return new Product8<>(a, b, c, d, e, f, g, h, combine);
    }

    static <A, Builder, Out> Generator<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                      Fn2<Builder, A, Builder> addFn,
                                                      Fn1<Builder, Out> buildFn,
                                                      Iterable<Generator<A>> elements) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, elements);
    }

    static <Elem, Builder, Out> Generator<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                         Fn2<Builder, Elem, Builder> addFn,
                                                         Fn1<Builder, Out> buildFn,
                                                         int size,
                                                         Generator<Elem> gen) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, gen));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ConstantGenerator<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("constant");

        private final A value;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            return input -> result(input, value);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Mapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

        private final Fn1<In, A> fn;
        private final Generator<In> operand;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            Generate<In> g = operand.prepare(parameters);
            return input -> g.apply(input).fmap(fn);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FlatMapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("flatMap");

        private final Generator<In> operand;
        private final Fn1<? super In, ? extends Generator<A>> fn;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, In>> runner = operand.prepare(parameters);
            return input -> {
                Result<? extends Seed, In> result1 = runner.apply(input);
                Generator<A> g2 = fn.apply(result1.getValue());
                return g2.prepare(parameters).apply(result1.getNextState());
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class BooleanGenerator extends Generator<Boolean> {
        private static Maybe<String> LABEL = Maybe.just("boolean");

        private static final BooleanGenerator INSTANCE = new BooleanGenerator();

        @Override
        public Generate<Boolean> prepare(Parameters parameters) {
            return BuildingBlocks::nextBoolean;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class DoubleGenerator extends Generator<Double> {
        private static Maybe<String> LABEL = Maybe.just("double");

        private static final DoubleGenerator INSTANCE = new DoubleGenerator();

        @Override
        public Generate<Double> prepare(Parameters parameters) {
            return BuildingBlocks::nextDouble;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FloatGenerator extends Generator<Float> {
        private static Maybe<String> LABEL = Maybe.just("float");

        private static final FloatGenerator INSTANCE = new FloatGenerator();

        @Override
        public Generate<Float> prepare(Parameters parameters) {
            return BuildingBlocks::nextFloat;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class IntGenerator extends Generator<Integer> {
        private static Maybe<String> LABEL = Maybe.just("int");

        private static final IntGenerator INSTANCE = new IntGenerator();

        @Override
        public Generate<Integer> prepare(Parameters parameters) {

            return applyBiasSetting(parameters.getBiasSettings()
                            .intBias(Integer.MIN_VALUE, Integer.MAX_VALUE),
                    BuildingBlocks::nextInt);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class LongGenerator extends Generator<Long> {
        private static Maybe<String> LABEL = Maybe.just("long");

        private static final LongGenerator INSTANCE = new LongGenerator();

        @Override
        public Generate<Long> prepare(Parameters parameters) {
            return BuildingBlocks::nextLong;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class GaussianGenerator extends Generator<Double> {
        private static Maybe<String> LABEL = Maybe.just("gaussian");

        private static final GaussianGenerator INSTANCE = new GaussianGenerator();

        @Override
        public Generate<Double> prepare(Parameters parameters) {
            return BuildingBlocks::nextGaussian;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ByteGenerator extends Generator<Byte> {
        private static Maybe<String> LABEL = Maybe.just("byte");

        private static final ByteGenerator INSTANCE = new ByteGenerator();

        @Override
        public Generate<Byte> prepare(Parameters parameters) {
            return input -> nextInt(input).fmap(Integer::byteValue);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ShortGenerator extends Generator<Short> {
        private static Maybe<String> LABEL = Maybe.just("short");

        private static final ShortGenerator INSTANCE = new ShortGenerator();

        @Override
        public Generate<Short> prepare(Parameters parameters) {
            return input -> nextInt(input).fmap(Integer::shortValue);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class BytesGenerator extends Generator<Byte[]> {
        private final int count;

        @Override
        public Generate<Byte[]> prepare(Parameters parameters) {
            return input -> {
                byte[] buffer = new byte[count];
                Result<? extends Seed, Unit> next = nextBytes(buffer, input);
                Byte[] result = new Byte[count];
                int i = 0;
                for (byte b : buffer) {
                    result[i++] = b;
                }
                return next.fmap(__ -> result);
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just("bytes[" + count + "]");
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class SizedGenerator<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("sized");

        private final Fn1<Integer, Generator<A>> fn;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            SizeSelector sizeSelector = sizeSelector(parameters.getSizeParameters());
            return input -> {
                Result<? extends Seed, Integer> sizeResult = sizeSelector.selectSize(input);
                return fn.apply(sizeResult.getValue())
                        .prepare(parameters)
                        .apply(sizeResult.getNextState());
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class WithMetadata<A> extends Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> operand;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            return operand.prepare(parameters);
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Aggregate<Elem, Builder, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("aggregate");

        private final Fn0<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generator<Elem>> elements;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Iterable<Generate<Elem>> runners = Map.map(g -> g.prepare(parameters), elements);
            return input -> {
                Seed current = input;
                Builder builder = initialBuilderSupplier.apply();

                for (Generate<Elem> element : runners) {
                    Result<? extends Seed, Elem> next = element.apply(current);
                    builder = addFn.apply(builder, next.getValue());
                    current = next.getNextState();
                }
                return result(current, buildFn.apply(builder));
            };

        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product2<A, B, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product2");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Fn2<A, B, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                return result(rb.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product3<A, B, C, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product3");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Fn3<A, B, C, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                return result(rc.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product4<A, B, C, D, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product4");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Fn4<A, B, C, D, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                return result(rd.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product5<A, B, C, D, E, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product5");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Fn5<A, B, C, D, E, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                return result(re.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product6<A, B, C, D, E, F, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product6");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Fn6<A, B, C, D, E, F, Out> combine;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, F>> runF = f.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                Result<? extends Seed, F> rf = runF.apply(re.getNextState());
                return result(rf.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue(),
                                rf.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product7<A, B, C, D, E, F, G, Out> extends Generator<Out> {
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
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, F>> runF = f.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, G>> runG = g.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                Result<? extends Seed, F> rf = runF.apply(re.getNextState());
                Result<? extends Seed, G> rg = runG.apply(rf.getNextState());
                return result(rg.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue(),
                                rf.getValue(),
                                rg.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Product8<A, B, C, D, E, F, G, H, Out> extends Generator<Out> {
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
        public Generate<Out> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, A>> runA = a.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, B>> runB = b.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, C>> runC = c.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, D>> runD = d.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, E>> runE = e.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, F>> runF = f.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, G>> runG = g.prepare(parameters);
            Fn1<Seed, Result<? extends Seed, H>> runH = h.prepare(parameters);
            return input -> {
                Result<? extends Seed, A> ra = runA.apply(input);
                Result<? extends Seed, B> rb = runB.apply(ra.getNextState());
                Result<? extends Seed, C> rc = runC.apply(rb.getNextState());
                Result<? extends Seed, D> rd = runD.apply(rc.getNextState());
                Result<? extends Seed, E> re = runE.apply(rd.getNextState());
                Result<? extends Seed, F> rf = runF.apply(re.getNextState());
                Result<? extends Seed, G> rg = runG.apply(rf.getNextState());
                Result<? extends Seed, H> rh = runH.apply(rg.getNextState());
                return result(rh.getNextState(),
                        combine.apply(ra.getValue(),
                                rb.getValue(),
                                rc.getValue(),
                                rd.getValue(),
                                re.getValue(),
                                rf.getValue(),
                                rg.getValue(),
                                rh.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    private static <A> Generator<A> simpleGenerator(Maybe<String> label, Generate<A> runFn) {
        return new Generator<A>() {
            @Override
            public Generate<A> prepare(Parameters parameters) {
                return runFn;
            }

            @Override
            public Maybe<String> getLabel() {
                return label;
            }
        };
    }

    private static <A> Generate<A> applyBiasSetting(BiasSetting<A> biasSetting,
                                                    Generate<A> underlying) {
        return biasSetting.match(__ -> underlying,
                isv -> injectSpecial(isv.getSpecialValues(), underlying));
    }

    private static <A> Generate<A> injectSpecial(ImmutableNonEmptyVector<A> specialValues,
                                                 Generate<A> underlying) {
        return underlying;
    }
}
