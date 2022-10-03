package software.kes.kraftwerk;

import software.kes.collectionviews.Set;
import software.kes.collectionviews.Vector;
import software.kes.enhancediterables.FiniteIterable;
import software.kes.kraftwerk.core.BuildingBlocks;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;

final class ReservoirSample {
    private ReservoirSample() {
    }

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
        return parameters -> input -> {
            Integer[] result = new Integer[k];
            for (int i = 0; i < k; i++) {
                result[i] = i;
            }
            Result<Seed, Double> r0 = BuildingBlocks.nextDoubleFractional(input);
            double w = Math.exp(Math.log(r0.getValue()) / k);
            int j = k - 1;
            Seed current = r0.getNextState();
            while (j < n) {
                Result<? extends Seed, Double> r1 = BuildingBlocks.nextDoubleFractional(current);
                j += (int) (Math.log(r1.getValue()) / Math.log(1d - w)) + 1;
                if (j < n) {
                    Result<Seed, Double> r2 = BuildingBlocks.nextDoubleFractional(r1.getNextState());
                    Result<Seed, Integer> r3 = BuildingBlocks.unsafeNextIntBounded(k, r2.getNextState());
                    result[r3.getValue()] = j;
                    w = w * Math.exp(Math.log(r2.getValue()) / k);
                    current = r3.getNextState();
                } else {
                    current = r1.getNextState();
                }
            }
            return Result.result(current, Set.copyFrom(result));
        };
    }
}
