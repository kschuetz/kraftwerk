package dev.marksman.kraftwerk.weights;

/**
 * A triple of weights that express the frequencies of three possible outcomes in the context of generating values.
 * <p>
 * Any of the three weights can be zero; however, the sum of the weights will always be &gt;= 1.
 */
public final class TernaryWeights {
    public static final TernaryWeights BALANCED = new TernaryWeights(1, 1, 1);

    private final int weightA;
    private final int weightB;
    private final int weightC;

    private TernaryWeights(int weightA, int weightB, int weightC) {
        this.weightA = weightA;
        this.weightB = weightB;
        this.weightC = weightC;
    }

    /**
     * Creates a {@code TernaryWeights} with a specific weight for choice A, and a weight of 1 for choices B and C.
     *
     * @param weight the weight for choice A; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */

    public static TernaryWeights weightA(int weight) {
        return new TernaryWeights(weight, 1, 1);
    }

    /**
     * Creates a {@code TernaryWeights} with a specific weight for choice B, and a weight of 1 for choices A and C.
     *
     * @param weight the weight for choice B; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */
    public static TernaryWeights weightB(int weight) {
        return new TernaryWeights(1, weight, 1);
    }

    /**
     * Creates a {@code TernaryWeights} with a specific weight for choice C, and a weight of 1 for choices A and B.
     *
     * @param weight the weight for choice C; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */
    public static TernaryWeights weightC(int weight) {
        return new TernaryWeights(1, 1, weight);
    }

    /**
     * Creates a {@code TernaryWeights}.  The sum of the weights for all choices must be &gt;= 1.
     *
     * @param weightA the weight for choice A; must be &gt;= 0
     * @param weightB the weight for choice B; must be &gt;= 0
     * @param weightC the weight for choice C; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */
    public static TernaryWeights ternaryWeights(int weightA, int weightB, int weightC) {
        requireNonNegative(weightA);
        requireNonNegative(weightB);
        requireNonNegative(weightC);
        if (weightA + weightB + weightC < 1) {
            throw new IllegalArgumentException("sum of weights must be >= 1");
        }
        return new TernaryWeights(weightA, weightB, weightC);
    }

    public static TernaryWeights ternaryWeights() {
        return BALANCED;
    }

    private static void requireNonNegative(int weight) {
        if (weight < 0) throw new IllegalArgumentException("weight must be >= 0");
    }

    /**
     * Creates a new {@code TernaryWeights} that is the same as this one, with a new value of the weight for choice A.
     *
     * @param weight the weight for choice A; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */
    TernaryWeights toA(int weight) {
        return ternaryWeights(weight, weightB, weightC);
    }

    /**
     * Creates a new {@code TernaryWeights} that is the same as this one, with a new value of the weight for choice B.
     *
     * @param weight the weight for choice B; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */
    TernaryWeights toB(int weight) {
        return ternaryWeights(weightA, weight, weightC);
    }

    /**
     * Creates a new {@code TernaryWeights} that is the same as this one, with a new value of the weight for choice C.
     *
     * @param weight the weight for choice C; must be &gt;= 0
     * @return a {@code TernaryWeights}
     */
    TernaryWeights toC(int weight) {
        return ternaryWeights(weightA, weightB, weight);
    }

    public int getTotalWeight() {
        return weightA + weightB + weightC;
    }

    public int getWeightA() {
        return this.weightA;
    }

    public int getWeightB() {
        return this.weightB;
    }

    public int getWeightC() {
        return this.weightC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TernaryWeights that = (TernaryWeights) o;

        if (weightA != that.weightA) return false;
        if (weightB != that.weightB) return false;
        return weightC == that.weightC;
    }

    @Override
    public int hashCode() {
        int result = weightA;
        result = 31 * result + weightB;
        result = 31 * result + weightC;
        return result;
    }

    @Override
    public String toString() {
        return "TernaryWeights{" +
                "weightA=" + weightA +
                ", weightB=" + weightB +
                ", weightC=" + weightC +
                '}';
    }
}
