package dev.marksman.kraftwerk.weights;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

public final class MaybeWeights {
    private final BinaryWeights weights;

    private MaybeWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    public static MaybeWeightsBuilderJusts justs(int weight) {
        return new MaybeWeightsBuilderJusts(binaryWeights(1, weight));
    }

    public static MaybeWeightsBuilderNothings nothings(int weight) {
        return new MaybeWeightsBuilderNothings(binaryWeights(weight, 1));
    }

    public static MaybeWeights maybeWeights(int nothingWeight, int justWeight) {
        return new MaybeWeights(binaryWeights(nothingWeight, justWeight));
    }

    public int getNothingWeight() {
        return weights.getWeightA();
    }

    public int getJustWeight() {
        return weights.getWeightB();
    }

    public BinaryWeights getWeights() {
        return this.weights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaybeWeights that = (MaybeWeights) o;

        return weights.equals(that.weights);
    }

    @Override
    public int hashCode() {
        return weights.hashCode();
    }

    @Override
    public String toString() {
        return "MaybeWeights{" +
                "weights=" + weights +
                '}';
    }

    public static final class MaybeWeightsBuilderNothings {
        BinaryWeights weights;

        private MaybeWeightsBuilderNothings(BinaryWeights weights) {
            this.weights = weights;
        }

        public MaybeWeights toJusts(int weight) {
            return new MaybeWeights(weights.toB(weight));
        }
    }

    public static final class MaybeWeightsBuilderJusts {
        BinaryWeights weights;

        private MaybeWeightsBuilderJusts(BinaryWeights weights) {
            this.weights = weights;
        }

        public MaybeWeights toNothings(int weight) {
            return new MaybeWeights(weights.toA(weight));
        }
    }

}
