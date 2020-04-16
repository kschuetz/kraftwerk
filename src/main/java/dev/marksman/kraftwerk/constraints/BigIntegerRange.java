package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;

import java.math.BigInteger;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class BigIntegerRange implements Constraint<BigInteger> {
    private final BigInteger minInclusive;
    private final BigInteger maxExclusive;

    private BigIntegerRange(BigInteger minInclusive, BigInteger maxExclusive) {
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
    }

    public static BigIntegerRangeFrom from(BigInteger min) {
        return new BigIntegerRangeFrom() {
            @Override
            public BigIntegerRange to(BigInteger maxInclusive) {
                return inclusive(min, maxInclusive);
            }

            @Override
            public BigIntegerRange until(BigInteger maxExclusive1) {
                return exclusive(min, maxExclusive1);
            }
        };
    }

    public static BigIntegerRange inclusive(BigInteger minInclusive, BigInteger maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new BigIntegerRange(minInclusive, maxInclusive.add(BigInteger.ONE));
    }

    public static BigIntegerRange exclusive(BigInteger minInclusive, BigInteger maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new BigIntegerRange(minInclusive, maxExclusive);
    }

    public static BigIntegerRange exclusive(BigInteger maxExclusive) {
        return exclusive(BigInteger.ZERO, maxExclusive);
    }

    public BigInteger minInclusive() {
        return minInclusive;
    }

    public BigInteger maxExclusive() {
        return maxExclusive;
    }

    @Override
    public boolean includes(BigInteger value) {
        return GTE.gte(minInclusive, value) && LT.lt(maxExclusive, value);
    }

    public BigIntegerRange withMinInclusive(BigInteger minInclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    public BigIntegerRange withMaxInclusive(BigInteger maxInclusive) {
        return exclusive(minInclusive, maxInclusive.add(BigInteger.ONE));
    }

    public BigIntegerRange withMaxExclusive(BigInteger maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    public interface BigIntegerRangeFrom {
        BigIntegerRange to(BigInteger maxInclusive);

        BigIntegerRange until(BigInteger maxExclusive);
    }

}
