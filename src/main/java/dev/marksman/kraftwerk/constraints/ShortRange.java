package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class ShortRange implements Constraint<Short>, Iterable<Short> {
    private static final ShortRange FULL = new ShortRange(Short.MIN_VALUE, Short.MAX_VALUE);

    private final short minInclusive;
    private final short maxInclusive;

    private ShortRange(short minInclusive, short maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static ShortRange inclusive(short minInclusive, short maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new ShortRange(minInclusive, maxInclusive);
    }

    public static ShortRange exclusive(short maxExclusive) {
        return exclusive((short) 0, maxExclusive);
    }

    public static ShortRange exclusive(short minInclusive, short maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new ShortRange(minInclusive, (short) (maxExclusive - 1));
    }

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

    public ShortRange withMinInclusive(short minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public ShortRange withMaxInclusive(short maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

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

    public interface ShortRangeFrom {
        ShortRange to(short maxInclusive);

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
