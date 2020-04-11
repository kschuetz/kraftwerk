package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TernaryWeights {
    public static final TernaryWeights BALANCED = new TernaryWeights(1, 1, 1);

    int weightA;
    int weightB;
    int weightC;

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

}
