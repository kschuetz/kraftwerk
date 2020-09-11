package dev.marksman.kraftwerk.weights;

/**
 * A pair of weights that express the frequencies of two possible outcomes in the context of generating values.
 * <p>
 * Either the A weight or the B weight can be zero; however, the sum of
 * the weights will always be &gt;= 1.
 */
public final class BinaryWeights {
    private final int weightA;
    private final int weightB;

    private BinaryWeights(int weightA, int weightB) {
        this.weightA = weightA;
        this.weightB = weightB;
    }

    /**
     * Creates a {@code BinaryWeights} with a specific weight for choice A, and a weight of 1 for choice B.
     *
     * @param weight the weight for choice A; must be &gt;= 0
     * @return a {@code BinaryWeights}
     */
    public static BinaryWeights weightA(int weight) {
        return new BinaryWeights(weight, 1);
    }

    /**
     * Creates a {@code BinaryWeights} with a specific weight for choice B, and a weight of 1 for choice A.
     *
     * @param weight the weight for choice B; must be &gt;= 0
     * @return a {@code BinaryWeights}
     */
    public static BinaryWeights weightB(int weight) {
        return new BinaryWeights(1, weight);
    }

    /**
     * Creates a {@code BinaryWeights}.  The sum of the weights for both choices must be &gt;= 1.
     *
     * @param weightA the weight for choice A; must be &gt;= 0
     * @param weightB the weight for choice B; must be &gt;= 0
     * @return a {@code BinaryWeights}
     */
    public static BinaryWeights binaryWeights(int weightA, int weightB) {
        requireNonNegative(weightA);
        requireNonNegative(weightB);
        if (weightA + weightB < 1) {
            throw new IllegalArgumentException("sum of weights must be >= 1");
        }
        return new BinaryWeights(weightA, weightB);
    }

    private static void requireNonNegative(int weight) {
        if (weight < 0) throw new IllegalArgumentException("weight must be >= 0");
    }

    /**
     * Creates a new {@code BinaryWeights} that is the same as this one, with a new value of the weight for choice A.
     *
     * @param weight the weight for choice A; must be &gt;= 0
     * @return a {@code BinaryWeights}
     */
    BinaryWeights toA(int weight) {
        return binaryWeights(weight, weightB);
    }

    /**
     * Creates a new {@code BinaryWeights} that is the same as this one, with a new value of the weight for choice B.
     *
     * @param weight the weight for choice B; must be &gt;= 0
     * @return a {@code BinaryWeights}
     */
    BinaryWeights toB(int weight) {
        return binaryWeights(weightA, weight);
    }

    public int getTotalWeight() {
        return weightA + weightB;
    }

    public int getWeightA() {
        return this.weightA;
    }

    public int getWeightB() {
        return this.weightB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryWeights that = (BinaryWeights) o;

        if (weightA != that.weightA) return false;
        return weightB == that.weightB;
    }

    @Override
    public int hashCode() {
        int result = weightA;
        result = 31 * result + weightB;
        return result;
    }

    @Override
    public String toString() {
        return "BinaryWeights{" +
                "weightA=" + weightA +
                ", weightB=" + weightB +
                '}';
    }
}
