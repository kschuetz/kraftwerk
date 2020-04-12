package dev.marksman.kraftwerk.bias;

import dev.marksman.kraftwerk.SizeParameters;
import dev.marksman.kraftwerk.constraints.*;

import static dev.marksman.kraftwerk.bias.CompositeBiasSettings.compositeBiasSettings;

public interface BiasSettings {
    BiasSetting<Integer> intBias(IntRange range);

    BiasSetting<Long> longBias(LongRange range);

    BiasSetting<Float> floatBias(FloatRange range);

    BiasSetting<Double> doubleBias(DoubleRange range);

    BiasSetting<Byte> byteBias(ByteRange range);

    BiasSetting<Short> shortBias(ShortRange range);

    BiasSetting<Integer> sizeBias(SizeParameters sizeParameters);

    default BiasSettings overrideWith(BiasSettings other) {
        return compositeBiasSettings(other, this);
    }
}
