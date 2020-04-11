package dev.marksman.kraftwerk.constraints;

import java.math.BigInteger;

final class ConcreteBigIntegerRange implements BigIntegerRange {
    private final BigInteger min;
    private final BigInteger maxExclusive;

    ConcreteBigIntegerRange(BigInteger min, BigInteger maxExclusive) {
        this.min = min;
        this.maxExclusive = maxExclusive;
    }

    @Override
    public BigInteger min() {
        return min;
    }

    @Override
    public BigInteger maxExclusive() {
        return maxExclusive;
    }

    static BigIntegerRange concreteBigIntegerRangeInclusive(BigInteger min, BigInteger max) {
        if (max.compareTo(min) < 0) {
            throw new IllegalArgumentException("max must be >= min");
        }
        return new ConcreteBigIntegerRange(min, max.add(BigInteger.ONE));
    }

    static BigIntegerRange concreteBigIntegerRangeExclusive(BigInteger min, BigInteger maxExclusive) {
        if (maxExclusive.compareTo(min) <= 0) {
            throw new IllegalArgumentException("max must be > min");
        }
        return new ConcreteBigIntegerRange(min, maxExclusive);
    }

    static BigIntegerRangeFrom concreteBigIntegerRangeFrom(BigInteger min) {
        return new BigIntegerRangeFrom() {
            @Override
            public BigIntegerRange to(BigInteger max) {
                return concreteBigIntegerRangeInclusive(min, max);
            }

            @Override
            public BigIntegerRange until(BigInteger maxExclusive) {
                return concreteBigIntegerRangeExclusive(min, maxExclusive);
            }
        };
    }

}
