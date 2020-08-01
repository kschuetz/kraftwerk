package dev.marksman.kraftwerk.weights;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

public final class EitherWeights {
    private final BinaryWeights weights;

    private EitherWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    public static EitherWeightsBuilderLefts lefts(int weight) {
        return new EitherWeightsBuilderLefts(binaryWeights(1, weight));
    }

    public static EitherWeightsBuilderRights rights(int weight) {
        return new EitherWeightsBuilderRights(binaryWeights(weight, 1));
    }

    public static EitherWeights eitherWeights(int falseWeight, int trueWeight) {
        return new EitherWeights(binaryWeights(falseWeight, trueWeight));
    }

    public int getLeftWeight() {
        return weights.getWeightA();
    }

    public int getRightWeight() {
        return weights.getWeightB();
    }

    public BinaryWeights getWeights() {
        return this.weights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EitherWeights that = (EitherWeights) o;

        return weights.equals(that.weights);
    }

    @Override
    public int hashCode() {
        return weights.hashCode();
    }

    @Override
    public String toString() {
        return "EitherWeights{" +
                "weights=" + weights +
                '}';
    }

    public static final class EitherWeightsBuilderLefts {
        BinaryWeights weights;

        private EitherWeightsBuilderLefts(BinaryWeights weights) {
            this.weights = weights;
        }

        public EitherWeights toRights(int weight) {
            return new EitherWeights(weights.toB(weight));
        }
    }

    public static final class EitherWeightsBuilderRights {
        BinaryWeights weights;

        private EitherWeightsBuilderRights(BinaryWeights weights) {
            this.weights = weights;
        }

        public EitherWeights toLefts(int weight) {
            return new EitherWeights(weights.toA(weight));
        }
    }

}
