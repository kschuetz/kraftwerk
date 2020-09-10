package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@code short}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link ShortRange#inclusive}, {@link ShortRange#exclusive}),
 * <p>
 * or by using {@link ShortRange#from}:
 * <pre>
 * ShortRange.from((short) 0).to((short) 10)      // inclusive upper bound
 * ShortRange.from((short) 0).until((short) 10)   // exclusive upper bound
 * </pre>
 */
public final class ShortRange implements Constraint<Short>, Iterable<Short> {
    private static final ShortRange FULL = new ShortRange(Short.MIN_VALUE, Short.MAX_VALUE);

    private final short minInclusive;
    private final short maxInclusive;

    private ShortRange(short minInclusive, short maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code ShortRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code ShortRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link ShortRangeFrom}
     */
    public static ShortRangeFrom from(short minInclusive) {
        return new ShortRangeFrom() {
            @Override
            public ShortRange to(short maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public ShortRange until(short maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code ShortRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static ShortRange inclusive(short minInclusive, short maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new ShortRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code ShortRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static ShortRange exclusive(short minInclusive, short maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new ShortRange(minInclusive, (short) (maxExclusive - 1));
    }

    /**
     * Creates a {@code ShortRange} from {@code 0}..{@code maxExclusive}.
     */
    public static ShortRange exclusive(short maxExclusive) {
        return exclusive((short) 0, maxExclusive);
    }

    /**
     * Creates a {@code ShortRange} that includes all possible {@code short}s ({@link Short#MIN_VALUE}..{@link Short#MAX_VALUE}).
     */
    public static ShortRange fullRange() {
        return FULL;
    }

    @Override
    public Iterator<Short> iterator() {
        return new ShortRangeIterator(minInclusive, maxInclusive);
    }

    public short minInclusive() {
        return minInclusive;
    }

    public short maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Short n) {
        return n >= minInclusive && n <= maxInclusive;
    }

    /**
     * Creates a new {@code ShortRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code ShortRange}
     */
    public ShortRange withMinInclusive(short minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code ShortRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code ShortRange}
     */
    public ShortRange withMaxInclusive(short maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code ShortRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code ShortRange}
     */
    public ShortRange withMaxExclusive(short maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortRange shorts = (ShortRange) o;

        if (minInclusive != shorts.minInclusive) return false;
        return maxInclusive == shorts.maxInclusive;
    }

    @Override
    public int hashCode() {
        int result = minInclusive;
        result = 31 * result + (int) maxInclusive;
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    /**
     * A partially constructed {@link ShortRange}, with the lower bound already provided.
     */
    public interface ShortRangeFrom {
        /**
         * Creates a {@link ShortRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code ShortRange}
         */
        ShortRange to(short maxInclusive);

        /**
         * Creates a {@link ShortRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code ShortRange}
         */
        ShortRange until(short maxExclusive);
    }

    private static class ShortRangeIterator implements Iterator<Short> {
        private final short max;
        private short current;
        private boolean exhausted;

        public ShortRangeIterator(short current, short max) {
            this.current = current;
            this.max = max;
            exhausted = current > max;
        }

        @Override
        public boolean hasNext() {
            return !exhausted;
        }

        @Override
        public Short next() {
            if (exhausted) {
                throw new NoSuchElementException();
            }
            short result = current;
            if (current >= max) {
                exhausted = true;
            } else {
                current += 1;
            }
            return result;
        }
    }
}
