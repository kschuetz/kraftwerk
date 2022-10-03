package software.kes.kraftwerk.core;

import com.jnape.palatable.lambda.adt.Unit;
import software.kes.kraftwerk.Result;
import software.kes.kraftwerk.Seed;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * Even though this class is public, it is <i>not</i> part of the public API and can change at any time.
 */
public final class BuildingBlocks {

    private static final double DOUBLE_UNIT = 0x1.0p-53;  // 1.0  / (1L << 53)
    private static final float FLOAT_UNIT = 0x1.0p-24f; // 1.0f / (1 << 24)

    private BuildingBlocks() {

    }

    public static Result<Seed, Integer> nextIntBounded(int bound, Seed input) {
        checkBound(bound);

        if ((bound & -bound) == bound) { // bound is a power of 2
            return unsafeNextIntBoundedPowerOf2(bound, input);
        } else {
            return unsafeNextIntBounded(bound, input);
        }
    }

    public static Result<Seed, Integer> unsafeNextIntBoundedPowerOf2(int bound, Seed input) {
        long s1 = getNextSeed(input.getSeedValue());
        int n = bitsFrom(31, s1);
        return Result.result(input.setNextSeedValue(s1), (int) ((bound * (long) n) >> 31));
    }

    public static Result<Seed, Integer> unsafeNextIntBounded(int bound, Seed input) {
        long bits, val;
        long nextSeed = input.getSeedValue();
        do {
            nextSeed = getNextSeed(nextSeed);
            bits = bitsFrom(31, nextSeed);
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return Result.result(input.setNextSeedValue(nextSeed), (int) val);
    }

    public static Result<Seed, Integer> nextInt(Seed input) {
        return next(32, input);
    }

    public static Result<Seed, Integer> nextIntExclusive(int origin, int bound, Seed input) {
        checkOriginBound(origin, bound);

        long n = (long) bound - origin;
        long m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return unsafeNextIntExclusive(origin, (int) n, input);
        } else if ((n & m) == 0) {
            // power of two
            return unsafeNextIntExclusivePowerOf2(origin, n, input);
        } else {
            return unsafeNextIntExclusiveWide(origin, n, input);
        }
    }

    public static Result<Seed, Integer> unsafeNextIntExclusivePowerOf2(int origin, long range, Seed input) {
        long m = range - 1;
        return nextInt(input).fmap(r -> (r & (int) m) + origin);
    }

    public static Result<Seed, Integer> unsafeNextIntExclusive(int origin, int range, Seed input) {
        return nextIntBounded(range, input).fmap(r -> origin + r);
    }

    public static Result<Seed, Integer> unsafeNextIntExclusiveWide(int origin, long range, Seed input) {
        long m = range - 1;
        Result<Seed, Integer> rg1 = nextInt(input);
        Seed current = rg1.getNextState();
        int r = rg1._2();
        for (int u = r >>> 1;
             u + m - (r = u % (int) range) < 0; ) {
            Result<Seed, Integer> next = nextInt(current);
            u = next._2() >>> 1;
            current = next._1();
        }
        r += origin;

        return Result.result(current, r);
    }

    public static Result<Seed, Integer> nextIntBetween(int min, int max, Seed input) {
        checkMinMax(min, max);
        return unsafeNextIntBetween(min, max, input);
    }

    public static Result<Seed, Integer> unsafeNextIntBetween(int min, int max, Seed input) {
        if (max == Integer.MAX_VALUE) {
            if (min == Integer.MIN_VALUE) {
                return nextInt(input);
            } else {
                return nextIntExclusive(min - 1, max, input)
                        .fmap(n -> n + 1);
            }
        } else {
            return nextIntExclusive(min, max + 1, input);
        }
    }

    public static Result<Seed, Double> nextDoubleFractional(Seed input) {
        long s1 = getNextSeed(input.getSeedValue());
        long s2 = getNextSeed(s1);
        int i1 = bitsFrom(26, s1);
        int i2 = bitsFrom(27, s2);
        double result = (((long) i1 << 27) + i2) / (double) (1L << 53);
        return Result.result(input.setNextSeedValue(s2), result);
    }

    public static Result<Seed, Float> nextFloatFractional(Seed input) {
        long s1 = getNextSeed(input.getSeedValue());
        int n = bitsFrom(24, s1);
        float result = (n / ((float) (1 << 24)));
        return Result.result(input.setNextSeedValue(s1), result);
    }

    public static Result<Seed, Double> unsafeNextDoubleBetween(double origin, double bound, Seed input) {
        Result<Seed, Long> l = nextLong(input);
        double r = (l.getValue() >>> 11) * DOUBLE_UNIT;
        r = r * (bound - origin) + origin;
        if (r >= bound) {
            r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
        }
        return Result.result(l.getNextState(), r);
    }

    public static Result<Seed, Long> nextLong(Seed input) {
        long s1 = getNextSeed(input.getSeedValue());
        long s2 = getNextSeed(s1);
        int i1 = bitsFrom(32, s1);
        int i2 = bitsFrom(32, s2);
        long result = ((long) i1 << 32) + i2;
        return Result.result(input.setNextSeedValue(s2), result);
    }

