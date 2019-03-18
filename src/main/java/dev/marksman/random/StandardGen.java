package dev.marksman.random;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.product.Product2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Random;
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
    public final Product2<StandardGen, Integer> nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");

        if ((bound & -bound) == bound) { // bound is a power of 2
            return mapResult(n -> (int) ((bound * (long) n) >> 31), next(31));
        }

        int bits, val;
        StandardGen nextSeed;
        Product2<StandardGen, Integer> next;
        do {
            next = next(31);
            nextSeed = next._1();
            bits = next._2();
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return product(nextSeed, val);
    }

    @Override
    public final Product2<StandardGen, Integer> nextInt() {
        return next(32);
    }

    @Override
    public final Product2<StandardGen, Double> nextDouble() {
        Product2<StandardGen, Integer> s1 = next(26);
        Product2<StandardGen, Integer> s2 = s1._1().next(27);
        double result = (((long) s1._2() << 27) + s2._2()) / (double) (1L << 53);
        return product(s2._1(), result);
    }

    @Override
    public final Product2<StandardGen, Float> nextFloat() {
        return mapResult(n -> n / ((float) (1 << 24)), next(24));
    }

    @Override
    public final Product2<StandardGen, Long> nextLong() {
        Product2<StandardGen, Integer> s1 = next(32);
        Product2<StandardGen, Integer> s2 = s1._1().next(32);
        long result = ((long) s1._2() << 32) + s2._2();
        return product(s2._1(), result);
    }

    @Override
    public final Product2<StandardGen, Boolean> nextBoolean() {
        return mapResult(n -> n != 0, next(1));
    }

    @Override
    public final Product2<StandardGen, Unit> nextBytes(byte[] dest) {
        StandardGen nextSeed = this;
        int i = 0;
        while (i < dest.length) {
            Product2<StandardGen, Integer> nextInt = nextSeed.nextInt();
            nextSeed = nextInt._1();
            int rnd = nextInt._2();
            for (int n = Math.min(dest.length - i, 4); n-- > 0; rnd >>= 8) {
                dest[i++] = (byte) rnd;
            }
        }

        return product(nextSeed, UNIT);
    }

    @Override
    public final Product2<CacheNextGaussian, Double> nextGaussian() {
        StandardGen newSeed = this;
        double v1, v2, s;
        do {
            Product2<StandardGen, Double> d1 = newSeed.nextDouble();
            Product2<StandardGen, Double> d2 = d1._1().nextDouble();
            newSeed = d2._1();
            v1 = 2 * d1._2() - 1;
            v2 = 2 * d2._2() - 1;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
        double result = v1 * multiplier;
        double nextResult = v2 * multiplier;
        return product(cacheNextGaussian(newSeed, nextResult), result);
    }

    private Product2<StandardGen, Integer> next(int bits) {
        long newSeedValue = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        int result = (int) (newSeedValue >>> (48 - bits));

        return product(nextStandardGen(newSeedValue), result);
    }

    public static StandardGen initStandardGen(long seed) {
        return nextStandardGen((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1));
    }

    public static StandardGen initStandardGen() {
        Random random = new Random();
        return initStandardGen(random.nextLong());
    }

    private static <A, B, R> Product2<R, B> mapResult(Function<A, B> fn, Product2<R, A> p) {
        return product(p._1(), fn.apply(p._2()));
    }

    private static StandardGen nextStandardGen(long seed) {
        return new StandardGen(seed);
    }

}
