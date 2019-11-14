package dev.marksman.kraftwerk;

import static dev.marksman.kraftwerk.CompositeBiasSettings.compositeBiasSettings;

public interface BiasSettings {
    BiasSetting<Integer> intBias(int min, int max);

    BiasSetting<Long> longBias(long min, long max);

    BiasSetting<Float> floatBias(float min, float max);

    BiasSetting<Double> doubleBias(double min, double max);

    BiasSetting<Byte> byteBias(byte min, byte max);

    BiasSetting<Short> shortBias(short min, short max);

    BiasSetting<Integer> sizeBias(SizeParameters sizeParameters);

    default BiasSettings overrideWith(BiasSettings other) {
        return compositeBiasSettings(other, this);
    }
}
