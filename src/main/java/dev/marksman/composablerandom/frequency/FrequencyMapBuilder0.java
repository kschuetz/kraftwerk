package dev.marksman.composablerandom.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilderN.frequencyMapBuilderN;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMapBuilder0<A> implements FrequencyMapBuilder<A> {
    @Override
    public FrequencyMapBuilder<A> add(int weight, Generator<? extends A> generator) {
        if (weight > 0) {
            FrequencyMap<? extends A> frequencyMap = frequencyMap(weight, generator);
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

    static <A> FrequencyMapBuilder0<A> frequencyMapBuilder0() {
        return new FrequencyMapBuilder0<>();
    }
}
