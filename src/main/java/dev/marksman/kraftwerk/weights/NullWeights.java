package dev.marksman.kraftwerk.weights;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.weights.BinaryWeights.binaryWeights;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NullWeights {
    BinaryWeights weights;

    public int getNullWeight() {
        return weights.getWeightA();
    }

    public int getNonNullWeight() {
        return weights.getWeightB();
    }

    public static NullWeightsBuilderNonNull nonNulls(int weight) {
        return new NullWeightsBuilderNonNull(binaryWeights(1, weight));
    }

    public static NullWeightsBuilderNulls nulls(int weight) {
        return new NullWeightsBuilderNulls(binaryWeights(weight, 1));
    }

    public static NullWeights nullWeights(int nullWeight, int nonNullWeight) {
        return new NullWeights(binaryWeights(nullWeight, nonNullWeight));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class NullWeightsBuilderNulls {
        BinaryWeights weights;

        public NullWeights toNonNulls(int weight) {
            return new NullWeights(weights.toB(weight));
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class NullWeightsBuilderNonNull {
        BinaryWeights weights;

        public NullWeights toNulls(int weight) {
            return new NullWeights(weights.toA(weight));
        }
    }

}
