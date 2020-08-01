package dev.marksman.kraftwerk.weights;

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

    public static TernaryWeights weightA(int weight) {
        return new TernaryWeights(weight, 1, 1);
    }

    public static TernaryWeights weightB(int weight) {
        return new TernaryWeights(1, weight, 1);
    }

    public static TernaryWeights weightC(int weight) {
        return new TernaryWeights(1, 1, weight);
    }

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

    TernaryWeights toA(int weight) {
        return ternaryWeights(weight, weightB, weightC);
    }

    TernaryWeights toB(int weight) {
        return ternaryWeights(weightA, weight, weightC);
    }

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
