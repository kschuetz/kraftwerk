package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.core.StandardSeed;

import java.util.Random;

/**
 * An immutable seed for a random value generator.
 */
public interface Seed {
    /**
     * Gets the internal state value of this {@code Seed}.
     *
     * @return a {@code long}.
     */
    long getSeedValue();

    /**
     * Returns a new {@code Seed} that is computed by perturbing the inner state of this {@code Seed} with a given value.
     *
     * @param value any {@code long} value
     * @return a {@code Seed}
     */
    Seed perturb(long value);

    /**
     * Creates a {@code Seed} of the same underlying implementation, with the provided value for the internal state.
     *
     * @param value the value for the internal state of the new {@code Seed}
     * @return a {@code Seed}
     */
    Seed setNextSeedValue(long value);

    /**
     * Instantiates a {@code Seed}.
     *
     * @param value the value used to initialize the inner state of the {@code Seed}.
     *              Note that this value will be not necessarily (nor likely) be equal to the internal state
     *              of the instantiated {@code Seed}.  However, the same value will always result in the same
     *              internal state.
     * @return a {@code Seed}
     */
    static Seed create(long value) {
        return StandardSeed.initStandardSeed(value);
    }

    /**
     * Randomly generates a {@code Seed}.
     *
     * @return a {@code Seed}
     */
    static Seed random() {
        return create(new Random().nextLong());
    }
}
