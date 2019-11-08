package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.FrequencyEntry;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.ToGenerator;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.frequency.FrequencyMap1.frequencyMap1;

public interface FrequencyMap<A> extends ToGenerator<A> {

    Generator<A> toGenerator();

    FrequencyMap<A> add(int weight, Generator<? extends A> gen);

    FrequencyMap<A> combine(FrequencyMap<A> other);

    <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn);

    /**
     * Multiplies existing weights by `positiveFactor`.  Useful for combining with
     * other `FrequencyMap`s.
     *
     * @param positiveFactor number to multiply by.  Must be &gt;= 1.
     * @return a new `FrequencyMap` containing the same entries with the weights multiplied.
     * @throws IllegalArgumentException if positiveFactor is &lt; 1.
     */
    FrequencyMap<A> multiply(int positiveFactor);

    default FrequencyMap<A> add(Generator<? extends A> gen) {
        return add(1, gen);
    }

    default FrequencyMap<A> add(FrequencyEntry<A> entry) {
        return add(entry._1(), entry._2());
    }

    default FrequencyMap<A> addValue(A value) {
        return add(1, constant(value));
    }

    default FrequencyMap<A> addValue(int weight, A value) {
        return add(weight, constant(value));
    }

    static <A> FrequencyMap<A> frequencyMap(int weight1, Generator<A> gen1) {
        return frequencyMap1(weight1, gen1);
    }

    static <A> FrequencyMap<A> frequencyMap(int weight1, A value1) {
        return frequencyMap1(weight1, constant(value1));
    }

    static <A> FrequencyMap<A> frequencyMap(Generator<A> gen1) {
        return frequencyMap1(1, gen1);
    }

    static <A> FrequencyMap<A> frequencyMap(FrequencyEntry<A> entry) {
        return frequencyMap1(entry._1(), entry._2());
    }

}
