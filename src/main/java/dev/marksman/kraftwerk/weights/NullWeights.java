package dev.marksman.kraftwerk.weights;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

public final class NullWeights {
    private final BinaryWeights weights;

    private NullWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    public static NullWeightsBuilderNonNull nonNulls(int weight) {
        return new NullWeightsBuilderNonNull(binaryWeights(1, weight));
    }

    public static NullWeightsBuilderNulls nulls(int weight) {
        return new NullWeightsBuilderNulls(binaryWeights(weight, 1));
    }

    public static NullWeights nullWeights(int nullWeight, int nonNullWeight) {
        return new NullWeights(binaryWeights(nullWeight, nonNullWeight));
    }

    public int getNullWeight() {
        return weights.getWeightA();
    }

    public int getNonNullWeight() {
        return weights.getWeightB();
    }

    public BinaryWeights getWeights() {
        return this.weights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NullWeights that = (NullWeights) o;

        return weights.equals(that.weights);
    }

    @Override
    public int hashCode() {
        return weights.hashCode();
    }

    @Override
    public String toString() {
        return "NullWeights{" +
                "weights=" + weights +
                '}';
    }

    public static final class NullWeightsBuilderNulls {
        BinaryWeights weights;

        private NullWeightsBuilderNulls(BinaryWeights weights) {
            this.weights = weights;
        }

        public NullWeights toNonNulls(int weight) {
            return new NullWeights(weights.toB(weight));
        }
    }

    public static final class NullWeightsBuilderNonNull {
        BinaryWeights weights;

        private NullWeightsBuilderNonNull(BinaryWeights weights) {
            this.weights = weights;
        }

        public NullWeights toNulls(int weight) {
            return new NullWeights(weights.toA(weight));
        }
    }
}
