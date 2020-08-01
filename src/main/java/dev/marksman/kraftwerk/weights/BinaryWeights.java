package dev.marksman.kraftwerk.weights;

public final class BinaryWeights {
    private final int weightA;
    private final int weightB;

    private BinaryWeights(int weightA, int weightB) {
        this.weightA = weightA;
        this.weightB = weightB;
    }

    public static BinaryWeights weightA(int weight) {
        return new BinaryWeights(weight, 1);
    }

    public static BinaryWeights weightB(int weight) {
        return new BinaryWeights(1, weight);
    }

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

    BinaryWeights toA(int weight) {
        return binaryWeights(weight, weightB);
    }

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
