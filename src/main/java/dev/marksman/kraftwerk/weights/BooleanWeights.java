package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanWeights {
    BinaryWeights weights;

    public int getFalseWeight() {
        return weights.getWeightA();
    }

    public int getTrueWeight() {
        return weights.getWeightB();
    }

    public static BooleanWeightsBuilderTrues trues(int weight) {
        return new BooleanWeightsBuilderTrues(binaryWeights(1, weight));
    }

    public static BooleanWeightsBuilderFalses falses(int weight) {
        return new BooleanWeightsBuilderFalses(binaryWeights(weight, 1));
    }

    public static BooleanWeights booleanWeights(int falseWeight, int trueWeight) {
        return new BooleanWeights(binaryWeights(falseWeight, trueWeight));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BooleanWeightsBuilderFalses {
        BinaryWeights weights;

        public BooleanWeights toTrues(int weight) {
            return new BooleanWeights(weights.toB(weight));
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BooleanWeightsBuilderTrues {
        BinaryWeights weights;

        public BooleanWeights toFalses(int weight) {
            return new BooleanWeights(weights.toA(weight));
        }
    }

}
