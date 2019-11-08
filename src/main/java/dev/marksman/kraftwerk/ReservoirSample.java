package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.Set;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.FiniteIterable;
import dev.marksman.kraftwerk.random.BuildingBlocks;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;

class ReservoirSample {

    static Generator<FiniteIterable<Integer>> reservoirSample(int n, int k) {
        if (k < 1 || n < 1) {
            return Generators.constant(Vector.empty());
        } else if (k >= n) {
            return Generators.constant(Vector.range(n));
        } else if (k > n / 2) {
            return reservoirSampleImpl(n, n - k)
                    .fmap(exclude -> Vector.range(n).filter(i -> !exclude.contains(i)));
        } else {
            return reservoirSampleImpl(n, k).fmap(upcast());
        }
    }

    private static Generator<Set<Integer>> reservoirSampleImpl(int n, int k) {
        return new Generator<Set<Integer>>() {

            @Override
            public Generate<Set<Integer>> prepare(Parameters parameters) {
                return input -> {
                    Seed current = input;
                    Integer[] result = new Integer[k];
                    for (int i = 0; i < k; i++) {
                        result[i] = i;
                    }
                    for (int i = k; i < n; i++) {
                        Result<? extends Seed, Integer> next = BuildingBlocks.nextIntBounded(i, current);
                        Integer value = next.getValue();
                        if (value < k) {
                            result[value] = i;
                        }
                        current = next.getNextState();
                    }
                    return Result.result(current, Set.copyFrom(result));
                };
            }
        };
    }

}
