package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.bias.BiasSetting;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import dev.marksman.kraftwerk.core.BuildingBlocks;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.util.Labeling;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.Normalize.normalizeSizeRange;
import static dev.marksman.kraftwerk.Result.result;
import static dev.marksman.kraftwerk.SizeParameters.sizeParameters;
import static dev.marksman.kraftwerk.SizeSelectors.sizeSelector;
import static dev.marksman.kraftwerk.bias.BiasSetting.noBias;
import static dev.marksman.kraftwerk.core.BuildingBlocks.checkBound;
import static dev.marksman.kraftwerk.core.BuildingBlocks.checkCount;
import static dev.marksman.kraftwerk.core.BuildingBlocks.checkMinMax;
import static dev.marksman.kraftwerk.core.BuildingBlocks.checkOriginBound;
import static dev.marksman.kraftwerk.core.BuildingBlocks.nextBytes;
import static dev.marksman.kraftwerk.core.BuildingBlocks.nextInt;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextDoubleBetween;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextIntBounded;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextIntBoundedPowerOf2;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextIntExclusive;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextIntExclusivePowerOf2;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextIntExclusiveWide;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextLongExclusive;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextLongExclusivePowerOf2;
import static dev.marksman.kraftwerk.core.BuildingBlocks.unsafeNextLongExclusiveWithOverflow;

final class Primitives {
    private static final DoubleRange DEFAULT_DOUBLE_RANGE = DoubleRange.inclusive(-1E16, 1E16);
    private static final FloatRange DEFAULT_FLOAT_RANGE = FloatRange.inclusive(-1E7f, 1E7f);

    private Primitives() {

    }

    static Generator<Integer> generateInt() {
        return IntGenerator.INSTANCE;
    }

    static Generator<Integer> generateInt(IntRange range) {
        return generateInt(range.minInclusive(), range.maxInclusive());
    }

