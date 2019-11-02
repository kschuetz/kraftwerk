package dev.marksman.composablerandom.random;

import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;

import static dev.marksman.composablerandom.Result.result;

public class RandomUtils {

    public static long getNextSeed(long seedValue) {
        return (seedValue * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
    }

    public static int bitsFrom(int bits, long seed) {
        return (int) (seed >>> (48 - bits));
    }

    public static Result<Seed, Integer> nextBits(Seed seed, int bits) {
        long newSeedValue = (seed.getSeedValue() * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        int result = (int) (newSeedValue >>> (48 - bits));

        return result(seed.setNextSeedValue(newSeedValue), result);
    }

    public static Result<Seed, Integer> nextInt(Seed input) {
        return nextBits(input, 32);
    }

    public static Result<Seed, Long> nextLong(Seed input) {
        long s1 = getNextSeed(input.getSeedValue());
        long s2 = getNextSeed(s1);
        int i1 = bitsFrom(32, s1);
        int i2 = bitsFrom(32, s2);
        long result = ((long) i1 << 32) + i2;
        return result(input.setNextSeedValue(s2), result);
    }

    public static Result<Seed, Double> nextDouble(Seed input) {
        long s1 = getNextSeed(input.getSeedValue());
        long s2 = getNextSeed(s1);
        int i1 = bitsFrom(26, s1);
        int i2 = bitsFrom(27, s2);
        double result = (((long) i1 << 27) + i2) / (double) (1L << 53);
        return result(input.setNextSeedValue(s2), result);
    }

}
