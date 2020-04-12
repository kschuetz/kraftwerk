package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.bias.BiasSetting;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.core.BuildingBlocks;
import dev.marksman.kraftwerk.util.Labeling;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static dev.marksman.kraftwerk.SizeSelectors.sizeSelector;
import static dev.marksman.kraftwerk.bias.BiasSetting.noBias;
import static dev.marksman.kraftwerk.core.BuildingBlocks.*;

class Primitives {

    static Generator<Integer> generateInt() {
        return IntGenerator.INSTANCE;
    }

    static Generator<Integer> generateInt(IntRange range) {
        return generateInt(range.min(), range.max());
    }

    private static Generator<Integer> generateInt(int min, int max) {
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

    private static Generator<Integer> generateIntExclusive(int bound) {
        return generateIntExclusiveImpl(bound, p -> p.getBiasSettings().intBias(IntRange.exclusive(bound)));
    }

    private static Generator<Integer> generateIntExclusiveImpl(int bound,
                                                               Fn1<GeneratorParameters, BiasSetting<Integer>> getBias) {
        checkBound(bound);
        Maybe<String> label = just(Labeling.intInterval(0, bound, true));

        if ((bound & -bound) == bound) { // bound is a power of 2
            return simpleGenerator(label, getBias, input -> unsafeNextIntBoundedPowerOf2(bound, input));
        } else {
            return simpleGenerator(label, getBias, input -> unsafeNextIntBounded(bound, input));
        }

    }

    private static Generator<Integer> generateIntExclusive(int origin, int bound) {
        return generateIntExclusiveImpl(origin, bound, p -> p.getBiasSettings().intBias(IntRange.exclusive(origin, bound)));
    }

    private static Generator<Integer> generateIntExclusiveImpl(int origin, int bound,
                                                               Fn1<GeneratorParameters, BiasSetting<Integer>> getBias) {
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
        return DoubleGenerator.DEFAULT_DOUBLE_GENERATOR;
    }

    static FloatingPointGenerator<Double> generateDouble(DoubleRange range) {
        return new DoubleGenerator(just(range), false, false);
    }

    static FloatingPointGenerator<Float> generateFloat() {
        return FloatGenerator.DEFAULT_FLOAT_GENERATOR;
    }

    static FloatingPointGenerator<Float> generateFloat(FloatRange range) {
        return new FloatGenerator(just(range), false, false);
    }

    static Generator<Long> generateLong() {
        return LongGenerator.INSTANCE;
    }

    static Generator<Long> generateLong(LongRange range) {
        return generateLong(range.min(), range.max());
    }

    private static Generator<Long> generateLong(long min, long max) {
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

    private static Generator<Long> generateLongExclusive(long bound) {
        checkBound(bound);
        if (bound <= Integer.MAX_VALUE) {
            return generateIntExclusive((int) bound).fmap(Integer::longValue);
        } else {
            return generateLongExclusive(0, bound);
        }
    }

    static Generator<Long> generateLongExclusiveImpl(long bound,
                                                     Fn1<GeneratorParameters, BiasSetting<Long>> getBias) {
        checkBound(bound);
        if (bound <= Integer.MAX_VALUE) {
            return generateIntExclusive((int) bound).fmap(Integer::longValue);
        } else {
            return generateLongExclusive(0, bound);
        }
    }

    private static Generator<Long> generateLongExclusive(long origin, long bound) {
        return generateLongExclusiveImpl(origin, bound, p -> p.getBiasSettings().longBias(LongRange.exclusive(origin, bound)));
    }

    private static Generator<Long> generateLongExclusiveImpl(long origin, long bound, Fn1<GeneratorParameters, BiasSetting<Long>> getBias) {
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

    static Generator<Integer> generateSize() {
        return SizeGenerator.INSTANCE;
    }

    static <A> Generator<A> sized(Fn1<Integer, Generator<A>> fn) {
        return generateSize().flatMap(fn);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class BooleanGenerator implements Generator<Boolean> {
        private static final Maybe<String> LABEL = Maybe.just("boolean");

        private static final BooleanGenerator INSTANCE = new BooleanGenerator();

        @Override
        public Generate<Boolean> prepare(GeneratorParameters generatorParameters) {
            return BuildingBlocks::nextBoolean;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class DoubleGenerator implements FloatingPointGenerator<Double> {
        private static final Maybe<String> LABEL = Maybe.just("double");
        private static final DoubleRange DEFAULT_RANGE = DoubleRange.exclusive(1d);

        private final Maybe<DoubleRange> range;
        private final boolean includeNaNs;
        private final boolean includeInfinities;

        private static final DoubleGenerator DEFAULT_DOUBLE_GENERATOR = new DoubleGenerator(nothing(), false, false);

        @Override
        public Generate<Double> prepare(GeneratorParameters generatorParameters) {
            DoubleRange actualRange = this.range.orElse(DEFAULT_RANGE);
            BiasSetting<Double> bias = buildBiasSetting(generatorParameters, actualRange);
            Generate<Double> generate = BuildingBlocks::nextDouble; // TODO: handle range for doubles

            return Bias.applyBiasSetting(bias, generate);
        }

        @Override
        public FloatingPointGenerator<Double> withNaNs(boolean enabled) {
            return (enabled != includeNaNs)
                    ? new DoubleGenerator(range, enabled, includeInfinities)
                    : this;
        }

        @Override
        public FloatingPointGenerator<Double> withInfinities(boolean enabled) {
            return (enabled != includeInfinities)
                    ? new DoubleGenerator(range, includeNaNs, enabled)
                    : this;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

        private BiasSetting<Double> buildBiasSetting(GeneratorParameters generatorParameters, DoubleRange range) {
            BiasSetting<Double> bias = generatorParameters.getBiasSettings().doubleBias(range);
            ArrayList<Double> specialValues = new ArrayList<>();
            if (includeNaNs) {
                specialValues.add(Double.NaN);
            }
            if (includeInfinities) {
                if (range.contains(Double.MIN_VALUE)) {
                    specialValues.add(Double.NEGATIVE_INFINITY);
                }
                if (range.contains(Double.MAX_VALUE)) {
                    specialValues.add(Double.POSITIVE_INFINITY);
                }
            }
            return bias.addSpecialValues(specialValues);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FloatGenerator implements FloatingPointGenerator<Float> {
        private static final Maybe<String> LABEL = Maybe.just("float");
        private static final FloatRange DEFAULT_RANGE = FloatRange.exclusive(1f);

        private final Maybe<FloatRange> range;
        private final boolean includeNaNs;
        private final boolean includeInfinities;

        private static final FloatGenerator DEFAULT_FLOAT_GENERATOR = new FloatGenerator(nothing(), false, false);

        @Override
        public Generate<Float> prepare(GeneratorParameters generatorParameters) {
            FloatRange actualRange = this.range.orElse(DEFAULT_RANGE);
            BiasSetting<Float> bias = buildBiasSetting(generatorParameters, actualRange);
            Generate<Float> generate = BuildingBlocks::nextFloat; // TODO: handle range for floats

            return Bias.applyBiasSetting(bias, generate);
        }

        @Override
        public FloatingPointGenerator<Float> withNaNs(boolean enabled) {
            return (enabled != includeNaNs)
                    ? new FloatGenerator(range, enabled, includeInfinities)
                    : this;
        }

        @Override
        public FloatingPointGenerator<Float> withInfinities(boolean enabled) {
            return (enabled != includeInfinities)
                    ? new FloatGenerator(range, includeNaNs, enabled)
                    : this;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

        private BiasSetting<Float> buildBiasSetting(GeneratorParameters generatorParameters, FloatRange range) {
            BiasSetting<Float> bias = generatorParameters.getBiasSettings().floatBias(range);
            ArrayList<Float> specialValues = new ArrayList<>();
            if (includeNaNs) {
                specialValues.add(Float.NaN);
            }
            if (includeInfinities) {
                if (range.contains(Float.MIN_VALUE)) {
                    specialValues.add(Float.NEGATIVE_INFINITY);
                }
                if (range.contains(Float.MAX_VALUE)) {
                    specialValues.add(Float.POSITIVE_INFINITY);
                }
            }
            return bias.addSpecialValues(specialValues);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class IntGenerator implements Generator<Integer> {
        private static final Maybe<String> LABEL = Maybe.just("int");

        private static final IntGenerator INSTANCE = new IntGenerator();

        @Override
        public Generate<Integer> prepare(GeneratorParameters generatorParameters) {

            return Bias.applyBiasSetting(generatorParameters.getBiasSettings()
                            .intBias(IntRange.fullRange()),
                    BuildingBlocks::nextInt);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class LongGenerator implements Generator<Long> {
        private static final Maybe<String> LABEL = Maybe.just("long");

        private static final LongGenerator INSTANCE = new LongGenerator();

        @Override
        public Generate<Long> prepare(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings()
                            .longBias(LongRange.fullRange()),
                    BuildingBlocks::nextLong);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class GaussianGenerator implements Generator<Double> {
        private static final Maybe<String> LABEL = Maybe.just("gaussian");

        private static final GaussianGenerator INSTANCE = new GaussianGenerator();

        @Override
        public Generate<Double> prepare(GeneratorParameters generatorParameters) {
            return BuildingBlocks::nextGaussian;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ByteGenerator implements Generator<Byte> {
        private static final Maybe<String> LABEL = Maybe.just("byte");

        private static final ByteGenerator INSTANCE = new ByteGenerator();

        @Override
        public Generate<Byte> prepare(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().byteBias(Byte.MIN_VALUE, Byte.MAX_VALUE),
                    input -> nextInt(input).fmap(Integer::byteValue));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ShortGenerator implements Generator<Short> {
        private static final Maybe<String> LABEL = Maybe.just("short");

        private static final ShortGenerator INSTANCE = new ShortGenerator();

        @Override
        public Generate<Short> prepare(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().shortBias(Short.MIN_VALUE, Short.MAX_VALUE),
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
        public Generate<Byte[]> prepare(GeneratorParameters generatorParameters) {
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
    private static class SizeGenerator implements Generator<Integer> {
        private static final Maybe<String> LABEL = Maybe.just("size");

        private static SizeGenerator INSTANCE = new SizeGenerator();

        @Override
        public Generate<Integer> prepare(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().sizeBias(generatorParameters.getSizeParameters()),
                    sizeSelector(generatorParameters.getSizeParameters()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    private static <A> Generator<A> simpleGenerator(Maybe<String> label,
                                                    Fn1<GeneratorParameters, BiasSetting<A>> getBias,
                                                    Generate<A> runFn) {
        return new Generator<A>() {
            @Override
            public Generate<A> prepare(GeneratorParameters generatorParameters) {
                return Bias.applyBiasSetting(getBias.apply(generatorParameters), runFn);
            }

            @Override
            public Maybe<String> getLabel() {
                return label;
            }
        };
    }

}
