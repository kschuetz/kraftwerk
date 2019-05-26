package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class BinaryWeights {
    private final int weightA;
    private final int weightB;

    BinaryWeights toA(int weight) {
        return binaryWeights(weight, weightB);
    }

    BinaryWeights toB(int weight) {
        return binaryWeights(weightA, weight);
    }

    public static BinaryWeights weightA(int weight) {
        return new BinaryWeights(weight, 1);
    }

    public static BinaryWeights weightB(int weight) {
        return new BinaryWeights(1, weight);
    }

    static BinaryWeights binaryWeights(int weightA, int weightB) {
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

}