    private static Generator<Integer> generateInt(int min, int max) {
        checkMinMax(min, max);
        if (min == max) {
            return constant(min).labeled(Labeling.intInterval(min, max, false));
        } else if (max == Integer.MAX_VALUE) {
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
        return new DoubleGenerator(just(DEFAULT_DOUBLE_RANGE), false, false);
    }

    static FloatingPointGenerator<Double> generateDoubleFractional() {
        return DoubleGenerator.DEFAULT_DOUBLE_GENERATOR;
    }

    static FloatingPointGenerator<Double> generateDouble(DoubleRange range) {
        return new DoubleGenerator(just(range), false, false);
    }

    static FloatingPointGenerator<Float> generateFloat() {
        return new FloatGenerator(just(DEFAULT_FLOAT_RANGE), false, false);
    }

    static FloatingPointGenerator<Float> generateFloatFractional() {
        return FloatGenerator.DEFAULT_FLOAT_GENERATOR;
    }

    static FloatingPointGenerator<Float> generateFloat(FloatRange range) {
        return new FloatGenerator(just(range), false, false);
    }

    static Generator<Long> generateLong() {
        return LongGenerator.INSTANCE;
    }

    static Generator<Long> generateLong(LongRange range) {
        return generateLong(range.minInclusive(), range.maxInclusive());
    }

    private static Generator<Long> generateLong(long min, long max) {
        checkMinMax(min, max);
        if (min == max) {
            return constant(min).labeled(Labeling.longInterval(min, max, false));
        } else if (max == Long.MAX_VALUE) {
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

    static Generator<Byte> generateByte() {
        return ByteGenerator.DEFAULT_BYTE_GENERATOR;
    }

    static Generator<Byte> generateByte(ByteRange range) {
        return new ByteGenerator(range);
    }

    static Generator<Short> generateShort() {
        return ShortGenerator.DEFAULT_SHORT_GENERATOR;
    }

    static Generator<Short> generateShort(ShortRange range) {
        return new ShortGenerator(range);
    }

    static Generator<Character> generateChar() {
        return CharGenerator.DEFAULT_CHAR_GENERATOR;
    }

    static Generator<Character> generateChar(CharRange range) {
        return new CharGenerator(range);
    }

    static Generator<Object> generateBoxedPrimitive() {
        return FrequencyMap.<Object>frequencyMap(generateInt())
                .add(generateLong())
                .add(generateShort())
                .add(generateByte())
                .add(generateDoubleFractional())
                .add(generateFloatFractional())
                .add(generateBoolean())
                .add(generateChar())
                .toGenerator();
    }

    static Generator<Double> generateGaussian() {
        return GaussianGenerator.INSTANCE;
    }

    static Generator<Byte[]> generateByteArray() {
        return sized(Primitives::generateByteArray);
    }

    static Generator<Byte[]> generateByteArray(int count) {
        checkCount(count);
        return new BytesGenerator(count);
    }

    static Generator<Seed> generateSeed() {
        return SeedGenerator.INSTANCE;
    }

    static Generator<Integer> generateSize() {
        return SizeGenerator.INSTANCE;
    }

    static Generator<Integer> generateSize(IntRange sizeRange) {
        IntRange normalizedSizeRange = normalizeSizeRange(sizeRange);
        return new RangedSizeGenerator(normalizedSizeRange);
    }

    static <A> Generator<A> sized(Fn1<Integer, Generator<A>> fn) {
        return generateSize().flatMap(fn);
    }

    private static <A> Generator<A> simpleGenerator(Maybe<String> label,
                                                    Fn1<GeneratorParameters, BiasSetting<A>> getBias,
                                                    GenerateFn<A> runFn) {
        return new Generator<A>() {
            @Override
            public GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters) {
                return Bias.applyBiasSetting(getBias.apply(generatorParameters), runFn);
            }

            @Override
            public Maybe<String> getLabel() {
                return label;
            }
        };
    }

    private static class BooleanGenerator implements Generator<Boolean> {
        private static final Maybe<String> LABEL = Maybe.just("boolean");

        private static final BooleanGenerator INSTANCE = new BooleanGenerator();

        private BooleanGenerator() {
        }

        @Override
        public GenerateFn<Boolean> createGenerateFn(GeneratorParameters generatorParameters) {
            return BuildingBlocks::nextBoolean;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    private static class DoubleGenerator implements FloatingPointGenerator<Double> {
        private static final Maybe<String> LABEL = Maybe.just("double");
        private static final DoubleRange DEFAULT_RANGE = DoubleRange.exclusive(1d);
        private static final DoubleGenerator DEFAULT_DOUBLE_GENERATOR = new DoubleGenerator(nothing(), false, false);
        private final Maybe<DoubleRange> range;
        private final boolean includeNaNs;
        private final boolean includeInfinities;

        private DoubleGenerator(Maybe<DoubleRange> range, boolean includeNaNs, boolean includeInfinities) {
            this.range = range;
            this.includeNaNs = includeNaNs;
            this.includeInfinities = includeInfinities;
        }

        @Override
        public GenerateFn<Double> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(buildBiasSetting(generatorParameters),
                    range.match(__ -> defaultGenerate(),
                            this::constrainedGenerate));
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

        private BiasSetting<Double> buildBiasSetting(GeneratorParameters generatorParameters) {
            DoubleRange range = this.range.orElse(DEFAULT_RANGE);
            BiasSetting<Double> bias = generatorParameters.getBiasSettings().doubleBias(range);
            ArrayList<Double> specialValues = new ArrayList<>();
            if (includeNaNs) {
                specialValues.add(Double.NaN);
            }
            if (includeInfinities) {
                specialValues.add(Double.NEGATIVE_INFINITY);
                specialValues.add(Double.POSITIVE_INFINITY);
            }
            return bias.addSpecialValues(specialValues);
        }

        private GenerateFn<Double> defaultGenerate() {
            return BuildingBlocks::nextDoubleFractional;
        }

        private GenerateFn<Double> constrainedGenerate(DoubleRange range) {
            double min = range.minInclusive();
            double max = range.maxInclusive();
            if (min == max) {
                return input -> result(input, min);
            } else if (max == Math.nextAfter(min, Double.POSITIVE_INFINITY)) {
                return input -> {
                    Result<Seed, Boolean> booleanResult = BuildingBlocks.nextBoolean(input);
                    return booleanResult.getValue()
                            ? result(booleanResult.getNextState(), min)
                            : result(booleanResult.getNextState(), max);
                };
            } else {
                double maxExclusive = range.maxExclusive();
                double bound = maxExclusive == Double.POSITIVE_INFINITY ? max : maxExclusive;
                return input -> unsafeNextDoubleBetween(min, bound, input);
            }
        }
    }

    private static class FloatGenerator implements FloatingPointGenerator<Float> {
        private static final Maybe<String> LABEL = Maybe.just("float");
        private static final FloatRange DEFAULT_RANGE = FloatRange.exclusive(1f);
        private static final FloatGenerator DEFAULT_FLOAT_GENERATOR = new FloatGenerator(nothing(), false, false);
        private final Maybe<FloatRange> range;
        private final boolean includeNaNs;
        private final boolean includeInfinities;

        private FloatGenerator(Maybe<FloatRange> range, boolean includeNaNs, boolean includeInfinities) {
            this.range = range;
            this.includeNaNs = includeNaNs;
            this.includeInfinities = includeInfinities;
        }

        @Override
        public GenerateFn<Float> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(buildBiasSetting(generatorParameters),
                    range.match(__ -> defaultGenerate(),
                            this::constrainedGenerate));
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

        private BiasSetting<Float> buildBiasSetting(GeneratorParameters generatorParameters) {
            FloatRange range = this.range.orElse(DEFAULT_RANGE);
            BiasSetting<Float> bias = generatorParameters.getBiasSettings().floatBias(range);
            ArrayList<Float> specialValues = new ArrayList<>();
            if (includeNaNs) {
                specialValues.add(Float.NaN);
            }
            if (includeInfinities) {
                specialValues.add(Float.NEGATIVE_INFINITY);
                specialValues.add(Float.POSITIVE_INFINITY);
            }
            return bias.addSpecialValues(specialValues);
        }

        private GenerateFn<Float> defaultGenerate() {
            return BuildingBlocks::nextFloatFractional;
        }

        private GenerateFn<Float> constrainedGenerate(FloatRange range) {
            float min = range.minInclusive();
            float max = range.maxInclusive();
            if (min == max) {
                return input -> result(input, min);
            } else if (max == Math.nextAfter(min, Float.POSITIVE_INFINITY)) {
                return input -> {
                    Result<Seed, Boolean> booleanResult = BuildingBlocks.nextBoolean(input);
                    return booleanResult.getValue()
                            ? result(booleanResult.getNextState(), min)
                            : result(booleanResult.getNextState(), max);
                };
            } else {
                double bound = Math.nextAfter((double) max, Double.POSITIVE_INFINITY);
                return input -> {
                    Result<Seed, Double> doubleResult = unsafeNextDoubleBetween(min, bound, input);
                    double doubleValue = doubleResult.getValue();
                    return result(doubleResult.getNextState(), (float) doubleValue);
                };
            }
        }

    }

    private static class IntGenerator implements Generator<Integer> {
        private static final Maybe<String> LABEL = Maybe.just("int");

        private static final IntGenerator INSTANCE = new IntGenerator();

        private IntGenerator() {
        }

        @Override
        public GenerateFn<Integer> createGenerateFn(GeneratorParameters generatorParameters) {

            return Bias.applyBiasSetting(generatorParameters.getBiasSettings()
                            .intBias(IntRange.fullRange()),
                    BuildingBlocks::nextInt);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static class LongGenerator implements Generator<Long> {
        private static final Maybe<String> LABEL = Maybe.just("long");

        private static final LongGenerator INSTANCE = new LongGenerator();

        private LongGenerator() {
        }

        @Override
        public GenerateFn<Long> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings()
                            .longBias(LongRange.fullRange()),
                    BuildingBlocks::nextLong);
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static class GaussianGenerator implements Generator<Double> {
        private static final Maybe<String> LABEL = Maybe.just("gaussian");

        private static final GaussianGenerator INSTANCE = new GaussianGenerator();

        private GaussianGenerator() {
        }

        @Override
        public GenerateFn<Double> createGenerateFn(GeneratorParameters generatorParameters) {
            return BuildingBlocks::nextGaussian;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static class ByteGenerator implements Generator<Byte> {
        private static final Maybe<String> LABEL = Maybe.just("byte");

        private static final ByteGenerator DEFAULT_BYTE_GENERATOR = new ByteGenerator(ByteRange.fullRange());

        private final ByteRange range;

        private ByteGenerator(ByteRange range) {
            this.range = range;
        }

        @Override
        public GenerateFn<Byte> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().byteBias(range),
                    input -> unsafeNextIntExclusive(0, 256, input).fmap(getMapper()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

        private Fn1<Integer, Byte> getMapper() {
            if (range.minInclusive() == Byte.MIN_VALUE && range.maxInclusive() == Byte.MAX_VALUE) {
                return Integer::byteValue;
            } else {
                byte min = range.minInclusive();
                int span = (range.maxInclusive() - min) + 1;
                return i -> (byte) (min + (i % span));
            }
        }

    }

    private static class ShortGenerator implements Generator<Short> {
        private static final Maybe<String> LABEL = Maybe.just("short");

        private static final ShortGenerator DEFAULT_SHORT_GENERATOR = new ShortGenerator(ShortRange.fullRange());

        private final ShortRange range;

        private ShortGenerator(ShortRange range) {
            this.range = range;
        }

        @Override
        public GenerateFn<Short> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().shortBias(range),
                    input -> unsafeNextIntExclusive(0, 65536, input).fmap(getMapper()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

        private Fn1<Integer, Short> getMapper() {
            if (range.minInclusive() == Short.MIN_VALUE && range.maxInclusive() == Short.MAX_VALUE) {
                return Integer::shortValue;
            } else {
                short min = range.minInclusive();
                int span = (range.maxInclusive() - min) + 1;
                return i -> (short) (min + (i % span));
            }
        }
    }

    private static class CharGenerator implements Generator<Character> {
        private static final Maybe<String> LABEL = Maybe.just("char");

        private static final CharGenerator DEFAULT_CHAR_GENERATOR = new CharGenerator(CharRange.fullRange());

        private final CharRange range;

        private CharGenerator(CharRange range) {
            this.range = range;
        }

        @Override
        public GenerateFn<Character> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().charBias(range),
                    input -> nextInt(input).fmap(getMapper()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

        private Fn1<Integer, Character> getMapper() {
            int min = range.minInclusive();
            int span = (range.maxInclusive() - min) + 1;
            return i -> (char) (min + (i % span));
        }
    }

    private static class BytesGenerator implements Generator<Byte[]> {
        private final int count;

        private BytesGenerator(int count) {
            this.count = count;
        }

        @Override
        public GenerateFn<Byte[]> createGenerateFn(GeneratorParameters generatorParameters) {
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

    private static class SeedGenerator implements Generator<Seed> {
        private static final Maybe<String> LABEL = Maybe.just("seed");

        private static final SeedGenerator INSTANCE = new SeedGenerator();

        @Override
        public GenerateFn<Seed> createGenerateFn(GeneratorParameters generatorParameters) {
            return seed -> {
                Result<Seed, Long> longResult = BuildingBlocks.nextLong(seed);
                return result(longResult.getNextState(), Seed.create(longResult.getValue()));
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static class SizeGenerator implements Generator<Integer> {
        private static final Maybe<String> LABEL = Maybe.just("size");

        private static final SizeGenerator INSTANCE = new SizeGenerator();

        private SizeGenerator() {
        }

        @Override
        public GenerateFn<Integer> createGenerateFn(GeneratorParameters generatorParameters) {
            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().sizeBias(generatorParameters.getSizeParameters()),
                    sizeSelector(generatorParameters.getSizeParameters()));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    private static class RangedSizeGenerator implements Generator<Integer> {
        private static final Maybe<String> LABEL = Maybe.just("size");
        private final IntRange sizeRange;

        private RangedSizeGenerator(IntRange sizeRange) {
            this.sizeRange = sizeRange;
        }

        @Override
        public GenerateFn<Integer> createGenerateFn(GeneratorParameters generatorParameters) {
            SizeParameters sizeParameters = sizeParameters(Maybe.just(sizeRange.minInclusive()),
                    Maybe.just(sizeRange.maxInclusive()),
                    generatorParameters.getSizeParameters().getPreferredSize().filter(sizeRange::includes));

            return Bias.applyBiasSetting(generatorParameters.getBiasSettings().sizeBias(sizeParameters),
                    sizeSelector(sizeParameters));
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }
}
