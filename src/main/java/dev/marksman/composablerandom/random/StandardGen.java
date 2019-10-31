package dev.marksman.composablerandom.random;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Random;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.random.CacheNextGaussian.cacheNextGaussian;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class StandardGen implements LegacySeed {
    private final long seedValue;

    @Override
    public final Result<StandardGen, Integer> nextIntBounded(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");

        if ((bound & -bound) == bound) { // bound is a power of 2
            long s1 = getNextSeed(seedValue);
            int n = bitsFrom(31, s1);
            return result(nextStandardGen(s1), (int) ((bound * (long) n) >> 31));
        }

        long bits, val;
        long nextSeed = seedValue;
        do {
            nextSeed = getNextSeed(nextSeed);
            bits = bitsFrom(31, nextSeed);
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return result(nextStandardGen(nextSeed), (int) val);
    }

    @Override
    public final Result<StandardGen, Integer> nextInt() {
        return next(32);
    }

    @Override
    public final Result<StandardGen, Integer> nextIntExclusive(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = (long) bound - origin;
        long m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return nextIntBounded((int) n).fmap(r -> origin + r);
        } else if ((n & m) == 0) {
            // power of two
            return nextInt().fmap(r -> (r & (int) m) + origin);
        } else {
            Result<StandardGen, Integer> rg1 = nextInt();
            StandardGen current = rg1.getNextState();
            int r = rg1._2();
            for (int u = r >>> 1;
                 u + m - (r = u % (int) n) < 0; ) {
                Result<StandardGen, Integer> next = current.nextInt();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        }
    }

    @Override
    public final Result<StandardGen, Integer> nextIntBetween(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be >= min");
        }
        if (max == Integer.MAX_VALUE) {
            if (min == Integer.MIN_VALUE) {
                return nextInt();
            } else {
                return nextIntExclusive(min - 1, max)
                        .fmap(n -> n + 1);
            }
        } else {
            return nextIntExclusive(min, max + 1);
        }
    }

    @Override
    public final Result<StandardGen, Double> nextDouble() {
        long s1 = getNextSeed(seedValue);
        long s2 = getNextSeed(s1);
        int i1 = bitsFrom(26, s1);
        int i2 = bitsFrom(27, s2);
        double result = (((long) i1 << 27) + i2) / (double) (1L << 53);
        return result(nextStandardGen(s2), result);
    }

    @Override
    public final Result<StandardGen, Float> nextFloat() {
        long s1 = getNextSeed(seedValue);
        int n = bitsFrom(24, s1);
        float result = (n / ((float) (1 << 24)));
        return result(nextStandardGen(s1), result);
    }

    @Override
    public final Result<StandardGen, Long> nextLong() {
        long s1 = getNextSeed(seedValue);
        long s2 = getNextSeed(s1);
        int i1 = bitsFrom(32, s1);
        int i2 = bitsFrom(32, s2);
        long result = ((long) i1 << 32) + i2;
        return result(nextStandardGen(s2), result);
    }

    @Override
    public Result<StandardGen, Long> nextLongBounded(long bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");
        if (bound <= Integer.MAX_VALUE) {
            return nextIntBounded((int) bound).fmap(Integer::longValue);
        } else {
            return nextLongExclusive(0, bound);
        }
    }

    @Override
    public Result<StandardGen, Long> nextLongExclusive(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }

        if (origin < 0 && bound > 0 && bound > Math.abs(origin - Long.MIN_VALUE)) {
            return nextLongExclusiveWithOverflow(origin, bound);
        }

        long n = bound - origin;
        long m = n - 1;

        if ((n & m) == 0L) {
            // power of two
            return nextLong().fmap(r -> (r & m) + origin);
        } else {
            Result<StandardGen, Long> rg1 = nextLong();
            StandardGen current = rg1.getNextState();
            long r = rg1.getValue();
            for (long u = r >>> 1;
                 u + m - (r = u % n) < 0L; ) {
                Result<StandardGen, Long> next = current.nextLong();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        }
    }

    @Override
    public Result<StandardGen, Long> nextLongBetween(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("max must be >= min");
        }
        if (max == Long.MAX_VALUE) {
            if (min == Long.MIN_VALUE) {
                return nextLong();
            } else {
                return nextLongExclusive(min - 1, max)
                        .fmap(n -> n + 1);
            }
        } else {
            return nextLongExclusive(min, max + 1);
        }
    }

    private Result<StandardGen, Long> nextLongExclusiveWithOverflow(long origin, long bound) {
        Result<StandardGen, Long> result = nextLong();
        long value = result.getValue();
        // since we are covering more than half the range of longs, this loop shouldn't take too long
        while (value < origin || value >= bound) {
            result = result.getNextState().nextLong();
            value = result.getValue();
        }
        return result;
    }

    @Override
    public final Result<StandardGen, Boolean> nextBoolean() {
        long newSeedValue = getNextSeed(seedValue);
        boolean b = (((int) (newSeedValue >>> 47)) & 1) != 0;

        return result(nextStandardGen(newSeedValue), b);
    }

    private static long getNextSeed(long seedValue) {
        return (seedValue * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
    }

    private static int bitsFrom(int bits, long seed) {
        return (int) (seed >>> (48 - bits));
    }

    @Override
    public final Result<StandardGen, Unit> nextBytes(byte[] dest) {
        StandardGen nextSeed = this;
        int i = 0;
        while (i < dest.length) {
            Result<StandardGen, Integer> nextInt = nextSeed.nextInt();
            nextSeed = nextInt._1();
            int rnd = nextInt._2();
            for (int n = Math.min(dest.length - i, 4); n-- > 0; rnd >>= 8) {
                dest[i++] = (byte) rnd;
            }
        }

        return result(nextSeed, UNIT);
    }

    @Override
    public final Result<CacheNextGaussian, Double> nextGaussian() {
        StandardGen newSeed = this;
        double v1, v2, s;
        do {
            Result<StandardGen, Double> d1 = newSeed.nextDouble();
            Result<StandardGen, Double> d2 = d1._1().nextDouble();
            newSeed = d2._1();
            v1 = 2 * d1._2() - 1;
            v2 = 2 * d2._2() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        double result = v1 * multiplier;
        double nextResult = v2 * multiplier;
        return result(cacheNextGaussian(newSeed, nextResult), result);
    }

    @Override
    public final LegacySeed perturb(long value) {
        long newSeed = nextLong().getValue() ^ value;
        return nextStandardGen(newSeed);
    }


    private Result<StandardGen, Integer> next(int bits) {
        long newSeedValue = (seedValue * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        int result = (int) (newSeedValue >>> (48 - bits));

        return result(nextStandardGen(newSeedValue), result);
    }

    public static StandardGen initStandardGen(long seed) {
        return nextStandardGen((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1));
    }

    public static StandardGen initStandardGen() {
        Random random = new Random();
        return initStandardGen(random.nextLong());
    }

    private static StandardGen nextStandardGen(long seed) {
        return new StandardGen(seed);
    }

}
