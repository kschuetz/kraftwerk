package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;

import java.util.function.Supplier;

import static dev.marksman.composablerandom.builtin.Primitives.generateIntExclusive;

class Weighted {

    static <A> Generator<A> leftRight(int leftWeight, int rightWeight,
                                      String leftName, String rightName,
                                      Supplier<Generator<A>> leftGenerator,
                                      Supplier<Generator<A>> rightGenerator) {
        if (leftWeight < 0) {
            throw new IllegalArgumentException(leftName + " must be >= 0");
        }
        if (rightWeight < 0) {
            throw new IllegalArgumentException(rightName + " must be >= 0");
        }
        int total = leftWeight + rightWeight;
        if (total < 1) {
            throw new IllegalArgumentException("sum of weights must be >= 1");
        }
        if (leftWeight == 0) {
            return rightGenerator.get();
        } else if (rightWeight == 0) {
            return leftGenerator.get();
        } else {
            Generator<A> left = leftGenerator.get();
            Generator<A> right = rightGenerator.get();
            return generateIntExclusive(total)
                    .flatMap(n -> n < leftWeight ? left : right);
        }
    }
}
