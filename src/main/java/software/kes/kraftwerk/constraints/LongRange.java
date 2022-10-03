package software.kes.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A range of {@code long}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link LongRange#inclusive}, {@link LongRange#exclusive}),
 * <p>
 * or by using {@link LongRange#from}:
 * <pre>
 * LongRange.from(0).to(10)      // inclusive upper bound
 * LongRange.from(0).until(10)   // exclusive upper bound
 * </pre>
 */
public final class LongRange implements Constraint<Long>, Iterable<Long> {
    private static final LongRange FULL = new LongRange(Long.MIN_VALUE, Long.MAX_VALUE);

    private final long minInclusive;
    private final long maxInclusive;

    private LongRange(long minInclusive, long maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code LongRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code LongRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link LongRangeFrom}
     */
    public static LongRangeFrom from(long minInclusive) {
        return new LongRangeFrom() {
            @Override
            public LongRange to(long maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }


            @Override
            public LongRange until(long maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code LongRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static LongRange inclusive(long minInclusive, long maxInclusive) {
        RangeInputValidation.validateRangeInclusive(minInclusive, maxInclusive);
        return new LongRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code LongRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static LongRange exclusive(long minInclusive, long maxExclusive) {
        RangeInputValidation.validateRangeExclusive(minInclusive, maxExclusive);
        return new LongRange(minInclusive, maxExclusive - 1);
    }

    /**
     * Creates a {@code LongRange} from {@code 0}..{@code maxExclusive}.
     */
    public static LongRange exclusive(long maxExclusive) {
        return exclusive(0, maxExclusive);
    }

    /**
     * Creates a {@code LongRange} that includes all possible {@code long}s ({@link Long#MIN_VALUE}..{@link Long#MAX_VALUE}).
     */
    public static LongRange fullRange() {
        return FULL;
    }

    @Override
    public Iterator<Long> iterator() {
        return new LongRangeIterator(minInclusive, maxInclusive);
    }

    public long minInclusive() {
        return minInclusive;
    }

    public long maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Long n) {
        return n >= minInclusive && n <= maxInclusive;
    }

    /**
     * Creates a new {@code LongRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code LongRange}
     */
    public LongRange withMinInclusive(long minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code LongRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code LongRange}
     */
    public LongRange withMaxInclusive(long maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code LongRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code LongRange}
     */
    public LongRange withMaxExclusive(long maxExclusive) {
        return inclusive(minInclusive, maxExclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongRange longs = (LongRange) o;

        if (minInclusive != longs.minInclusive) return false;
        return maxInclusive == longs.maxInclusive;
    }

    @Override
    public int hashCode() {
        int result = (int) (minInclusive ^ (minInclusive >>> 32));
        result = 31 * result + (int) (maxInclusive ^ (maxInclusive >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return RangeToString.rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    /**
     * A partially constructed {@link LongRange}, with the lower bound already provided.
     */
    public interface LongRangeFrom {
        /**
         * Creates a {@link LongRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code LongRange}
         */
        LongRange to(long maxInclusive);

        /**
         * Creates a {@link LongRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code LongRange}
         */
        LongRange until(long maxExclusive);
    }

    private static class LongRangeIterator implements Iterator<Long> {
        private final long max;
        private long current;
        private boolean exhausted;

        public LongRangeIterator(long current, long max) {
            this.current = current;
            this.max = max;
            exhausted = current > max;
        }

        @Override
        public boolean hasNext() {
            return !exhausted;
        }

        @Override
        public Long next() {
            if (exhausted) {
                throw new NoSuchElementException();
            }
            long result = current;
            if (current >= max) {
                exhausted = true;
            } else {
                current += 1;
            }
            return result;
        }
    }
}
