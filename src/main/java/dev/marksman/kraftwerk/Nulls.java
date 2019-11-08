package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.frequency.FrequencyMapBuilder;

import static dev.marksman.kraftwerk.NullWeights.nonNullWeight;

class Nulls {

    private static final NullWeights DEFAULT_NULL_WEIGHTS = nonNullWeight(9).toNull(1);

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
