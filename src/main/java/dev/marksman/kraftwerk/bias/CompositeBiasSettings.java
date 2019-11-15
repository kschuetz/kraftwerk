package dev.marksman.kraftwerk.bias;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.SizeParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CompositeBiasSettings implements BiasSettings {
    private final BiasSettings first;
    private final BiasSettings second;

    @Override
    public BiasSetting<Integer> intBias(int min, int max) {
        return compose(bs -> bs.intBias(min, max));
    }

    @Override
    public BiasSetting<Long> longBias(long min, long max) {
        return compose(bs -> bs.longBias(min, max));
    }

    @Override
    public BiasSetting<Float> floatBias(float min, float max) {
        return compose(bs -> bs.floatBias(min, max));
    }

    @Override
    public BiasSetting<Double> doubleBias(double min, double max) {
        return compose(bs -> bs.doubleBias(min, max));
    }

    @Override
    public BiasSetting<Byte> byteBias(byte min, byte max) {
        return compose(bs -> bs.byteBias(min, max));
    }

    @Override
    public BiasSetting<Short> shortBias(short min, short max) {
        return compose(bs -> bs.shortBias(min, max));
    }

    @Override
    public BiasSetting<Integer> sizeBias(SizeParameters sizeParameters) {
        return compose(bs -> bs.sizeBias(sizeParameters));
    }

    private <A> BiasSetting<A> compose(Fn1<BiasSettings, BiasSetting<A>> f) {
        return f.apply(first)
                .match(__ -> f.apply(second),
                        id());
    }

    static BiasSettings compositeBiasSettings(BiasSettings first,
                                              BiasSettings second) {
        return new CompositeBiasSettings(first, second);
    }
}
