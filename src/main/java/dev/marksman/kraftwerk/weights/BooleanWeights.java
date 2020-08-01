package dev.marksman.kraftwerk.weights;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

public final class BooleanWeights {
    private final BinaryWeights weights;

    private BooleanWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    public static BooleanWeightsBuilderTrues trues(int weight) {
        return new BooleanWeightsBuilderTrues(binaryWeights(1, weight));
    }

    public static BooleanWeightsBuilderFalses falses(int weight) {
        return new BooleanWeightsBuilderFalses(binaryWeights(weight, 1));
    }

    public static BooleanWeights booleanWeights(int falseWeight, int trueWeight) {
        return new BooleanWeights(binaryWeights(falseWeight, trueWeight));
    }

    public int getFalseWeight() {
        return weights.getWeightA();
    }

    public int getTrueWeight() {
        return weights.getWeightB();
    }

    public BinaryWeights getWeights() {
        return this.weights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooleanWeights that = (BooleanWeights) o;

        return weights.equals(that.weights);
    }

    @Override
    public int hashCode() {
        return weights.hashCode();
    }

    @Override
    public String toString() {
        return "BooleanWeights{" +
                "weights=" + weights +
                '}';
    }

    public static final class BooleanWeightsBuilderFalses {
        BinaryWeights weights;

        private BooleanWeightsBuilderFalses(BinaryWeights weights) {
            this.weights = weights;
        }

        public BooleanWeights toTrues(int weight) {
            return new BooleanWeights(weights.toB(weight));
        }
    }

    public static final class BooleanWeightsBuilderTrues {
        BinaryWeights weights;

        private BooleanWeightsBuilderTrues(BinaryWeights weights) {
            this.weights = weights;
        }

        public BooleanWeights toFalses(int weight) {
            return new BooleanWeights(weights.toA(weight));
        }
    }
}
