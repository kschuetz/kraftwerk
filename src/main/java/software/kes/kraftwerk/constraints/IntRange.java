package software.kes.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static software.kes.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@code int}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link IntRange#inclusive}, {@link IntRange#exclusive}),
 * <p>
 * or by using {@link IntRange#from}:
 * <pre>
 * IntRange.from(0).to(10)      // inclusive upper bound
 * IntRange.from(0).until(10)   // exclusive upper bound
 * </pre>
 */
public final class IntRange implements Constraint<Integer>, Iterable<Integer> {
    private static final IntRange FULL = new IntRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

    private final int minInclusive;
    private final int maxInclusive;

    private IntRange(int minInclusive, int maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs an {@code IntRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code IntRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return an {@link IntRangeFrom}
     */
    public static IntRangeFrom from(int minInclusive) {
        return new IntRangeFrom() {
            @Override
            public IntRange to(int maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public IntRange until(int maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates an {@code IntRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static IntRange inclusive(int minInclusive, int maxInclusive) {
        RangeInputValidation.validateRangeInclusive(minInclusive, maxInclusive);
        return new IntRange(minInclusive, maxInclusive);
    }

    /**
     * Creates an {@code IntRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static IntRange exclusive(int minInclusive, int maxExclusive) {
        RangeInputValidation.validateRangeExclusive(minInclusive, maxExclusive);
        return new IntRange(minInclusive, maxExclusive - 1);
    }

    /**
     * Creates an {@code IntRange} from {@code 0}..{@code maxExclusive}.
     */
    public static IntRange exclusive(int maxExclusive) {
        return exclusive(0, maxExclusive);
    }

    /**
     * Creates an {@code IntRange} that includes all possible {@code int}s ({@link Integer#MIN_VALUE}..{@link Integer#MAX_VALUE}).
     */
    public static IntRange fullRange() {
        return FULL;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntRangeIterator(minInclusive, maxInclusive);
    }

    public int minInclusive() {
        return minInclusive;
    }

    public int maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Integer value) {
        return value >= minInclusive && value <= maxInclusive;
    }

    /**
     * Creates a new {@code IntRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code IntRange}
     */
    public IntRange withMinInclusive(int minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code IntRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code IntRange}
     */
    public IntRange withMaxInclusive(int maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code IntRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code IntRange}
     */
    public IntRange withMaxExclusive(int maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntRange integers = (IntRange) o;

        if (minInclusive != integers.minInclusive) return false;
        return maxInclusive == integers.maxInclusive;
    }

    @Override
    public int hashCode() {
        int result = minInclusive;
        result = 31 * result + maxInclusive;
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    /**
     * A partially constructed {@link IntRange}, with the lower bound already provided.
     */
    public interface IntRangeFrom {
        /**
         * Creates an {@link IntRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return an {@code IntRange}
         */
        IntRange to(int maxInclusive);

        /**
         * Creates an {@link IntRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return an {@code IntRange}
         */
        IntRange until(int maxExclusive);
    }

    private static class IntRangeIterator implements Iterator<Integer> {
        private final int max;
        private int current;
        private boolean exhausted;

        public IntRangeIterator(int current, int max) {
            this.current = current;
            this.max = max;
            exhausted = current > max;
        }

        @Override
        public boolean hasNext() {
            return !exhausted;
        }

        @Override
        public Integer next() {
            if (exhausted) {
                throw new NoSuchElementException();
            }
            int result = current;
            if (current >= max) {
                exhausted = true;
            } else {
                current += 1;
            }
            return result;
        }
    }
}
