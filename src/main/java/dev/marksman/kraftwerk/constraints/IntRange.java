package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class IntRange implements Constraint<Integer>, Iterable<Integer> {
    private static final IntRange FULL = new IntRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

    private final int minInclusive;
    private final int maxInclusive;

    private IntRange(int minInclusive, int maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static IntRange inclusive(int minInclusive, int maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new IntRange(minInclusive, maxInclusive);
    }

    public static IntRange exclusive(int minInclusive, int maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new IntRange(minInclusive, maxExclusive - 1);
    }

    public static IntRange exclusive(int maxExclusive) {
        return exclusive(0, maxExclusive);
    }

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

    public IntRange withMinInclusive(int minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public IntRange withMaxInclusive(int maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

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

    public interface IntRangeFrom {
        IntRange to(int maxInclusive);

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
