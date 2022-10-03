package software.kes.kraftwerk.frequency;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Generators;
import software.kes.kraftwerk.ToGenerator;
import software.kes.kraftwerk.Weighted;

import static software.kes.kraftwerk.Generators.constant;
import static software.kes.kraftwerk.frequency.IncompleteFrequencyMap.incompleteFrequencyMap;

/**
 * A strategy for randomly choosing between one or more {@link Generator}s (or values) of a specific type, with a custom set of probabilities.
 * Before it can be converted to a {@code Generator}, a {@code FrequencyMap} must contain at least one value with a weight greater than zero.
 * <p>
 * To build a {@code Generator} from a {@code FrequencyMap}, call {@link FrequencyMap#toGenerator()}.  A {@code FrequencyMap}
 * can always continue to be used and added to, even after calling {@code toGenerator}.
 * <p>
 * {@code FrequencyMap}s are immutable.  Any method that appears to mutate a {@code FrequencyMap} actually
 * creates a new {@code FrequencyMap}, while leaving the original unchanged.
 *
 * @param <A> the output type
 */
public interface FrequencyMap<A> extends ToGenerator<A>, Functor<A, FrequencyMap<?>> {
    /**
     * Creates an empty {@code FrequencyMap}.
     *
     * @param <A> the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMap() {
        return incompleteFrequencyMap();
    }

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param weightedValue the first weighted value to add to the {@code FrequencyMap}
     * @param <A>           the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMapFirstValue(Weighted<? extends A> weightedValue) {
        return FrequencyMap.<A>frequencyMap().add(weightedValue.fmap(Generators::constant));
    }

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param value the first value to add to the {@code FrequencyMap}; will have a weight of 1
     * @param <A>   the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMapFirstValue(A value) {
        return FrequencyMap.<A>frequencyMap().add(constant(value).weighted());
    }

    /**
     * Creates a {@code FrequencyMap}.
     *
     * @param gen1 the first {@link Generator} to add to the {@code FrequencyMap}; will have a weight of 1
     * @param <A>  the output type
     * @return a {@code FrequencyMap<A>}
     */
    static <A> FrequencyMap<A> frequencyMap(Generator<? extends A> gen1) {
        return FrequencyMap.<A>frequencyMap().add(gen1.weighted());
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
        return FrequencyMap.<A>frequencyMap().add(weightedGenerator);
    }

    /**
     * Returns true if this {@code FrequencyMap} contains at least one element with weight greater than zero.
     */
    boolean isEmpty();

    /**
     * Builds a {@link Generator} from this {@code FrequencyMap}, if it is not empty.
     * If this {@code FrequencyMap} is empty (i.e., it contains no weights greater than zero), this method will throw an
     * {@link IllegalStateException}.
     * <p>
     * This {@code FrequencyMap<A>} can continue to be used after calling this method.
     *
     * @return a {@code Generator<A>}
     * @see FrequencyMap#isEmpty
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
