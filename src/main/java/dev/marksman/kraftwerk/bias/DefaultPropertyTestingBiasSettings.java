package dev.marksman.kraftwerk.bias;

import dev.marksman.kraftwerk.SizeParameters;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Between.between;

public final class DefaultPropertyTestingBiasSettings implements BiasSettings {
    private static DefaultPropertyTestingBiasSettings INSTANCE = new DefaultPropertyTestingBiasSettings();

    private DefaultPropertyTestingBiasSettings() {

    }

    @Override
    public BiasSetting<Integer> intBias(int min, int max) {
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(min)
                .addSpecialValue(0)
                .addSpecialValue(1)
                .addSpecialValue(-1)
                .addSpecialValue(128)
                .addSpecialValue(-129)
                .addSpecialValue(32768)
                .addSpecialValue(-32769)
                .addSpecialValue(max)
                .build();
    }

    @Override
    public BiasSetting<Long> longBias(long min, long max) {
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(min)
                .addSpecialValue(0L)
                .addSpecialValue(1L)
                .addSpecialValue(-1L)
                .addSpecialValue(Integer.MAX_VALUE + 1L)
                .addSpecialValue(-(Integer.MAX_VALUE + 1L))
                .addSpecialValue(max)
                .build();
    }

    @Override
    public BiasSetting<Float> floatBias(float min, float max) {
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(min)
                .addSpecialValue(0.0f)
                .addSpecialValue(-0.0f)
                .addSpecialValue(1.0f)
                .addSpecialValue(-1.0f)
                .addSpecialValue(Float.MIN_VALUE)
                .addSpecialValue(-Float.MIN_VALUE)
                .addSpecialValue(max)
                .build();
    }

    @Override
    public BiasSetting<Double> doubleBias(double min, double max) {
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(min)
                .addSpecialValue(0.0)
                .addSpecialValue(-0.0)
                .addSpecialValue(1.0)
                .addSpecialValue(-1.0)
                .addSpecialValue(Double.MIN_VALUE)
                .addSpecialValue(-Double.MIN_VALUE)
                .addSpecialValue(max)
                .build();
    }

    @Override
    public BiasSetting<Byte> byteBias(byte min, byte max) {
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(min)
                .addSpecialValue((byte) -1)
                .addSpecialValue((byte) 0)
                .addSpecialValue((byte) 1)
                .addSpecialValue(max)
                .build();
    }

    @Override
    public BiasSetting<Short> shortBias(short min, short max) {
        return BiasSetting.builder(between(min, max))
                .addSpecialValue(min)
                .addSpecialValue((short) -1)
                .addSpecialValue((short) 0)
                .addSpecialValue((short) 1)
                .addSpecialValue((short) 128)
                .addSpecialValue((short) -129)
                .addSpecialValue(max)
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
