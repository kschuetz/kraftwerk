package dev.marksman.composablerandom;

import java.math.BigInteger;
import java.util.Random;

import static dev.marksman.composablerandom.Generator.generator;
import static dev.marksman.composablerandom.Result.result;

class BigNumbers {

    static Generator<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
        return generator(rs -> {
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

}
