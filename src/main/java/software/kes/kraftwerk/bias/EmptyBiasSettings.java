package software.kes.kraftwerk.bias;

import software.kes.kraftwerk.SizeParameters;
import software.kes.kraftwerk.constraints.BigDecimalRange;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.ByteRange;
import software.kes.kraftwerk.constraints.CharRange;
import software.kes.kraftwerk.constraints.DoubleRange;
import software.kes.kraftwerk.constraints.FloatRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LongRange;
import software.kes.kraftwerk.constraints.ShortRange;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link BiasSettings} with all settings set to {@link BiasSetting.NoBias}.
 */
public final class EmptyBiasSettings implements BiasSettings {
    private static final EmptyBiasSettings INSTANCE = new EmptyBiasSettings();

    private EmptyBiasSettings() {

    }

    /**
     * Creates an {@code EmptyBiasSettings}.
     */
    public static BiasSettings emptyBiasSettings() {
        return INSTANCE;
    }

    @Override
    public BiasSetting<Integer> intBias(IntRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Long> longBias(LongRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Float> floatBias(FloatRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Double> doubleBias(DoubleRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Byte> byteBias(ByteRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Short> shortBias(ShortRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Character> charBias(CharRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<BigInteger> bigIntegerBias(BigIntegerRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<BigDecimal> bigDecimalBias(BigDecimalRange range) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSetting<Integer> sizeBias(SizeParameters sizeParameters) {
        return BiasSetting.noBias();
    }

    @Override
    public BiasSettings overrideWith(BiasSettings other) {
        return other;
    }
}
