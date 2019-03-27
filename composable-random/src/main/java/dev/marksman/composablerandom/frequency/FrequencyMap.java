package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.frequencyMap1;

public interface FrequencyMap<A> {

    Generator<A> generator();

    FrequencyMap<A> add(int weight, Generator<? extends A> generator);

    default FrequencyMap<A> add(int weight, A value) {
        return add(weight, constant(value));
    }

    default FrequencyMap<A> add(Generator<? extends A> generator) {
        return add(1, generator);
    }

    default FrequencyMap<A> add(A value) {
        return add(1, constant(value));
    }

    static <A> FrequencyMap<A> frequencyMap(int weight1, Generator<A> generator1) {
        return frequencyMap1(weight1, generator1);
    }

    static <A> FrequencyMap<A> frequencyMap(int weight1, A value1) {
        return frequencyMap1(weight1, constant(value1));
    }

}
