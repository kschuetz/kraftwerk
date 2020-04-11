package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.frequency.FrequencyMapBuilder;
import dev.marksman.kraftwerk.weights.NullWeights;

import static dev.marksman.kraftwerk.weights.NullWeights.nonNulls;

class Nulls {

    private static final NullWeights DEFAULT_NULL_WEIGHTS = nonNulls(9).toNulls(1);

    static <A> Generator<A> generateNull() {
        return Generators.constant(null);
    }

    static <A> Generator<A> generateWithNulls(NullWeights weights, Generator<A> g) {
        return FrequencyMapBuilder.<A>frequencyMapBuilder()
                .add(weights.getNonNullWeight(), g)
                .add(weights.getNullWeight(), generateNull())
                .build()
                .toGenerator();
    }

    static <A> Generator<A> generateWithNulls(Generator<A> g) {
        return generateWithNulls(DEFAULT_NULL_WEIGHTS, g);

    }

}
