package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BigIntegerRange)) return false;
        final BigIntegerRange other = (BigIntegerRange) o;
        if (!Objects.equals(this.minInclusive, other.minInclusive))
            return false;
        return Objects.equals(this.maxExclusive, other.maxExclusive);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $minInclusive = this.minInclusive;
        result = result * PRIME + ($minInclusive == null ? 43 : $minInclusive.hashCode());
        final Object $maxExclusive = this.maxExclusive;
        result = result * PRIME + ($maxExclusive == null ? 43 : $maxExclusive.hashCode());
        return result;
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
