package dev.marksman.composablerandom;

import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import static dev.marksman.composablerandom.NullWeights.nonNullWeight;

class Nulls {

    private static final NullWeights DEFAULT_NULL_WEIGHTS = nonNullWeight(9).toNull(1);

    static <A> Generate<A> generateNull() {
        return Generate.constant(null);
    }

    static <A> Generate<A> generateWithNulls(NullWeights weights, Generate<A> g) {
        return FrequencyMapBuilder.<A>frequencyMapBuilder()
                .add(weights.getNonNullWeight(), g)
                .add(weights.getNullWeight(), generateNull())
                .build()
                .toGenerate();
    }

    static <A> Generate<A> generateWithNulls(Generate<A> g) {
        return generateWithNulls(DEFAULT_NULL_WEIGHTS, g);

    }

}
