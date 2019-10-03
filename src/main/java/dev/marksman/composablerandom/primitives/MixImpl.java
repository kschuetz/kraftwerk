package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.BinaryWeights;
import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;

public class MixImpl<Elem> implements GeneratorState<Elem> {
    private final int weightA;
    private final int totalWeight;
    private final GeneratorState<Elem> left;
    private final GeneratorState<Elem> right;

    private MixImpl(BinaryWeights weights, GeneratorState<Elem> left, GeneratorState<Elem> right) {
        this.weightA = weights.getWeightA();
        this.totalWeight = weights.getTotalWeight();
        this.left = left;
        this.right = right;
    }

    @Override
    public Result<? extends Seed, Elem> run(Seed input) {
        Result<? extends Seed, Integer> nextState = input.nextIntBounded(totalWeight);
        if (nextState.getValue() < weightA) {
            return left.run(nextState.getNextState());
        } else {
            return right.run(nextState.getNextState());
        }
    }

    public static <Elem> GeneratorState<Elem> mixImpl(BinaryWeights weights, GeneratorState<Elem> left, GeneratorState<Elem> right) {
        if (weights.getWeightA() < 1) {
            return right;
        } else if (weights.getWeightB() < 1) {
            return left;
        } else {
            return new MixImpl<>(weights, left, right);
        }
    }
}
