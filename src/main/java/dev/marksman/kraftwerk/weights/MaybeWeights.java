package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MaybeWeights {
    BinaryWeights weights;

    public int getNothingWeight() {
        return weights.getWeightA();
    }

    public int getJustWeight() {
        return weights.getWeightB();
    }

    public static MaybeWeightsBuilderJusts justs(int weight) {
        return new MaybeWeightsBuilderJusts(binaryWeights(1, weight));
    }

    public static MaybeWeightsBuilderNothings nothings(int weight) {
        return new MaybeWeightsBuilderNothings(binaryWeights(weight, 1));
    }

    public static MaybeWeights maybeWeights(int nothingWeight, int justWeight) {
        return new MaybeWeights(binaryWeights(nothingWeight, justWeight));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MaybeWeightsBuilderNothings {
        BinaryWeights weights;

        public MaybeWeights toJusts(int weight) {
            return new MaybeWeights(weights.toB(weight));
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MaybeWeightsBuilderJusts {
        BinaryWeights weights;

        public MaybeWeights toNothings(int weight) {
            return new MaybeWeights(weights.toA(weight));
        }
    }

}
