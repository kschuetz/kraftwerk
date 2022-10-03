package software.kes.kraftwerk.bias;

import software.kes.kraftwerk.Generator;
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
        return CompositeBiasSettings.compositeBiasSettings(other, this);
    }
}
