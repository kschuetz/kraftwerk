package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder0.frequencyMapBuilder0;

public interface FrequencyMapBuilder<A> {
    FrequencyMapBuilder<A> add(int weight, Generator<? extends A> generator);

    FrequencyMapBuilder<A> combine(FrequencyMap<A> other);

    FrequencyMap<A> build();

    default FrequencyMapBuilder<A> add(int weight, A value) {
        return add(weight, constant(value));
    }

    default FrequencyMapBuilder<A> add(Generator<? extends A> generator) {
        return add(1, generator);
    }

    default FrequencyMapBuilder<A> add(A value) {
        return add(1, constant(value));
    }

    default FrequencyMapBuilder<A> add(FrequencyEntry<? extends A> entry) {
        return add(entry.getWeight(), entry.getGenerator());
    }

    static <A> FrequencyMapBuilder<A> frequencyMapBuilder() {
        return frequencyMapBuilder0();
    }
}
