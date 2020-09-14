package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.ToGenerator;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.frequency.FrequencyMap1.frequencyMap1;

/**
 * A strategy for randomly choosing between one or more {@link Generator}s (or values) of a specific type, with a custom set of probabilities.
 * A {@code FrequencyMap} must contain at least one value, and the sum of the weights of all values must be &gt; = 1.
 * <p>
 * To build a {@code Generator} from a {@code FrequencyMap}, call {@link FrequencyMap#toGenerator()}.  A {@code FrequencyMap}
 * can always continue to be used and added to, even after calling {@code toGenerator}.
 * <p>
 * {@code FrequencyMap}s are immutable.  Any method that appears to mutate a {@code FrequencyMap} actually
 * creates a new {@code FrequencyMap}, while leaving the original unchanged.
 *
 * @param <A> the output type
 */
public interface FrequencyMap<A> extends ToGenerator<A> {

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param weightedValue the first weighted value to add to the {@code FrequencyMap}
     * @param <A>           the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMapValue(Weighted<? extends A> weightedValue) {
        return frequencyMap1(weightedValue.fmap(Generators::constant));
    }

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param value the first value to add to the {@code FrequencyMap}; will have a weight of 1
     * @param <A>   the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMapValue(A value) {
        return frequencyMap1(constant(value).weighted());
    }

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param gen1 the first {@link Generator} to add to the {@code FrequencyMap}; will have a weight of 1
     * @param <A>  the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMap(Generator<? extends A> gen1) {
        return frequencyMap1(gen1.weighted());
    }

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param weightedGenerator the first weighted {@link Generator} to add to the {@code FrequencyMap}.
     *                          Its weight must be &gt;= 1.
     * @param <A>               the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMap(Weighted<? extends Generator<? extends A>> weightedGenerator) {
        return frequencyMap1(weightedGenerator);
    }

    /**
     * Builds a {@link Generator} from this {@code FrequencyMap}.
     * <p>
     * This {@code FrequencyMap<A>} can continue to be used after call this method.
     *
     * @return a {@code Generator<A>}
     */
    Generator<A> toGenerator();

    /**
     * Creates a new {@code FrequencyMap} that is the same as this one, with a weighted {@link Generator} added.
     *
     * @param weightedGenerator a weighted {@code Generator}
     * @return a {@code FrequencyMapBuilder<A>}
     */
    FrequencyMap<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator);

    /**
     * Creates a new {@code FrequencyMap} that is the combination of this one and another one.
     * <p>
     * Both this and the other {@code FrequencyMap} can continue to be used after calling this method.
     *
     * @param other the other {@code FrequencyMap}
     * @return a {@code FrequencyMap}
     */
    FrequencyMap<A> combine(FrequencyMap<A> other);

    <B> FrequencyMap<B> fmap(Fn1<? super A, ? extends B> fn);

    /**
     * Creates a new {@code FrequencyMap} that is the same as this one, with its existing weights multiplied by {@code positiveFactor}.
     * Useful for combining with other {@code FrequencyMap}s.
     *
     * @param positiveFactor number to multiply by;  must be &gt;= 1.
     * @return a new {@code FrequencyMap} containing the same entries with the weights multiplied
     * @throws IllegalArgumentException if positiveFactor is &lt; 1.
     */
    FrequencyMap<A> multiply(int positiveFactor);

    /**
     * Creates a new {@code FrequencyMap} that is the same as this one, with a {@link Generator} added.
     * The added {@code Generator} will have a weight of 1.
     *
     * @param gen a {@code Generator}
     * @return a {@code FrequencyMap<A>}
     */
    default FrequencyMap<A> add(Generator<? extends A> gen) {
        return add(gen.weighted());
    }

    /**
     * Creates a new {@code FrequencyMap} that is the same as this one, with a value added.
     * The added value will have a weight of 1.
     *
     * @param value the value
     * @return a {@code FrequencyMap<A>}
     */
    default FrequencyMap<A> addValue(A value) {
        return add(constant(value).weighted());
    }

    /**
     * Creates a new {@code FrequencyMap} that is the same as this one, with a weighted value added.
     *
     * @param weightedValue the weighted value
     * @return a {@code FrequencyMap<A>}
     */
    default FrequencyMap<A> addValue(Weighted<? extends A> weightedValue) {
        return add(weightedValue.fmap(Generators::constant));
    }

}
