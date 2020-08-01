package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static dev.marksman.kraftwerk.frequency.FrequencyMapBuilderN.frequencyMapBuilderN;

final class FrequencyMapBuilder0<A> implements FrequencyMapBuilder<A> {
    private FrequencyMapBuilder0() {
    }

    static <A> FrequencyMapBuilder0<A> frequencyMapBuilder0() {
        return new FrequencyMapBuilder0<>();
    }

    @Override
    public FrequencyMapBuilder<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        if (weightedGenerator.getWeight() > 0) {
            FrequencyMap<? extends A> frequencyMap = frequencyMap(weightedGenerator);
            return frequencyMapBuilderN(frequencyMap);
        } else {
            return this;
        }
    }

    @Override
    public FrequencyMapBuilder<A> combine(FrequencyMap<A> other) {
        return frequencyMapBuilderN(other);
    }

    @Override
    public <B> FrequencyMapBuilder0<B> fmap(Fn1<? super A, ? extends B> fn) {
        return frequencyMapBuilder0();
    }

    @Override
    public FrequencyMapBuilder<A> multiply(int positiveFactor) {
        return this;
    }

    @Override
    public FrequencyMap<A> build() {
        throw new IllegalStateException("Cannot build FrequencyMap: no positive weights provided");
    }
}
