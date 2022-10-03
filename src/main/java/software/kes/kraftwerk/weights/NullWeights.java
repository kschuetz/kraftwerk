package software.kes.kraftwerk.weights;

import static software.kes.kraftwerk.weights.BinaryWeights.binaryWeights;

/**
 * A pair of weights that express the frequencies of {@code null}s vs. non-{@code null} values in the context of
 * generating nullable values.
 * <p>
 * Either the {@code null} weight or the non-{@code null} weight can be zero; however, the sum of
 * the weights will always be &gt;= 1.
 */
public final class NullWeights {
    private final BinaryWeights weights;

    private NullWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    /**
     * Partially constructs a {@code NullWeights} with the {@code null} weight.
     *
     * @param weight the weight for {@code null}s; must be &gt;= 0
     * @return a {@link NullWeightsBuilderNonNull}
     */
    public static NullWeightsBuilderNonNull nonNulls(int weight) {
        return new NullWeightsBuilderNonNull(binaryWeights(1, weight));
    }

    /**
     * Partially constructs a {@code NullWeights} with the non-{@code null} weight.
     *
     * @param weight the weight for non-{@code null}s; must be &gt;= 0
     * @return a {@link NullWeightsBuilderNulls}
     */
    public static NullWeightsBuilderNulls nulls(int weight) {
        return new NullWeightsBuilderNulls(binaryWeights(weight, 1));
    }

    /**
     * Creates a {@code NullWeights}.  The sum of the weights for both choices must be &gt;= 1.
     *
     * @param nullWeight    the weight for non-{@code null}s; must be &gt;= 0
     * @param nonNullWeight the weight for {@code null}s; must be &gt;= 0
     * @return a {@code NullWeights}
     */
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

    /**
     * A partially constructed {@link NullWeights}, with the non-{@code null} weight already provided.
     */
    public static final class NullWeightsBuilderNulls {
        BinaryWeights weights;

        private NullWeightsBuilderNulls(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link NullWeights} with the already provided non-{@code null} weight.
         *
         * @param weight the weight for {@code null}s; must be &gt;= 0
         * @return a {@code NullWeights}
         */
        public NullWeights toNonNulls(int weight) {
            return new NullWeights(weights.toB(weight));
        }
    }

    /**
     * A partially constructed {@link NullWeights}, with the {@code null} weight already provided.
     */
    public static final class NullWeightsBuilderNonNull {
        BinaryWeights weights;

        private NullWeightsBuilderNonNull(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link NullWeights} with the already provided {@code null} weight.
         *
         * @param weight the weight for non-{@code null}s; must be &gt;= 0
         * @return a {@code NullWeights}
         */
        public NullWeights toNulls(int weight) {
            return new NullWeights(weights.toA(weight));
        }
    }
}
