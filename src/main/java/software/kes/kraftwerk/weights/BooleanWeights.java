package software.kes.kraftwerk.weights;

import static software.kes.kraftwerk.weights.BinaryWeights.binaryWeights;

/**
 * A pair of weights that express the frequencies of {@code true}s vs. {@code false}s in the context of
 * generating {@code Boolean} values.
 * <p>
 * Either the {@code true} weight or the {@code false} weight can be zero; however, the sum of
 * the weights will always be &gt;= 1.
 */
public final class BooleanWeights {
    private final BinaryWeights weights;

    private BooleanWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    /**
     * Partially constructs a {@code BooleanWeights} with the {@code true} weight.
     *
     * @param weight the weight for {@code true}s; must be &gt;= 0
     * @return a {@link BooleanWeightsBuilderTrues}
     */
    public static BooleanWeightsBuilderTrues trues(int weight) {
        return new BooleanWeightsBuilderTrues(binaryWeights(1, weight));
    }

    /**
     * Partially constructs a {@code BooleanWeights} with the {@code false} weight.
     *
     * @param weight the weight for {@code false}s; must be &gt;= 0
     * @return a {@link BooleanWeightsBuilderFalses}
     */
    public static BooleanWeightsBuilderFalses falses(int weight) {
        return new BooleanWeightsBuilderFalses(binaryWeights(weight, 1));
    }

    /**
     * Creates a {@code BooleanWeights}.  The sum of the weights for both choices must be &gt;= 1.
     *
     * @param falseWeight the weight for {@code false}s; must be &gt;= 0
     * @param trueWeight  the weight for {@code true}s; must be &gt;= 0
     * @return a {@code BooleanWeights}
     */
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

    /**
     * A partially constructed {@link BooleanWeights}, with the {@code false} weight already provided.
     */
    public static final class BooleanWeightsBuilderFalses {
        BinaryWeights weights;

        private BooleanWeightsBuilderFalses(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link BooleanWeights} with the already provided {@code false} weight.
         *
         * @param weight the weight for {@code true}s; must be &gt;= 0
         * @return a {@code BooleanWeights}
         */
        public BooleanWeights toTrues(int weight) {
            return new BooleanWeights(weights.toB(weight));
        }
    }

    /**
     * A partially constructed {@link BooleanWeights}, with the {@code true} weight already provided.
     */
    public static final class BooleanWeightsBuilderTrues {
        BinaryWeights weights;

        private BooleanWeightsBuilderTrues(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link BooleanWeights} with the already provided {@code true} weight.
         *
         * @param weight the weight for {@code false}s; must be &gt;= 0
         * @return a {@code BooleanWeights}
         */
        public BooleanWeights toFalses(int weight) {
            return new BooleanWeights(weights.toA(weight));
        }
    }
}
