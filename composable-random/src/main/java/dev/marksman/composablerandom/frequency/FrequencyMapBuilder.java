package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;

import java.util.function.Function;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder0.frequencyMapBuilder0;

public interface FrequencyMapBuilder<A> {
    FrequencyMapBuilder<A> add(int weight, Generator<? extends A> generator);

    FrequencyMapBuilder<A> combine(FrequencyMap<A> other);

    FrequencyMap<A> build();

    <B> FrequencyMapBuilder<B> fmap(Function<? super A, ? extends B> fn);

    FrequencyMapBuilder<A> multiply(int positiveFactor);

    default FrequencyMapBuilder<A> add(Generator<? extends A> generator) {
        return add(1, generator);
    }

    default FrequencyMapBuilder<A> addValue(int weight, A value) {
        return add(weight, constant(value));
    }

    default FrequencyMapBuilder<A> addValue(A value) {
        return add(1, constant(value));
    }

    default FrequencyMapBuilder<A> add(FrequencyEntry<A> entry) {
        return add(entry.getWeight(), entry.getGenerator());
    }

    static <A> FrequencyMapBuilder<A> frequencyMapBuilder() {
        return frequencyMapBuilder0();
    }
}
