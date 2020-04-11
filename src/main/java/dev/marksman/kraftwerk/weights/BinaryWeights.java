package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BinaryWeights {
    int weightA;
    int weightB;

    BinaryWeights toA(int weight) {
        return binaryWeights(weight, weightB);
    }

    BinaryWeights toB(int weight) {
        return binaryWeights(weightA, weight);
    }

    public int getTotalWeight() {
        return weightA + weightB;
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

}
