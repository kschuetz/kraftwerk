package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class FrequencyMapBuilderN<A> implements FrequencyMapBuilder<A> {
    private final FrequencyMap<A> result;

    @Override
    public FrequencyMapBuilder<A> add(int weight, Generator<? extends A> generator) {
        if (weight > 0) {
            return frequencyMapBuilderN(result.add(weight, generator));
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
    public <B> FrequencyMapBuilder<B> fmap(Function<? super A, ? extends B> fn) {
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
