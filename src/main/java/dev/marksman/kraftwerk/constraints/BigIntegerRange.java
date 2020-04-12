package dev.marksman.kraftwerk.constraints;

import java.math.BigInteger;

import static dev.marksman.kraftwerk.constraints.ConcreteBigIntegerRange.*;

public interface BigIntegerRange {
    BigInteger min();

    BigInteger maxExclusive();

    default boolean contains(BigInteger n) {
        return n.compareTo(min()) >= 0 && n.compareTo(maxExclusive()) < 0;
    }

    default BigIntegerRange withMin(BigInteger min) {
        return concreteBigIntegerRangeExclusive(min, maxExclusive());
    }

    default BigIntegerRange withMaxExclusive(BigInteger maxExclusive) {
        return concreteBigIntegerRangeExclusive(min(), maxExclusive);
    }

    static BigIntegerRangeFrom from(BigInteger min) {
        return concreteBigIntegerRangeFrom(min);
    }

    static BigIntegerRange inclusive(BigInteger min, BigInteger max) {
        return concreteBigIntegerRangeInclusive(min, max);
    }

    static BigIntegerRange exclusive(BigInteger min, BigInteger maxExclusive) {
        return concreteBigIntegerRangeExclusive(min, maxExclusive);
    }

    static BigIntegerRange exclusive(BigInteger maxExclusive) {
        return concreteBigIntegerRangeExclusive(BigInteger.ZERO, maxExclusive);
    }

    interface BigIntegerRangeFrom {
        BigIntegerRange to(BigInteger max);

        BigIntegerRange until(BigInteger maxExclusive);
    }
}
