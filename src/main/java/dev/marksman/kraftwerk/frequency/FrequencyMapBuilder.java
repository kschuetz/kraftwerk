package dev.marksman.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.Weighted;

import static dev.marksman.kraftwerk.frequency.FrequencyMapBuilder0.frequencyMapBuilder0;

/**
 * A builder to facilitate the construction of {@link FrequencyMap}s.
 * <p>
 * Use one of the {@link FrequencyMapBuilder#add} methods to add more choices, and call {@link FrequencyMapBuilder#build} to
 * build the final {@code FrequencyMap}.
 * <p>
 * All instances of {@code FrequencyMapBuilder} are immutable and can be reused, even after calling {@code build}.
 *
 * @param <A> the output type
 */
public interface FrequencyMapBuilder<A> {
    /**
     * Creates a {@code FrequencyMapBuilder}.
     *
     * @param <A> the output type
     * @return a {@code FrequencyMapBuilder<A>}
     */
    static <A> FrequencyMapBuilder<A> frequencyMapBuilder() {
        return frequencyMapBuilder0();
    }

    /**
     * Creates a new {@code FrequencyMapBuilder} that is the same as this one, with a weighted {@link Generator} added.
     *
     * @param weightedGenerator a weighted {@code Generator}
     * @return a {@code FrequencyMapBuilder<A>}
     */
    FrequencyMapBuilder<A> add(Weighted<? extends Generator<? extends A>> weightedGenerator);

    /**
     * Creates a new {@code FrequencyMapBuilder} that is the combination of this one and another one.
     * <p>
     * Both this and the other {@code FrequencyMapBuilder} can continue to be used after calling this method.
     *
     * @param other the other {@code FrequencyMapBuilder}
     * @return a {@code FrequencyMapBuilder}
     */
    FrequencyMapBuilder<A> combine(FrequencyMap<A> other);

    /**
     * Creates a {@link FrequencyMap} from the elements added so far.
     * <p>
     * This {@code FrequencyMapBuilder} can continue to be used after calling this method.
     *
     * @return a {@code FrequencyMap<A>}
     */
    FrequencyMap<A> build();

    <B> FrequencyMapBuilder<B> fmap(Fn1<? super A, ? extends B> fn);

    /**
     * Creates a new {@code FrequencyMapBuilder} that is the same as this one, with its existing weights multiplied by {@code positiveFactor}.
     *
     * @param positiveFactor number to multiply by;  must be &gt;= 1.
     * @return a {@code FrequencyMapBuilder<A>}
     * @throws IllegalArgumentException if positiveFactor is &lt; 1.
     */
    FrequencyMapBuilder<A> multiply(int positiveFactor);

    /**
     * Adds a {@link Generator} with a weight of 1.
     *
     * @param gen a {@code Generator}
     * @return a {@code FrequencyMapBuilder<A>}
     */
    default FrequencyMapBuilder<A> add(Generator<? extends A> gen) {
        return add(gen.weighted(1));
    }

    /**
     * Creates a new {@code FrequencyMapBuilder} that is the same as this one, with a weighted value added.
     *
     * @param weightedValue the weighted value
     * @return a {@code FrequencyMapBuilder<A>}
     */
    default FrequencyMapBuilder<A> addValue(Weighted<? extends A> weightedValue) {
        return add(weightedValue.fmap(Generators::constant));
    }

    /**
     * Creates a new {@code FrequencyMapBuilder} that is the same as this one, with a value added.
     * The added value will have a weight of 1.
     *
     * @param value the value
     * @return a {@code FrequencyMapBuilder<A>}
     */
    default FrequencyMapBuilder<A> addValue(A value) {
        return addValue(Weighted.weighted(value));
    }
}
