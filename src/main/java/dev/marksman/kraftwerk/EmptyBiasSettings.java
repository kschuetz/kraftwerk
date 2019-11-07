package dev.marksman.kraftwerk;

import static dev.marksman.kraftwerk.BiasSetting.noBias;

public final class EmptyBiasSettings implements BiasSettings {
    private static final EmptyBiasSettings INSTANCE = new EmptyBiasSettings();

    private EmptyBiasSettings() {

    }

    @Override
    public BiasSetting<Integer> intBias(int min, int max) {
        return noBias();
    }

    @Override
    public BiasSetting<Long> longBias(long min, long max) {
        return noBias();
    }

    @Override
    public BiasSetting<Float> floatBias(float min, float max) {
        return noBias();
    }

    @Override
    public BiasSetting<Double> doubleBias(double min, double max) {
        return noBias();
    }

    @Override
    public BiasSetting<Byte> byteBias(byte min, byte max) {
        return noBias();
    }

    @Override
    public BiasSetting<Short> shortBias(short min, short max) {
        return noBias();
    }

    @Override
    public BiasSetting<Character> charBias(char min, char max) {
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

    public static BiasSettings emptyBiasSettings() {
        return INSTANCE;
    }
}
