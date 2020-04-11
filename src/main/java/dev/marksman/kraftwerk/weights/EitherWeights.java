package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EitherWeights {
    BinaryWeights weights;

    public int getLeftWeight() {
        return weights.getWeightA();
    }

    public int getRightWeight() {
        return weights.getWeightB();
    }

    public static EitherWeightsBuilderLefts lefts(int weight) {
        return new EitherWeightsBuilderLefts(binaryWeights(1, weight));
    }

    public static EitherWeightsBuilderRights rights(int weight) {
        return new EitherWeightsBuilderRights(binaryWeights(weight, 1));
    }

    public static EitherWeights eitherWeights(int falseWeight, int trueWeight) {
        return new EitherWeights(binaryWeights(falseWeight, trueWeight));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EitherWeightsBuilderLefts {
        BinaryWeights weights;

        public EitherWeights toRights(int weight) {
            return new EitherWeights(weights.toB(weight));
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EitherWeightsBuilderRights {
        BinaryWeights weights;

        public EitherWeights toLefts(int weight) {
            return new EitherWeights(weights.toA(weight));
        }
    }

}
