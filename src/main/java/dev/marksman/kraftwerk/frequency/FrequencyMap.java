package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.frequency.FrequencyMap1.frequencyMap1;

public interface FrequencyMap<A> extends ToGenerator<A> {

    Generator<A> toGenerator();

    FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator);

    FrequencyMap<A> combine(FrequencyMap<A> other);

    <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn);

    /**
     * Multiplies existing weights by {@code positiveFactor}.  Useful for combining with
     * other {@code FrequencyMap}s.
     *
     * @param positiveFactor number to multiply by;  must be &gt;= 1.
     * @return a new {@code positiveFactor} containing the same entries with the weights multiplied
     * @throws IllegalArgumentException if positiveFactor is &lt; 1.
     */
    FrequencyMap<A> multiply(int positiveFactor);

    default FrequencyMap<A> add(Generator<? extends A> gen) {
        return add(gen.weighted());
    }

    default FrequencyMap<A> addValue(A value) {
        return add(constant(value).weighted());
    }

    default FrequencyMap<A> addValue(Weighted<? extends A> weightedValue) {
        return add(weightedValue.fmap(Generators::constant));
    }

    static <A> FrequencyMap<A> frequencyMapValue(Weighted<? extends A> weightedValue) {
        return frequencyMap1(weightedValue.fmap(Generators::constant));
    }

    static <A> FrequencyMap<A> frequencyMapValue(A value) {
        return frequencyMap1(constant(value).weighted());
    }

    static <A> FrequencyMap<A> frequencyMap(Generator<? extends A> gen1) {
        return frequencyMap1(gen1.weighted());
    }

    static <A> FrequencyMap<A> frequencyMap(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        return frequencyMap1(weightedGenerator);
    }

}
