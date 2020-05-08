package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Weighted;

final class FrequencyMapBuilderN<A> implements FrequencyMapBuilder<A> {
    private final FrequencyMap<A> result;

    private FrequencyMapBuilderN(FrequencyMap<A> result) {
        this.result = result;
    }

    @Override
    public FrequencyMapBuilder<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() > 0) {
            return frequencyMapBuilderN(result.add(weightedGenerator));
        } else {
            return this;
        }
    }

    @Override
    public FrequencyMapBuilder<A> combine(FrequencyMap<A> other) {
        return frequencyMapBuilderN(result.combine(other));
    }

    @Override
    public FrequencyMap<A> build() {
        return result;
    }

    @Override
    public <B> FrequencyMapBuilder<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMapBuilderN(result.fmap(fn));
    }

    @Override
    public FrequencyMapBuilder<A> multiply(int positiveFactor) {
        return frequencyMapBuilderN(result.multiply(positiveFactor));
    }

    @SuppressWarnings("unchecked")
    static <A> FrequencyMapBuilderN<A> frequencyMapBuilderN(FrequencyMap<? extends A> frequencyMap) {
        return new FrequencyMapBuilderN<>((FrequencyMap<A>) frequencyMap);
    }
}
