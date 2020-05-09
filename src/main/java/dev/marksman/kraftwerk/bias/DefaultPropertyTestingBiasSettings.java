package dev.marksman.kraftwerk.bias;

import dev.marksman.kraftwerk.SizeParameters;
import dev.marksman.kraftwerk.constraints.BigDecimalRange;
import dev.marksman.kraftwerk.constraints.BigIntegerRange;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Between.between;

public final class DefaultPropertyTestingBiasSettings implements BiasSettings {
    private static final DefaultPropertyTestingBiasSettings INSTANCE = new DefaultPropertyTestingBiasSettings();
    public static final BigDecimal SMALL_BIG_DECIMAL = BigDecimal.valueOf(0.000001);

    private DefaultPropertyTestingBiasSettings() {

    }

    @Override
    public BiasSetting<Integer> intBias(IntRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue(0)
                .addSpecialValue(1)
                .addSpecialValue(-1)
                .addSpecialValue(128)
                .addSpecialValue(-129)
                .addSpecialValue(32768)
                .addSpecialValue(-32769)
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<Long> longBias(LongRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue(0L)
                .addSpecialValue(1L)
                .addSpecialValue(-1L)
                .addSpecialValue(Integer.MAX_VALUE + 1L)
                .addSpecialValue(-(Integer.MAX_VALUE + 1L))
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<Float> floatBias(FloatRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue(0.0f)
                .addSpecialValue(-0.0f)
                .addSpecialValue(1.0f)
                .addSpecialValue(-1.0f)
                .addSpecialValue(Float.MIN_VALUE)
                .addSpecialValue(-Float.MIN_VALUE)
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<Double> doubleBias(DoubleRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue(0.0)
                .addSpecialValue(-0.0)
                .addSpecialValue(1.0)
                .addSpecialValue(-1.0)
                .addSpecialValue(Double.MIN_VALUE)
                .addSpecialValue(-Double.MIN_VALUE)
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<Byte> byteBias(ByteRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue((byte) -1)
                .addSpecialValue((byte) 0)
                .addSpecialValue((byte) 1)
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<Short> shortBias(ShortRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue((short) -1)
                .addSpecialValue((short) 0)
                .addSpecialValue((short) 1)
                .addSpecialValue((short) 128)
                .addSpecialValue((short) -129)
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<Character> charBias(CharRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue((char) 0)
                .addSpecialValue((char) 127)
                .addSpecialValue((char) 128)
                .addSpecialValue((char) 255)
                .addSpecialValue((char) 256)
                .addSpecialValue(' ')
                .addSpecialValue('\n')
                .addSpecialValue(range.maxInclusive())
                .build();
    }

    @Override
    public BiasSetting<BigInteger> bigIntegerBias(BigIntegerRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.minInclusive())
                .addSpecialValue(BigInteger.ZERO)
                .addSpecialValue(BigInteger.ONE)
                .addSpecialValue(BigInteger.ONE.negate())
                .addSpecialValue(BigInteger.valueOf(Long.MAX_VALUE))
                .addSpecialValue(BigInteger.valueOf(Long.MIN_VALUE))
                .addSpecialValue(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE))
                .addSpecialValue(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE))
                .addSpecialValue(range.maxExclusive().subtract(BigInteger.ONE))
                .build();
    }

    @Override
    public BiasSetting<BigDecimal> bigDecimalBias(BigDecimalRange range) {
        return BiasSetting.builder(range::includes)
                .addSpecialValue(range.min())
                .addSpecialValue(range.min().add(SMALL_BIG_DECIMAL))
                .addSpecialValue(SMALL_BIG_DECIMAL.negate())
                .addSpecialValue(BigDecimal.ZERO)
                .addSpecialValue(SMALL_BIG_DECIMAL)
                .addSpecialValue(BigDecimal.ONE)
                .addSpecialValue(BigDecimal.ONE.negate())
                .addSpecialValue(BigDecimal.valueOf(Long.MAX_VALUE))
                .addSpecialValue(BigDecimal.valueOf(Long.MIN_VALUE))
                .addSpecialValue(BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.ONE))
                .addSpecialValue(BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.ONE))
                .addSpecialValue(range.max().subtract(SMALL_BIG_DECIMAL))
                .addSpecialValue(range.max())
                .build();
    }

    @Override
    public BiasSetting<Integer> sizeBias(SizeParameters sizeParameters) {
        Integer min = sizeParameters.getMinSize().orElse(0);
        Integer max = sizeParameters.getMaxSize().orElse(1);
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(0)
                .addSpecialValue(1)
                .build();
    }

    public static DefaultPropertyTestingBiasSettings defaultPropertyTestBiasSettings() {
        return INSTANCE;
    }

}
