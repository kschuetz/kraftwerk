package software.kes.kraftwerk.weights;

import static software.kes.kraftwerk.weights.BinaryWeights.binaryWeights;

/**
 * A pair of weights that express the frequencies of {@code just}s vs. {@code nothing}s in the context of
 * generating {@link com.jnape.palatable.lambda.adt.Maybe} values.
 * <p>
 * Either the {@code just} weight or the {@code nothing} weight can be zero; however, the sum of
 * the weights will always be &gt;= 1.
 */
public final class MaybeWeights {
    private final BinaryWeights weights;

    private MaybeWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    /**
     * Partially constructs a {@code MaybeWeights} with the {@code just} weight.
     *
     * @param weight the weight for {@code just}s; must be &gt;= 0
     * @return a {@link MaybeWeightsBuilderJusts}
     */
    public static MaybeWeightsBuilderJusts justs(int weight) {
        return new MaybeWeightsBuilderJusts(binaryWeights(1, weight));
    }

    /**
     * Partially constructs a {@code MaybeWeights} with the {@code nothing} weight.
     *
     * @param weight the weight for {@code nothing}s; must be &gt;= 0
     * @return a {@link MaybeWeightsBuilderNothings}
     */
    public static MaybeWeightsBuilderNothings nothings(int weight) {
        return new MaybeWeightsBuilderNothings(binaryWeights(weight, 1));
    }

    /**
     * Creates a {@code MaybeWeights}.  The sum of the weights for both choices must be &gt;= 1.
     *
     * @param nothingWeight the weight for {@code nothing}s; must be &gt;= 0
     * @param justWeight    the weight for {@code just}s; must be &gt;= 0
     * @return a {@code MaybeWeights}
     */
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

    /**
     * A partially constructed {@link MaybeWeights}, with the {@code nothing} weight already provided.
     */
    public static final class MaybeWeightsBuilderNothings {
        BinaryWeights weights;

        private MaybeWeightsBuilderNothings(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link MaybeWeights} with the already provided {@code nothing} weight.
         *
         * @param weight the weight for {@code just}s; must be &gt;= 0
         * @return a {@code MaybeWeights}
         */
        public MaybeWeights toJusts(int weight) {
            return new MaybeWeights(weights.toB(weight));
        }
    }

    /**
     * A partially constructed {@link MaybeWeights}, with the {@code just} weight already provided.
     */
    public static final class MaybeWeightsBuilderJusts {
        BinaryWeights weights;

        private MaybeWeightsBuilderJusts(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link MaybeWeights} with the already provided {@code just} weight.
         *
         * @param weight the weight for {@code nothing}s; must be &gt;= 0
         * @return a {@code MaybeWeights}
         */
        public MaybeWeights toNothings(int weight) {
            return new MaybeWeights(weights.toA(weight));
        }
    }
}
