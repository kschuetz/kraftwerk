package software.kes.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A range of {@link BigInteger}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link BigIntegerRange#inclusive}, {@link BigIntegerRange#exclusive}),
 * <p>
 * or by using {@link BigIntegerRange#from}:
 * <pre>
 * BigIntegerRange.from(BigInteger.ZERO).to(BigInteger.TWO)      // 0..2 inclusive
 * BigIntegerRange.from(BigInteger.ZERO).until(BigInteger.TWO)   // 0..2 exclusive
 * </pre>
 */
public final class BigIntegerRange implements Constraint<BigInteger>, Iterable<BigInteger> {
    private final BigInteger minInclusive;
    private final BigInteger maxExclusive;

    private BigIntegerRange(BigInteger minInclusive, BigInteger maxExclusive) {
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
    }

    /**
     * Partially constructs a {@code BigIntegerRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code BigIntegerRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link BigIntegerRangeFrom}
     */
    public static BigIntegerRangeFrom from(BigInteger minInclusive) {
        return new BigIntegerRangeFrom() {
            @Override
            public BigIntegerRange to(BigInteger maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public BigIntegerRange until(BigInteger maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code BigIntegerRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static BigIntegerRange inclusive(BigInteger minInclusive, BigInteger maxInclusive) {
        RangeInputValidation.validateRangeInclusive(minInclusive, maxInclusive);
        return new BigIntegerRange(minInclusive, maxInclusive.add(BigInteger.ONE));
    }

    /**
     * Creates a {@code BigIntegerRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static BigIntegerRange exclusive(BigInteger minInclusive, BigInteger maxExclusive) {
        RangeInputValidation.validateRangeExclusive(minInclusive, maxExclusive);
        return new BigIntegerRange(minInclusive, maxExclusive);
    }

    /**
     * Creates a {@code BigIntegerRange} from {@code 0}..{@code maxExclusive}.
     */
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

    /**
     * Creates a new {@code BigIntegerRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code BigIntegerRange}
     */
    public BigIntegerRange withMinInclusive(BigInteger minInclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    /**
     * Creates a new {@code BigIntegerRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code BigIntegerRange}
     */
    public BigIntegerRange withMaxInclusive(BigInteger maxInclusive) {
        return exclusive(minInclusive, maxInclusive.add(BigInteger.ONE));
    }

    /**
     * Creates a new {@code BigIntegerRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code BigIntegerRange}
     */
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
        return RangeToString.rangeToString(getClass().getSimpleName(), minInclusive, true, maxExclusive, false);
    }

    /**
     * A partially constructed {@link BigIntegerRange}, with the lower bound already provided.
     */
    public interface BigIntegerRangeFrom {
        /**
         * Creates a {@link BigIntegerRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code BigIntegerRange}
         */
        BigIntegerRange to(BigInteger maxInclusive);

        /**
         * Creates a {@link BigIntegerRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code BigIntegerRange}
         */
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
