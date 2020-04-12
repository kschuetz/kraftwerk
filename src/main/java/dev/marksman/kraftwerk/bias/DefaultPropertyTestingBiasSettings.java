package dev.marksman.kraftwerk.bias;

import dev.marksman.kraftwerk.SizeParameters;
import dev.marksman.kraftwerk.constraints.*;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Between.between;

public final class DefaultPropertyTestingBiasSettings implements BiasSettings {
    private static DefaultPropertyTestingBiasSettings INSTANCE = new DefaultPropertyTestingBiasSettings();

    private DefaultPropertyTestingBiasSettings() {

    }

    @Override
    public BiasSetting<Integer> intBias(IntRange range) {
        return BiasSetting.builder(range::contains)
                .addSpecialValue(range.min())
                .addSpecialValue(0)
                .addSpecialValue(1)
                .addSpecialValue(-1)
                .addSpecialValue(128)
                .addSpecialValue(-129)
                .addSpecialValue(32768)
                .addSpecialValue(-32769)
                .addSpecialValue(range.max())
                .build();
    }

    @Override
    public BiasSetting<Long> longBias(LongRange range) {
        return BiasSetting.builder(range::contains)
                .addSpecialValue(range.min())
                .addSpecialValue(0L)
                .addSpecialValue(1L)
                .addSpecialValue(-1L)
                .addSpecialValue(Integer.MAX_VALUE + 1L)
                .addSpecialValue(-(Integer.MAX_VALUE + 1L))
                .addSpecialValue(range.max())
                .build();
    }

    @Override
    public BiasSetting<Float> floatBias(FloatRange range) {
        return BiasSetting.builder(range::contains)
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
        return BiasSetting.builder(range::contains)
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
        return BiasSetting.builder(range::contains)
                .addSpecialValue(range.min())
                .addSpecialValue((byte) -1)
                .addSpecialValue((byte) 0)
                .addSpecialValue((byte) 1)
                .addSpecialValue(range.max())
                .build();
    }

    @Override
    public BiasSetting<Short> shortBias(ShortRange range) {
        return BiasSetting.builder(range::contains)
                .addSpecialValue(range.min())
                .addSpecialValue((short) -1)
                .addSpecialValue((short) 0)
                .addSpecialValue((short) 1)
                .addSpecialValue((short) 128)
                .addSpecialValue((short) -129)
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
