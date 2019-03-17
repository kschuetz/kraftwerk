package dev.marksman.random;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.product.Product2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static dev.marksman.random.CacheNextGaussian.cacheNextGaussian;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class StandardGen implements RandomGen {
    private final long seed;
    
    public final long getSeedValue() {
        return seed;
    }

    @Override
    public final Product2<Integer, StandardGen> nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");

        if ((bound & -bound) == bound) { // bound is a power of 2
            return mapResult(n -> (int) ((bound * (long) n) >> 31), next(31));
        }

        int bits, val;
        StandardGen nextSeed;
        Product2<Integer, StandardGen> next;
        do {
            next = next(31);
            bits = next._1();
            nextSeed = next._2();
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return product(val, nextSeed);
    }

    @Override
    public final Product2<Integer, StandardGen> nextInt() {
        return next(32);
    }

    @Override
    public final Product2<Double, StandardGen> nextDouble() {
        Product2<Integer, StandardGen> s1 = next(26);
        Product2<Integer, StandardGen> s2 = s1._2().next(27);
        double result = (((long) s1._1() << 27) + s2._1()) / (double) (1L << 53);
        return product(result, s2._2());
    }

    @Override
    public final Product2<Float, StandardGen> nextFloat() {
        return mapResult(n -> n / ((float) (1 << 24)), next(24));
    }

    @Override
    public final Product2<Long, StandardGen> nextLong() {
        Product2<Integer, StandardGen> s1 = next(32);
        Product2<Integer, StandardGen> s2 = s1._2().next(32);
        long result = ((long) s1._1() << 32) + s2._1();
        return product(result, s2._2());
    }

    @Override
    public final Product2<Boolean, StandardGen> nextBoolean() {
        return mapResult(n -> n != 0, next(1));
    }

    @Override
    public final Product2<Unit, StandardGen> nextBytes(byte[] dest) {
        StandardGen nextSeed = this;
        int i = 0;
        while (i < dest.length) {
            Product2<Integer, StandardGen> nextInt = nextSeed.nextInt();
            int rnd = nextInt._1();
            nextSeed = nextInt._2();
            for (int n = Math.min(dest.length - i, 4); n-- > 0; rnd >>= 8) {
                dest[i++] = (byte) rnd;
            }
        }

        return product(UNIT, nextSeed);
    }

    @Override
    public final Product2<Double, CacheNextGaussian> nextGaussian() {
        StandardGen newSeed = this;
        double v1, v2, s;
        do {
            Product2<Double, StandardGen> d1 = newSeed.nextDouble();
            Product2<Double, StandardGen> d2 = d1._2().nextDouble();
            newSeed = d2._2();
            v1 = 2 * d1._1() - 1;
            v2 = 2 * d2._1() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        double result = v1 * multiplier;
        double nextResult = v2 * multiplier;
        return product(result, cacheNextGaussian(newSeed, nextResult));
    }

    private Product2<Integer, StandardGen> next(int bits) {
        long newSeedValue = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        int result = (int) (newSeedValue >>> (48 - bits));

        return product(result, nextStandardGen(newSeedValue));
    }

    private static <A, B, C> Product2<B, C> mapResult(Function<A, B> fn, Product2<A, C> p) {
        return product(fn.apply(p._1()), p._2());
    }

    public static StandardGen initStandardGen(long seed) {
        return nextStandardGen((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1));
    }

    public static StandardGen nextStandardGen(long seed) {
        return new StandardGen(seed);
    }

}
