package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@code char}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link CharRange#inclusive}, {@link CharRange#exclusive}),
 * <p>
 * or by using {@link CharRange#from}:
 * <pre>
 * CharRange.from('a').to('z')      // 'a'..'z' inclusive
 * CharRange.from('a').until('z')   // 'a'..'z' exclusive
 * </pre>
 */
public final class CharRange implements Constraint<Character>, Iterable<Character> {
    private static final CharRange FULL = new CharRange(Character.MIN_VALUE, Character.MAX_VALUE);

    private final char minInclusive;
    private final char maxInclusive;

    private CharRange(char minInclusive, char maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code CharRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code CharRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link CharRangeFrom}
     */
    public static CharRangeFrom from(char minInclusive) {
        return new CharRangeFrom() {
            @Override
            public CharRange to(char maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public CharRange until(char maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code CharRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static CharRange inclusive(char minInclusive, char maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new CharRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code CharRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static CharRange exclusive(char minInclusive, char maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new CharRange(minInclusive, (char) (maxExclusive - 1));
    }

    /**
     * Creates a {@code CharRange} that includes all possible {@code char}s ({@link Character#MIN_VALUE}..{@link Character#MAX_VALUE}).
     */
    public static CharRange fullRange() {
        return FULL;
    }

    @Override
    public Iterator<Character> iterator() {
        return new CharRangeIterator(minInclusive, maxInclusive);
    }

    public char minInclusive() {
        return minInclusive;
    }

    public char maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Character value) {
        return value >= minInclusive && value <= maxInclusive;
    }

    /**
     * Creates a new {@code CharRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code CharRange}
     */
    public CharRange withMinInclusive(char minInclusive) {
        char max = maxInclusive;
        validateRangeInclusive(minInclusive, max);
        return new CharRange(minInclusive, max);
    }

    /**
     * Creates a new {@code CharRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code CharRange}
     */
    public CharRange withMaxInclusive(char maxInclusive) {
        char min = minInclusive;
        validateRangeInclusive(min, maxInclusive);
        return new CharRange(min, maxInclusive);
    }

    /**
     * Creates a new {@code CharRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code CharRange}
     */
    public CharRange withMaxExclusive(char maxExclusive) {
        char min = minInclusive;
        validateRangeExclusive(min, maxExclusive);
        return new CharRange(min, (char) (maxExclusive - 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CharRange that = (CharRange) o;

        if (minInclusive != that.minInclusive) return false;
        return maxInclusive == that.maxInclusive;
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
     * A partially constructed {@link CharRange}, with the lower bound already provided.
     */
    public interface CharRangeFrom {
        /**
         * Creates a {@link CharRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code CharRange}
         */
        CharRange to(char maxInclusive);

        /**
         * Creates a {@link CharRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code CharRange}
         */
        CharRange until(char maxExclusive);
    }

    private static class CharRangeIterator implements Iterator<Character> {
        private final char max;
        private char current;
        private boolean exhausted;

        public CharRangeIterator(char current, char max) {
            this.current = current;
            this.max = max;
            exhausted = current > max;
        }

        @Override
        public boolean hasNext() {
            return !exhausted;
        }

        @Override
        public Character next() {
            if (exhausted) {
                throw new NoSuchElementException();
            }
            char result = current;
            if (current >= max) {
                exhausted = true;
            } else {
                current += 1;
            }
            return result;
        }
    }
}
