package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.*;
import dev.marksman.composablerandom.random.RandomUtils;
import dev.marksman.composablerandom.util.Labeling;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static dev.marksman.composablerandom.Result.result;

public class Primitives {
    @EqualsAndHashCode(callSuper = true)
    @Value
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

    public static <A> ConstantGenerator<A> constant(A value) {
        return new ConstantGenerator<A>(value);
    }


    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Mapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

        private final Fn1<In, A> fn;
        private final Generator<In> operand;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            // TODO: mapped
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class FlatMapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("flatMap");

        private final Generator<In> operand;
        private final Fn1<? super In, ? extends Generator<A>> fn;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            // TODO: flatMapped
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    public static BooleanGenerator nextBoolean() {
        return BooleanGenerator.INSTANCE;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class IntGenerator extends Generator<Integer> {
        private static Maybe<String> LABEL = Maybe.just("int");

        private static final IntGenerator INSTANCE = new IntGenerator();

        @Override
        public Result<? extends Seed, Integer> run(GeneratorContext context, Seed input) {
            return RandomUtils.nextBits(input, 32);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static IntGenerator nextInt() {
        return IntGenerator.INSTANCE;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class LongGenerator extends Generator<Long> {
        private static Maybe<String> LABEL = Maybe.just("long");

        private static final LongGenerator INSTANCE = new LongGenerator();

        @Override
        public Result<? extends Seed, Long> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static LongGenerator nextLong() {
        return LongGenerator.INSTANCE;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
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

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class SizedGenerator<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("sized");

        private final Fn1<Integer, Generator<A>> fn;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class WithMetadata<A> extends Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> operand;

        @Override
        public Result<? extends Seed, A> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Aggregate<Elem, Builder, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("aggregate");

        private final Fn0<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generator<Elem>> elements;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product2<A, B, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product2");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Fn2<A, B, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Product3<A, B, C, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product3");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Fn3<A, B, C, Out> combine;

        @Override
        public Result<? extends Seed, Out> run(GeneratorContext context, Seed input) {
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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
            return null;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
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
}