    public static Result<Seed, Long> nextLongBounded(long bound, Seed input) {
        checkBound(bound);

        if (bound <= Integer.MAX_VALUE) {
            return unsafeNextLongBounded((int) bound, input);
        } else {
            return nextLongExclusive(0, bound, input);
        }
    }

    public static Result<Seed, Long> unsafeNextLongBounded(int bound, Seed input) {
        return nextIntBounded(bound, input).fmap(Integer::longValue);
    }

    public static Result<Seed, Long> nextLongExclusive(long origin, long bound, Seed input) {
        checkOriginBound(origin, bound);

        if (origin < 0 && bound > 0 && bound > Math.abs(origin - Long.MIN_VALUE)) {
            return unsafeNextLongExclusiveWithOverflow(origin, bound, input);
        }

        long range = bound - origin;
        long m = range - 1;

        if ((range & m) == 0L) {
            // power of two
            return unsafeNextLongExclusivePowerOf2(origin, range, input);
        } else {
            return unsafeNextLongExclusive(origin, range, input);
        }
    }

    public static Result<Seed, Long> unsafeNextLongExclusivePowerOf2(long origin, long range, Seed input) {
        long m = range - 1;
        return nextLong(input).fmap(r -> (r & m) + origin);
    }

    public static Result<Seed, Long> unsafeNextLongExclusive(long origin, long range, Seed input) {
        long m = range - 1;
        Result<Seed, Long> rg1 = nextLong(input);
        Seed current = rg1.getNextState();
        long r = rg1.getValue();
        for (long u = r >>> 1;
             u + m - (r = u % range) < 0L; ) {
            Result<Seed, Long> next = nextLong(current);
            u = next._2() >>> 1;
            current = next._1();
        }
        r += origin;

        return Result.result(current, r);
    }

    public static Result<Seed, Long> nextLongBetween(long min, long max, Seed input) {
        checkMinMax(min, max);

        if (max == Long.MAX_VALUE) {
            if (min == Long.MIN_VALUE) {
                return nextLong(input);
            } else {
                return nextLongExclusive(min - 1, max, input)
                        .fmap(n -> n + 1);
            }
        } else {
            return nextLongExclusive(min, max + 1, input);
        }
    }

    public static Result<Seed, Long> unsafeNextLongExclusiveWithOverflow(long origin, long bound, Seed input) {
        Result<Seed, Long> result = nextLong(input);
        long value = result.getValue();
        // since we are covering more than half the range of longs, this loop shouldn't take too long
        while (value < origin || value >= bound) {
            result = nextLong(result.getNextState());
            value = result.getValue();
        }
        return result;
    }

    public static Result<Seed, Boolean> nextBoolean(Seed input) {
        long newSeedValue = getNextSeed(input.getSeedValue());
        boolean b = (((int) (newSeedValue >>> 47)) & 1) != 0;

        return Result.result(input.setNextSeedValue(newSeedValue), b);
    }

    private static long getNextSeed(long seedValue) {
        return (seedValue * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
    }

    private static int bitsFrom(int bits, long seed) {
        return (int) (seed >>> (48 - bits));
    }

    public static Result<Seed, Unit> nextBytes(byte[] dest, Seed input) {
        Seed nextSeed = input;
        int i = 0;
        while (i < dest.length) {
            Result<Seed, Integer> nextInt = nextInt(nextSeed);
            nextSeed = nextInt._1();
            int rnd = nextInt._2();
            for (int n = Math.min(dest.length - i, 4); n-- > 0; rnd >>= 8) {
                dest[i++] = (byte) rnd;
            }
        }

        return Result.result(nextSeed, UNIT);
    }

    public static Result<Seed, Double> nextGaussian(Seed input) {
        if (input instanceof StandardSeedCacheGaussian) {
            StandardSeedCacheGaussian cached = (StandardSeedCacheGaussian) input;
            return Result.result(cached.getUnderlying(), cached.getNextGaussian());
        }

        Seed newSeed = input;
        double v1, v2, s;
        do {
            Result<Seed, Double> d1 = nextDoubleFractional(newSeed);
            Result<Seed, Double> d2 = nextDoubleFractional(d1._1());
            newSeed = d2._1();
            v1 = 2 * d1._2() - 1;
            v2 = 2 * d2._2() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        double result = v1 * multiplier;
        double nextResult = v2 * multiplier;
        return Result.result(StandardSeedCacheGaussian.standardSeedCacheGaussian(newSeed, nextResult), result);
    }

    public static Seed perturb(long value, Seed input) {
        long newSeed = nextLong(input).getValue() ^ value;
        return input.setNextSeedValue(newSeed);
    }

    private static Result<Seed, Integer> next(int bits, Seed input) {
        long newSeedValue = (input.getSeedValue() * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        int result = (int) (newSeedValue >>> (48 - bits));

        return Result.result(input.setNextSeedValue(newSeedValue), result);
    }

    public static void checkBound(long bound) {
        if (bound < 1) throw new IllegalArgumentException("bound must be > 0");
    }

    public static void checkOriginBound(long origin, long bound) {
        if (origin >= bound) throw new IllegalArgumentException("bound must be > origin");
    }

    public static void checkMinMax(long min, long max) {
        if (min > max) throw new IllegalArgumentException("max must be >= min");
    }

    public static void checkCount(int count) {
        if (count < 0) throw new IllegalArgumentException("count must be >= 0");
    }

}
