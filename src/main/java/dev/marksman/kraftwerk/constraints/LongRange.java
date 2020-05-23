package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class LongRange implements Constraint<Long>, Iterable<Long> {
    private static final LongRange FULL = new LongRange(Long.MIN_VALUE, Long.MAX_VALUE);

    private final long minInclusive;
    private final long maxInclusive;

    private LongRange(long minInclusive, long maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static LongRange inclusive(long minInclusive, long maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new LongRange(minInclusive, maxInclusive);
    }

    public static LongRange exclusive(long minInclusive, long maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new LongRange(minInclusive, maxExclusive - 1);
    }

    public static LongRange exclusive(long maxExclusive) {
        return exclusive(0, maxExclusive);
    }

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

    public LongRange withMinInclusive(long minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public LongRange withMaxInclusive(long maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

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
        return rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    public interface LongRangeFrom {
        LongRange to(long maxInclusive);

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
