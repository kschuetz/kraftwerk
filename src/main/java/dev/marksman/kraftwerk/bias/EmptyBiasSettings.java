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

import static dev.marksman.kraftwerk.bias.BiasSetting.noBias;

/**
 * {@link BiasSettings} with all settings set to {@link dev.marksman.kraftwerk.bias.BiasSetting.NoBias}.
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
        return noBias();
    }

    @Override
    public BiasSetting<Long> longBias(LongRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<Float> floatBias(FloatRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<Double> doubleBias(DoubleRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<Byte> byteBias(ByteRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<Short> shortBias(ShortRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<Character> charBias(CharRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<BigInteger> bigIntegerBias(BigIntegerRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<BigDecimal> bigDecimalBias(BigDecimalRange range) {
        return noBias();
    }

    @Override
    public BiasSetting<Integer> sizeBias(SizeParameters sizeParameters) {
        return noBias();
    }

    @Override
    public BiasSettings overrideWith(BiasSettings other) {
        return other;
    }
}
