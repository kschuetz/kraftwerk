package software.kes.kraftwerk;

import software.kes.kraftwerk.frequency.FrequencyMap;
import software.kes.kraftwerk.weights.NullWeights;

import static software.kes.kraftwerk.weights.NullWeights.nonNulls;

final class Nulls {
    private static final NullWeights DEFAULT_NULL_WEIGHTS = nonNulls(9).toNulls(1);

    private Nulls() {
    }

    static <A> Generator<A> generateNull() {
        return Generators.constant(null);
    }

    static <A> Generator<A> generateWithNulls(NullWeights weights, Generator<A> g) {
        return FrequencyMap.frequencyMap(g.weighted(weights.getNonNullWeight()))
                .add(Nulls.<A>generateNull().weighted(weights.getNullWeight()))
                .toGenerator();
    }

    static <A> Generator<A> generateWithNulls(Generator<A> g) {
        return generateWithNulls(DEFAULT_NULL_WEIGHTS, g);
    }
}
