package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class BigIntegerRange implements Constraint<BigInteger>, Iterable<BigInteger> {
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

    @Override
    public Iterator<BigInteger> iterator() {
        return new BigIntegerRangeIterator(minInclusive, maxExclusive);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigIntegerRange that = (BigIntegerRange) o;

        if (!minInclusive.equals(that.minInclusive)) return false;
        return maxExclusive.equals(that.maxExclusive);
    }

    @Override
    public int hashCode() {
        int result = minInclusive.hashCode();
        result = 31 * result + maxExclusive.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), minInclusive, true, maxExclusive, false);
    }

    public interface BigIntegerRangeFrom {
        BigIntegerRange to(BigInteger maxInclusive);

        BigIntegerRange until(BigInteger maxExclusive);
    }

    private static class BigIntegerRangeIterator implements Iterator<BigInteger> {
        private final BigInteger maxExclusive;
        private BigInteger current;

        public BigIntegerRangeIterator(BigInteger current, BigInteger maxExclusive) {
            this.current = current;
            this.maxExclusive = maxExclusive;
        }

        @Override
        public boolean hasNext() {
            return current.compareTo(maxExclusive) < 0;
        }

        @Override
        public BigInteger next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            BigInteger result = current;
            current = current.add(BigInteger.ONE);
            return result;
        }
    }
}
