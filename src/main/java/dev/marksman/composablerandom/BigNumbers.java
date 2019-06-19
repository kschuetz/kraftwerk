package dev.marksman.composablerandom;

import java.math.BigInteger;
import java.util.Random;

import static dev.marksman.composablerandom.Generate.generate;
import static dev.marksman.composablerandom.Result.result;

class BigNumbers {

    static Generate<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
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

    static Generate<BigInteger> generateBigIntegerExclusive(BigInteger origin, BigInteger bound) {
        BigInteger range = bound.subtract(origin);
        if (range.signum() < 1) throw new IllegalArgumentException("bound must be > origin");
        return generateBigIntegerExclusive(range).fmap(origin::add);
    }

    static Generate<BigInteger> generateBigInteger(BigInteger min, BigInteger max) {
        BigInteger range = max.subtract(min);
        if (range.signum() < 0) throw new IllegalArgumentException("max must be >= min");
        return generateBigIntegerExclusive(range.add(BigInteger.ONE));
    }

}
