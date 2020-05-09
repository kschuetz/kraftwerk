package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.constraints.BigDecimalRange;
import dev.marksman.kraftwerk.constraints.BigIntegerRange;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static dev.marksman.kraftwerk.Bias.applyBiasSetting;
import static dev.marksman.kraftwerk.Generators.generateLong;

class BigNumbers {
    private static final BigIntegerRange DEFAULT_BIG_INTEGER_RANGE =
            BigIntegerRange.from(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE))
                    .to(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));

    static Generator<BigInteger> generateBigInteger() {
        return generateBigInteger(DEFAULT_BIG_INTEGER_RANGE);
    }

    static Generator<BigInteger> generateBigInteger(BigIntegerRange range) {
        BigInteger min = range.minInclusive();
        if (min.signum() == 0) {
            return generateBigIntegerExclusive(range.maxExclusive());
        } else {
            return generateBigIntegerExclusive(min, range.maxExclusive());
        }
    }

    static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimalRange range) {
        BigInteger integerOrigin = makeMinInclusive(range.minIncluded(), movePointRight(decimalPlaces, range.min()));
        BigInteger integerBound = makeMaxExclusive(range.maxIncluded(), movePointRight(decimalPlaces, range.max()));
        BigInteger integerRange = integerBound.subtract(integerOrigin);

        if (integerRange.signum() < 1) {
            throw new IllegalArgumentException("range too narrow");
        }

        return generateBigIntegerExclusive(integerRange)
                .fmap(n -> movePointLeft(decimalPlaces, integerOrigin.add(n)));
    }

    private static Generator<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
        return applyBiasSetting(bs -> bs.bigIntegerBias(BigIntegerRange.exclusive(bound)),
                generateLong().fmap(s -> {
                    int bitLength = bound.bitLength();
                    Random rnd = new Random();
                    rnd.setSeed(s);
                    BigInteger result;
                    do {
                        result = new BigInteger(bitLength, rnd);
                    } while (result.compareTo(bound) >= 0);
                    return result;
                }));
    }

    private static Generator<BigInteger> generateBigIntegerExclusive(BigInteger origin, BigInteger bound) {
        BigInteger range = bound.subtract(origin);
        if (range.signum() < 1) throw new IllegalArgumentException("bound must be > origin");
        return generateBigIntegerExclusive(range).fmap(origin::add);
    }

    private static BigInteger movePointRight(int decimalPlaces, BigDecimal input) {
        return input.movePointRight(decimalPlaces).toBigInteger();
    }

    private static BigDecimal movePointLeft(int decimalPlaces, BigInteger input) {
        return new BigDecimal(input).movePointLeft(decimalPlaces);
    }

    private static BigInteger makeMinInclusive(boolean minIncluded, BigInteger min) {
        return minIncluded ? min : min.add(BigInteger.ONE);
    }

    private static BigInteger makeMaxExclusive(boolean maxIncluded, BigInteger max) {
        return maxIncluded ? max.add(BigInteger.ONE) : max;
    }

}
