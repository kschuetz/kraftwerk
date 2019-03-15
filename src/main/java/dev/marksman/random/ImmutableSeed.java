package dev.marksman.random;

import com.jnape.palatable.lambda.adt.product.Product2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.product.Product2.product;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class ImmutableSeed implements RandomGen {
    private final int seedHi;
    private final int seedLo;

    @Override
    public Product2<Integer, ImmutableSeed> nextInt() {
        return next(32);
    }

    @Override
    public Product2<Integer, ImmutableSeed> nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        } else if ((bound & -bound) == bound) {
            // power of two
            Product2<Integer, ImmutableSeed> next = next(31);
            int result = next._1() >> Integer.numberOfLeadingZeros(bound);
            return product(result, next._2());
        } else {
            ImmutableSeed state = this;
            do {
                Product2<Integer, ImmutableSeed> x = state.next(31);
                int bits = x._1();
                int value = bits % bound;
                if (bits - value + (bound - 1) < 0) {
                    return product(value, state);
                } else {
                    state = x._2();
                }
            } while (true);
        }
    }

    @Override
    public Product2<Double, ImmutableSeed> nextDouble() {
        Product2<Integer, ImmutableSeed> s1 = next(26);
        Product2<Integer, ImmutableSeed> s2 = s1._2().next(27);
        double r1 = (((double) s1._1()) * (double) (1L << 27)) + (double) s2._1();
        double result = r1 / ((double) (1L << 53));
        return product(result, s2._2());
    }

    @Override
    public Product2<Float, ImmutableSeed> nextFloat() {
        Product2<Integer, ImmutableSeed> s1 = next(24);
        double result = s1._1().doubleValue() / (double) (1 << 24);
        return product((float) result, s1._2());
    }

    @Override
    public Product2<Long, ImmutableSeed> nextLong() {
        Product2<Integer, ImmutableSeed> s1 = next(32);
        Product2<Integer, ImmutableSeed> s2 = s1._2().next(32);
        return product((s1._1().longValue() << 32) | (s2._1().longValue()),
                s2._2());
    }

    @Override
    public Product2<Boolean, ImmutableSeed> nextBoolean() {
        Product2<Integer, ImmutableSeed> s1 = next(1);
        return product(s1._1() != 0, s1._2());
    }

    @Override
    public Product2<Byte, ImmutableSeed> nextByte() {
        Product2<Integer, ImmutableSeed> s1 = next(8);
        return product(s1._1().byteValue(), s1._2());
    }

    @Override
    public Product2<Short, ImmutableSeed> nextShort() {
        Product2<Integer, ImmutableSeed> s1 = next(16);
        return product(s1._1().shortValue(), s1._2());
    }

    @Override
    public Product2<Byte[], ImmutableSeed> nextBytes(byte[] dest) {
        return null;
    }

    private Product2<Integer, ImmutableSeed> next(int bits) {
        // TODO: next(bits)

        int oldSeedLo = this.seedLo;

        long mul = 0x5DEECE66DL;
        int mulHi = (int) (mul >>> 24);
        int mulLo = ((int) mul) & ((1 << 24) - 1);

        int loProd = oldSeedLo * mulLo + 0xB;
        int hiProd = oldSeedLo * mulHi + this.seedHi * mulLo;

        int newSeedHi = msb24(loProd) + lsb24(hiProd) & ((1 << 24) - 1);
        int newSeedLo = lsb24(loProd);

        int result32 = (newSeedHi << 8) | (newSeedLo >> 16);
        int result = result32 >>> (32 - bits);

        return product(result, immutableSeed(newSeedHi, newSeedLo));
    }

    private static int msb24(int x) {
        return lsb24(x >> 8);
    }

    private static int lsb24(int x) {
        return x & ((1 << 24) - 1);
    }

    public static ImmutableSeed immutableSeed(int seedHi, int seedLo) {
        return new ImmutableSeed(seedHi, seedLo);
    }
}
