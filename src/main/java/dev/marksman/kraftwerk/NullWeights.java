package dev.marksman.kraftwerk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NullWeights {
    private final BinaryWeights weights;

    public int getNullWeight() {
        return weights.getWeightA();
    }

    public int getNonNullWeight() {
        return weights.getWeightB();
    }

    public NullWeights toNull(int weight) {
        return new NullWeights(weights.toA(weight));
    }

    public NullWeights toNonNull(int weight) {
        return new NullWeights(weights.toB(weight));
    }

    public static NullWeights nonNullWeight(int weight) {
        return new NullWeights(binaryWeights(1, weight));
    }

    public static NullWeights nullWeight(int weight) {
        return new NullWeights(binaryWeights(weight, 1));
    }

    public static NullWeights nullWeights(int nullWeight, int nonNullWeight) {
        return new NullWeights(binaryWeights(nullWeight, nonNullWeight));
    }

}
