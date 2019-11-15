package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EitherWeights {
    private final BinaryWeights weights;

    public int getLeftWeight() {
        return weights.getWeightA();
    }

    public int getRightWeight() {
        return weights.getWeightB();
    }

    public EitherWeights toLeft(int weight) {
        return new EitherWeights(weights.toA(weight));
    }

    public EitherWeights toRight(int weight) {
        return new EitherWeights(weights.toB(weight));
    }

    public static EitherWeights leftWeight(int weight) {
        return new EitherWeights(binaryWeights(1, weight));
    }

    public static EitherWeights rightWeight(int weight) {
        return new EitherWeights(binaryWeights(weight, 1));
    }

    public static EitherWeights eitherWeights(int falseWeight, int trueWeight) {
        return new EitherWeights(binaryWeights(falseWeight, trueWeight));
    }

}
