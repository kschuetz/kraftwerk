package software.kes.kraftwerk.bias;

import com.jnape.palatable.lambda.functions.Fn1;
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

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

final class CompositeBiasSettings implements BiasSettings {
    private final BiasSettings first;
    private final BiasSettings second;

    private CompositeBiasSettings(BiasSettings first, BiasSettings second) {
        this.first = first;
        this.second = second;
    }

    static BiasSettings compositeBiasSettings(BiasSettings first,
                                              BiasSettings second) {
        return new CompositeBiasSettings(first, second);
    }

    @Override
    public BiasSetting<Integer> intBias(IntRange range) {
        return compose(bs -> bs.intBias(range));
    }

    @Override
    public BiasSetting<Long> longBias(LongRange range) {
        return compose(bs -> bs.longBias(range));
    }

    @Override
    public BiasSetting<Float> floatBias(FloatRange range) {
        return compose(bs -> bs.floatBias(range));
    }

    @Override
    public BiasSetting<Double> doubleBias(DoubleRange range) {
        return compose(bs -> bs.doubleBias(range));
    }

    @Override
    public BiasSetting<Byte> byteBias(ByteRange range) {
        return compose(bs -> bs.byteBias(range));
    }

    @Override
    public BiasSetting<Short> shortBias(ShortRange range) {
        return compose(bs -> bs.shortBias(range));
    }

    @Override
    public BiasSetting<Character> charBias(CharRange range) {
        return compose(bs -> bs.charBias(range));
    }

    @Override
    public BiasSetting<BigInteger> bigIntegerBias(BigIntegerRange range) {
        return compose(bs -> bs.bigIntegerBias(range));
    }

    @Override
    public BiasSetting<BigDecimal> bigDecimalBias(BigDecimalRange range) {
        return compose(bs -> bs.bigDecimalBias(range));
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
}
