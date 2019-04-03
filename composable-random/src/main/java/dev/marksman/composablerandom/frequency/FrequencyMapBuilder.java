package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;

import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder0.frequencyMapBuilder0;

public interface FrequencyMapBuilder<A> {
    FrequencyMapBuilder<A> add(int weight, Generator<? extends A> generator);

    FrequencyMapBuilder<A> combine(FrequencyMap<A> other);

    FrequencyMap<A> build();

    static <A> FrequencyMapBuilder<A> frequencyMapBuilder() {
        return frequencyMapBuilder0();
    }
}
