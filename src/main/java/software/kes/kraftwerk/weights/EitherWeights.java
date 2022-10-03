package software.kes.kraftwerk.weights;

import static software.kes.kraftwerk.weights.BinaryWeights.binaryWeights;

/**
 * A pair of weights that express the frequencies of {@code left}s vs. {@code right}s in the context of
 * generating {@link com.jnape.palatable.lambda.adt.Either} values.
 * <p>
 * Either the {@code left} weight or the {@code right} weight can be zero; however, the sum of
 * the weights will always be &gt;= 1.
 */
public final class EitherWeights {
    private final BinaryWeights weights;

    private EitherWeights(BinaryWeights weights) {
        this.weights = weights;
    }

    /**
     * Partially constructs a {@code EitherWeights} with the {@code left} weight.
     *
     * @param weight the weight for {@code left}s; must be &gt;= 0
     * @return a {@link EitherWeightsBuilderLefts}
     */
    public static EitherWeightsBuilderLefts lefts(int weight) {
        return new EitherWeightsBuilderLefts(binaryWeights(1, weight));
    }

    /**
     * Partially constructs a {@code EitherWeights} with the {@code right} weight.
     *
     * @param weight the weight for {@code right}s; must be &gt;= 0
     * @return a {@link EitherWeightsBuilderRights}
     */
    public static EitherWeightsBuilderRights rights(int weight) {
        return new EitherWeightsBuilderRights(binaryWeights(weight, 1));
    }

    /**
     * Creates a {@code EitherWeights}.  The sum of the weights for both choices must be &gt;= 1.
     *
     * @param leftWeight  the weight for {@code left}s; must be &gt;= 0
     * @param rightWeight the weight for {@code right}s; must be &gt;= 0
     * @return a {@code EitherWeights}
     */
    public static EitherWeights eitherWeights(int leftWeight, int rightWeight) {
        return new EitherWeights(binaryWeights(leftWeight, rightWeight));
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

    /**
     * A partially constructed {@link EitherWeights}, with the {@code left} weight already provided.
     */
    public static final class EitherWeightsBuilderLefts {
        BinaryWeights weights;

        private EitherWeightsBuilderLefts(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link EitherWeights} with the already provided {@code left} weight.
         *
         * @param weight the weight for {@code right}s; must be &gt;= 0
         * @return a {@code EitherWeights}
         */
        public EitherWeights toRights(int weight) {
            return new EitherWeights(weights.toB(weight));
        }
    }

    /**
     * A partially constructed {@link EitherWeights}, with the {@code right} weight already provided.
     */
    public static final class EitherWeightsBuilderRights {
        BinaryWeights weights;

        private EitherWeightsBuilderRights(BinaryWeights weights) {
            this.weights = weights;
        }

        /**
         * Creates a {@link EitherWeights} with the already provided {@code right} weight.
         *
         * @param weight the weight for {@code left}s; must be &gt;= 0
         * @return a {@code EitherWeights}
         */
        public EitherWeights toLefts(int weight) {
            return new EitherWeights(weights.toA(weight));
        }
    }
}
