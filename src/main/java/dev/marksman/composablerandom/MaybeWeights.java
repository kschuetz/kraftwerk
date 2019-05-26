package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MaybeWeights {
    private final BinaryWeights weights;

    public int getNothingWeight() {
        return weights.getWeightA();
    }

    public int getJustWeight() {
        return weights.getWeightB();
    }

    public MaybeWeights toNothing(int weight) {
        return new MaybeWeights(weights.toA(weight));
    }

    public MaybeWeights toJust(int weight) {
        return new MaybeWeights(weights.toB(weight));
    }

    public static MaybeWeights justWeight(int weight) {
        return new MaybeWeights(binaryWeights(1, weight));
    }

    public static MaybeWeights nothingWeight(int weight) {
        return new MaybeWeights(binaryWeights(weight, 1));
    }

    public static MaybeWeights maybeWeights(int nothingWeight, int justWeight) {
        return new MaybeWeights(binaryWeights(nothingWeight, justWeight));
    }

}
