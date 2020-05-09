package dev.marksman.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class CharRange implements Constraint<Character>, Iterable<Character> {
    private static final CharRange FULL = new CharRange(Character.MIN_VALUE, Character.MAX_VALUE);

    private final char minInclusive;
    private final char maxInclusive;

    private CharRange(char minInclusive, char maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static CharRange inclusive(char minInclusive, char maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new CharRange(minInclusive, maxInclusive);
    }

    public static CharRange exclusive(char minInclusive, char maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new CharRange(minInclusive, (char) (maxExclusive - 1));
    }

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

    public CharRange withMinInclusive(char minInclusive) {
        char max = maxInclusive;
        validateRangeInclusive(minInclusive, max);
        return new CharRange(minInclusive, max);
    }

    public CharRange withMaxInclusive(char maxInclusive) {
        char min = minInclusive;
        validateRangeInclusive(min, maxInclusive);
        return new CharRange(min, maxInclusive);
    }

    public CharRange withMaxExclusive(char maxExclusive) {
        char min = minInclusive;
        validateRangeExclusive(min, maxExclusive);
        return new CharRange(min, (char) (maxExclusive - 1));
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CharRange)) return false;
        final CharRange other = (CharRange) o;
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

    public interface CharRangeFrom {
        CharRange to(char maxInclusive);

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
