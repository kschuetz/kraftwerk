package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanWeights {
    private final BinaryWeights weights;

    public int getFalseWeight() {
        return weights.getWeightA();
    }

    public int getTrueWeight() {
        return weights.getWeightB();
    }

    public BooleanWeights toFalse(int weight) {
        return new BooleanWeights(weights.toA(weight));
    }

    public BooleanWeights toTrue(int weight) {
        return new BooleanWeights(weights.toB(weight));
    }

    public static BooleanWeights trueWeight(int weight) {
        return new BooleanWeights(binaryWeights(1, weight));
    }

    public static BooleanWeights falseWeight(int weight) {
        return new BooleanWeights(binaryWeights(weight, 1));
    }

    public static BooleanWeights booleanWeights(int falseWeight, int trueWeight) {
        return new BooleanWeights(binaryWeights(falseWeight, trueWeight));
    }

}
