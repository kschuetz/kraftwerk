package dev.marksman.composablerandom.frequency;

import dev.marksman.composablerandom.Generator;

import java.util.function.Function;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.frequency.FrequencyMap1.frequencyMap1;

public interface FrequencyMap<A> {

    Generator<A> generator();

    FrequencyMap<A> add(int weight, Generator<? extends A> generator);

    FrequencyMap<A> combine(FrequencyMap<A> other);

    <B> FrequencyMap<B> fmap(Function<? super A, ? extends B> fn);

    /**
     * Multiplies existing weights by `positiveFactor`.  Useful for combining with
     * other `FrequencyMap`s.
     *
     * @param positiveFactor number to multiply by.  Must be &gt;= 1.
     * @return a new `FrequencyMap` containing the same entries with the weights multiplied.
     * @throws IllegalArgumentException if positiveFactor is &lt; 1.
     */
    FrequencyMap<A> multiply(int positiveFactor);

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
