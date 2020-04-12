package dev.marksman.kraftwerk.constraints;

import java.math.BigInteger;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

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
        validateRangeInclusive(min, max);
        return new ConcreteBigIntegerRange(min, max.add(BigInteger.ONE));
    }

    static BigIntegerRange concreteBigIntegerRangeExclusive(BigInteger min, BigInteger maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
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
