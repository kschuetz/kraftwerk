package dev.marksman.composablerandom;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.Generator.generate;
import static dev.marksman.composablerandom.Result.result;

class BigNumbers {

    static Generator<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
        return generate(rs -> {
            int bitLength = bound.bitLength();
            Result<? extends RandomState, Long> seed = rs.nextLong();
            Random rnd = new Random();
            rnd.setSeed(seed.getValue());
            BigInteger result;
            do {
                result = new BigInteger(bitLength, rnd);
            } while (result.compareTo(bound) >= 0);
            return result(seed.getNextState(), result);
        });
    }

    static Generator<BigInteger> generateBigIntegerExclusive(BigInteger origin, BigInteger bound) {
        BigInteger range = bound.subtract(origin);
        if (range.signum() < 1) throw new IllegalArgumentException("bound must be > origin");
        return generateBigIntegerExclusive(range).fmap(origin::add);
    }

    static Generator<BigInteger> generateBigInteger(BigInteger min, BigInteger max) {
        BigInteger range = max.subtract(min);
        if (range.signum() < 0) throw new IllegalArgumentException("max must be >= min");
        return generateBigIntegerExclusive(range.add(BigInteger.ONE));
    }

    static Generator<BigDecimal> generateBigDecimalExclusive(int decimalPlaces, BigDecimal bound) {
        BigInteger integerBound = bound.movePointRight(decimalPlaces).toBigInteger();
        if (integerBound.signum() < 0) {
            throw new IllegalArgumentException("bound must be > 0");
        }
        return generateBigIntegerExclusive(integerBound)
                .fmap(n -> new BigDecimal(n).movePointLeft(decimalPlaces));
    }

    static Generator<BigDecimal> generateBigDecimalExclusive(int decimalPlaces, BigDecimal origin, BigDecimal bound) {
        BigInteger integerOrigin = origin.movePointRight(decimalPlaces).toBigInteger();
        BigInteger integerBound = bound.movePointRight(decimalPlaces).toBigInteger();
        BigInteger range = integerBound.subtract(integerOrigin);
        if (range.signum() < 1) {
            throw new IllegalArgumentException("bound must be > origin");
        }
        return generateBigIntegerExclusive(range)
                .fmap(n -> new BigDecimal(integerOrigin.add(n)).movePointLeft(decimalPlaces));
    }


    static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimal min, BigDecimal max) {
        BigInteger integerOrigin = min.movePointRight(decimalPlaces).toBigInteger();
        BigInteger integerBound = max.movePointRight(decimalPlaces).toBigInteger();
        BigInteger range = integerBound.subtract(integerOrigin);
        int signum = range.signum();
        if (signum < 0) {
            throw new IllegalArgumentException("max must be >= min");
        } else if (signum == 0) {
            return constant(new BigDecimal(integerOrigin).movePointLeft(decimalPlaces));
        } else {
            return generateBigInteger(BigInteger.ZERO, range)
                    .fmap(n -> new BigDecimal(integerOrigin.add(n)).movePointLeft(decimalPlaces));
        }
    }


    /*
    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
    BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
    return randomBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
}
     */

}
