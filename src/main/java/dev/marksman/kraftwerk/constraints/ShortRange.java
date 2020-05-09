package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ShortRange)) return false;
        final ShortRange other = (ShortRange) o;
        if (this.minInclusive != other.minInclusive) return false;
        return this.maxInclusive == other.maxInclusive;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.minInclusive;
        result = result * PRIME + this.maxInclusive;
        return result;
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
