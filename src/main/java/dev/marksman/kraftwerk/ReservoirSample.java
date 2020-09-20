package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.Set;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.FiniteIterable;
import dev.marksman.kraftwerk.core.BuildingBlocks;

import java.util.Arrays;

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

    // TODO: generateBitMask needs work
    static Generator<Long> generateBitMask(int totalBits, int ones) {
        totalBits = Math.min(64, totalBits);
        totalBits = Math.max(0, totalBits);
        ones = Math.min(totalBits, ones);
        ones = Math.max(0, ones);
        final byte n = (byte) totalBits;
        final byte k = (byte) ones;
        if (totalBits == 0) {
            return Generators.constant(0L);
        } else if (ones == 64) {
            return Generators.constant(-1L);
        } else if (ones == totalBits) {
            return Generators.constant((1L << ones) - 1);
        } else return parameters -> input -> {
            Seed current = input;
            byte[] places = new byte[k];
            for (byte i = 0; i < k; i++) {
                places[i] = i;
            }
            System.out.println(Arrays.toString(places));
            for (byte i = k; i < n; i++) {
                Result<? extends Seed, Integer> next = BuildingBlocks.nextIntBounded(i, current);
                Integer value = next.getValue();
                System.out.println("value = " + value);
                if (value < k) {
                    places[value] = i;
                }
                current = next.getNextState();
            }
            System.out.println(Arrays.toString(places));
            long result = 0L;
            for (byte i = 0; i < k; i++) {
                result = result | (1L << places[i]);
            }
            return Result.result(current, result);
        };
    }
}
