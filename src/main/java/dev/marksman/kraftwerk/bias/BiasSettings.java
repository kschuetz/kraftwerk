package dev.marksman.kraftwerk.bias;

import dev.marksman.kraftwerk.Generator;
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

import static dev.marksman.kraftwerk.bias.CompositeBiasSettings.compositeBiasSettings;

/**
 * A collection of guidelines for injecting special values into the output of a {@link Generator}.
 * <p>
 * This is a low-level concept that should only be of concern for authors of property-testing libraries,
 * and not so much for users.  See {@link DefaultPropertyTestingBiasSettings#defaultPropertyTestBiasSettings()} for
 * a set of default value for property-testing purposed.
 */
public interface BiasSettings {
    BiasSetting<Integer> intBias(IntRange range);

    BiasSetting<Long> longBias(LongRange range);

    BiasSetting<Float> floatBias(FloatRange range);

    BiasSetting<Double> doubleBias(DoubleRange range);

    BiasSetting<Byte> byteBias(ByteRange range);

    BiasSetting<Short> shortBias(ShortRange range);

    BiasSetting<Character> charBias(CharRange range);

    BiasSetting<BigInteger> bigIntegerBias(BigIntegerRange range);

    BiasSetting<BigDecimal> bigDecimalBias(BigDecimalRange range);

    BiasSetting<Integer> sizeBias(SizeParameters sizeParameters);

    /**
     * Returns a new {@code BiasSettings} that is the same as this one, but with certain settings overridden.
     * <p>
     * Any {@link BiasSetting} in {@code other} that is not {@link BiasSetting.NoBias} will
     * override the setting found in this {@code BiasSettings}.
     *
     * @param other the other {@code BiasSettings}
     * @return a {@code BiasSettings}
     */
    default BiasSettings overrideWith(BiasSettings other) {
        return compositeBiasSettings(other, this);
    }
}
