package dev.marksman.kraftwerk.constraints;

import java.math.BigInteger;

import static dev.marksman.kraftwerk.constraints.ConcreteBigIntegerRange.concreteBigIntegerRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteBigIntegerRange.concreteBigIntegerRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteBigIntegerRange.concreteBigIntegerRangeInclusive;

public interface BigIntegerRange extends Constraint<BigInteger> {
    BigInteger min();

    BigInteger maxExclusive();

    @Override
    default boolean includes(BigInteger value) {
        return value.compareTo(min()) >= 0 && value.compareTo(maxExclusive()) < 0;
    }

    default BigIntegerRange withMin(BigInteger min) {
        return concreteBigIntegerRangeExclusive(min, maxExclusive());
    }

    default BigIntegerRange withMaxInclusive(BigInteger max) {
        return concreteBigIntegerRangeExclusive(min(), max.add(BigInteger.ONE));
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
