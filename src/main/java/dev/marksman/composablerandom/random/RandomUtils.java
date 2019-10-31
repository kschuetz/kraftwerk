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
}
