package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.random.BuildingBlocks;
import dev.marksman.kraftwerk.util.Labeling;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static dev.marksman.kraftwerk.BiasSetting.noBias;
import static dev.marksman.kraftwerk.Result.result;
import static dev.marksman.kraftwerk.SizeSelectors.sizeSelector;
import static dev.marksman.kraftwerk.random.BuildingBlocks.*;

class Primitives {

    static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        // TODO: withMetadata
        //        if (operand instanceof Primitives.WithMetadata) {
//            Primitives.WithMetadata<A> target1 = (Primitives.WithMetadata<A>) operand;
//            return new Primitives.WithMetadata<>(label, applicationData, target1.getOperand());
//        } else {
//            return new Primitives.WithMetadata<>(label, applicationData, operand);
//        }
        return operand;
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
        return generateIntExclusiveImpl(bound, p -> p.getBiasSettings().intBias(0, bound - 1));
    }

    private static Generator<Integer> generateIntExclusiveImpl(int bound,
                                                               Fn1<Parameters, BiasSetting<Integer>> getBias) {
        checkBound(bound);
        Maybe<String> label = Maybe.just(Labeling.intInterval(0, bound, true));

        if ((bound & -bound) == bound) { // bound is a power of 2
            return simpleGenerator(label, getBias, input -> unsafeNextIntBoundedPowerOf2(bound, input));
        } else {
            return simpleGenerator(label, getBias, input -> unsafeNextIntBounded(bound, input));
        }

    }

    static Generator<Integer> generateIntExclusive(int origin, int bound) {
        return generateIntExclusiveImpl(origin, bound, p -> p.getBiasSettings().intBias(origin, bound - 1));
    }

    private static Generator<Integer> generateIntExclusiveImpl(int origin, int bound,
                                                               Fn1<Parameters, BiasSetting<Integer>> getBias) {
        checkOriginBound(origin, bound);
        if (origin == 0) {
            return generateIntExclusive(bound);
        } else {
            long range = (long) bound - origin;
            long m = range - 1;
            if (range < Integer.MAX_VALUE) {
                return simpleGenerator(nothing(), getBias, input -> unsafeNextIntExclusive(origin, (int) range, input));
            } else if ((range & m) == 0) {
                // power of two
                return simpleGenerator(nothing(), getBias, input -> unsafeNextIntExclusivePowerOf2(origin, range, input));
            } else {
                return simpleGenerator(nothing(), getBias, input -> unsafeNextIntExclusiveWide(origin, range, input));
            }
        }
    }

    static Generator<Integer> generateIntIndex(int bound) {
        return generateIntExclusiveImpl(bound, constantly(noBias()));
    }

    static Generator<Boolean> generateBoolean() {
        return BooleanGenerator.INSTANCE;
    }

    static FloatingPointGenerator<Double> generateDouble() {
        return DoubleGenerator.BASIC;
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

    static Generator<Long> generateLongExclusiveImpl(long bound,
                                                     Fn1<Parameters, BiasSetting<Long>> getBias) {
        checkBound(bound);
        if (bound <= Integer.MAX_VALUE) {
            return generateIntExclusive((int) bound).fmap(Integer::longValue);
        } else {
            return generateLongExclusive(0, bound);
        }
    }

    static Generator<Long> generateLongExclusive(long origin, long bound) {
        return generateLongExclusiveImpl(origin, bound, p -> p.getBiasSettings().longBias(origin, bound - 1));
    }

    private static Generator<Long> generateLongExclusiveImpl(long origin, long bound, Fn1<Parameters, BiasSetting<Long>> getBias) {
        checkOriginBound(origin, bound);

        if (origin < 0 && bound > 0 && bound > Math.abs(origin - Long.MIN_VALUE)) {
            return simpleGenerator(nothing(), getBias, input -> unsafeNextLongExclusiveWithOverflow(origin, bound, input));
        }

        long range = bound - origin;
        long m = range - 1;

        if ((range & m) == 0L) {
            // power of two
            return simpleGenerator(nothing(), getBias, input -> unsafeNextLongExclusivePowerOf2(origin, range, input));
        } else {
            return simpleGenerator(nothing(), getBias, input -> unsafeNextLongExclusive(origin, range, input));
        }
    }

    static Generator<Long> generateLongIndex(long bound) {
        return generateLongExclusiveImpl(bound, constantly(noBias()));
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


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ConstantGenerator<A> implements Generator<A> {
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
    private static class FlatMapped<In, A> implements Generator<A> {
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
    private static class BooleanGenerator implements Generator<Boolean> {
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
    private static class DoubleGenerator implements FloatingPointGenerator<Double> {
        private static Maybe<String> LABEL = Maybe.just("double");

        private final boolean includeNaNs;
        private final boolean includeInfinities;

        private static final DoubleGenerator BASIC = new DoubleGenerator(false, false);

        @Override
        public Generate<Double> prepare(Parameters parameters) {
            return Bias.applyBiasSetting(parameters.getBiasSettings()
                            .doubleBias(Double.MIN_VALUE, Double.MAX_VALUE),
                    BuildingBlocks::nextDouble);
        }

        @Override
        public FloatingPointGenerator<Double> withNaNs(boolean enabled) {
            return (enabled != includeNaNs)
                    ? new DoubleGenerator(enabled, includeInfinities)
                    : this;
        }

        @Override
        public FloatingPointGenerator<Double> withInfinities(boolean enabled) {
            return (enabled != includeInfinities)
                    ? new DoubleGenerator(includeNaNs, enabled)
                    : this;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FloatGenerator implements Generator<Float> {
        private static Maybe<String> LABEL = Maybe.just("float");

        private static final FloatGenerator INSTANCE = new FloatGenerator();

        @Override
        public Generate<Float> prepare(Parameters parameters) {
            return Bias.applyBiasSetting(parameters.getBiasSettings()
                            .floatBias(Float.MIN_VALUE, Float.MAX_VALUE),
                    BuildingBlocks::nextFloat);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class IntGenerator implements Generator<Integer> {
        private static Maybe<String> LABEL = Maybe.just("int");

        private static final IntGenerator INSTANCE = new IntGenerator();

        @Override
        public Generate<Integer> prepare(Parameters parameters) {

            return Bias.applyBiasSetting(parameters.getBiasSettings()
                            .intBias(Integer.MIN_VALUE, Integer.MAX_VALUE),
                    BuildingBlocks::nextInt);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class LongGenerator implements Generator<Long> {
        private static Maybe<String> LABEL = Maybe.just("long");

        private static final LongGenerator INSTANCE = new LongGenerator();

        @Override
        public Generate<Long> prepare(Parameters parameters) {
            return Bias.applyBiasSetting(parameters.getBiasSettings()
                            .longBias(Long.MIN_VALUE, Long.MAX_VALUE),
                    BuildingBlocks::nextLong);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class GaussianGenerator implements Generator<Double> {
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
    private static class ByteGenerator implements Generator<Byte> {
        private static Maybe<String> LABEL = Maybe.just("byte");

        private static final ByteGenerator INSTANCE = new ByteGenerator();

        @Override
        public Generate<Byte> prepare(Parameters parameters) {
            return Bias.applyBiasSetting(parameters.getBiasSettings().byteBias(Byte.MIN_VALUE, Byte.MAX_VALUE),
                    input -> nextInt(input).fmap(Integer::byteValue));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ShortGenerator implements Generator<Short> {
        private static Maybe<String> LABEL = Maybe.just("short");

        private static final ShortGenerator INSTANCE = new ShortGenerator();

        @Override
        public Generate<Short> prepare(Parameters parameters) {
            return Bias.applyBiasSetting(parameters.getBiasSettings().shortBias(Short.MIN_VALUE, Short.MAX_VALUE),
                    input -> nextInt(input).fmap(Integer::shortValue));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class BytesGenerator implements Generator<Byte[]> {
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
    private static class SizedGenerator<A> implements Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("sized");

        private final Fn1<Integer, Generator<A>> fn;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            Generate<Integer> sizeSelector = Bias.applyBiasSetting(parameters.getBiasSettings().sizeBias(parameters.getSizeParameters()),
                    sizeSelector(parameters.getSizeParameters()));
            return input -> {
                Result<? extends Seed, Integer> sizeResult = sizeSelector.apply(input);
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
    private static class WithMetadata<A> implements Generator<A> {
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

    private static <A> Generator<A> simpleGenerator(Maybe<String> label,
                                                    Fn1<Parameters, BiasSetting<A>> getBias,
                                                    Generate<A> runFn) {
        return new Generator<A>() {
            @Override
            public Generate<A> prepare(Parameters parameters) {
                return Bias.applyBiasSetting(getBias.apply(parameters), runFn);
            }

            @Override
            public Maybe<String> getLabel() {
                return label;
            }
        };
    }

}
