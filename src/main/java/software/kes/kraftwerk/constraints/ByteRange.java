package software.kes.kraftwerk.constraints;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A range of {@code byte}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link ByteRange#inclusive}, {@link ByteRange#exclusive}),
 * <p>
 * or by using {@link ByteRange#from}:
 * <pre>
 * ByteRange.from((byte) 0).to((byte) 10)      // inclusive upper bound
 * ByteRange.from((byte) 0).until((byte) 10)   // exclusive upper bound
 * </pre>
 */
public final class ByteRange implements Constraint<Byte>, Iterable<Byte> {
    private static final ByteRange FULL = new ByteRange(Byte.MIN_VALUE, Byte.MAX_VALUE);

    private final byte minInclusive;
    private final byte maxInclusive;

    private ByteRange(byte minInclusive, byte maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code ByteRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code ByteRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link ByteRangeFrom}
     */
    public static ByteRangeFrom from(byte minInclusive) {
        return new ByteRangeFrom() {
            @Override
            public ByteRange to(byte maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public ByteRange until(byte maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code ByteRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static ByteRange inclusive(byte minInclusive, byte maxInclusive) {
        RangeInputValidation.validateRangeInclusive(minInclusive, maxInclusive);
        return new ByteRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code ByteRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static ByteRange exclusive(byte minInclusive, byte maxExclusive) {
        RangeInputValidation.validateRangeExclusive(minInclusive, maxExclusive);
        return new ByteRange(minInclusive, (byte) (maxExclusive - 1));
    }

    /**
     * Creates a {@code ByteRange} from {@code 0}..{@code maxExclusive}.
     */
    public static ByteRange exclusive(byte maxExclusive) {
        RangeInputValidation.validateRangeExclusive((byte) 0, maxExclusive);
        return new ByteRange((byte) 0, (byte) (maxExclusive - 1));
    }

    /**
     * Creates a {@code ByteRange} that includes all possible {@code byte}s ({@link Byte#MIN_VALUE}..{@link Byte#MAX_VALUE}).
     */
    public static ByteRange fullRange() {
        return FULL;
    }

    @Override
    public Iterator<Byte> iterator() {
        return new ByteRangeIterator(minInclusive, maxInclusive);
    }

    public byte minInclusive() {
        return minInclusive;
    }

    public byte maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Byte value) {
        return value >= minInclusive && value <= maxInclusive;
    }

    /**
     * Creates a new {@code ByteRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code ByteRange}
     */
    public ByteRange withMinInclusive(byte minInclusive) {
        byte max = maxInclusive;
        RangeInputValidation.validateRangeInclusive(minInclusive, max);
        return new ByteRange(minInclusive, max);
    }

    /**
     * Creates a new {@code ByteRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code ByteRange}
     */
    public ByteRange withMaxInclusive(byte maxInclusive) {
        byte min = minInclusive;
        RangeInputValidation.validateRangeInclusive(min, maxInclusive);
        return new ByteRange(min, maxInclusive);
    }

    /**
     * Creates a new {@code ByteRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code ByteRange}
     */
    public ByteRange withMaxExclusive(byte maxExclusive) {
        byte min = minInclusive;
        RangeInputValidation.validateRangeExclusive(min, maxExclusive);
        return new ByteRange(min, (byte) (maxExclusive - 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByteRange bytes = (ByteRange) o;

        if (minInclusive != bytes.minInclusive) return false;
        return maxInclusive == bytes.maxInclusive;
    }

    @Override
    public int hashCode() {
        int result = minInclusive;
        result = 31 * result + (int) maxInclusive;
        return result;
    }

    @Override
    public String toString() {
        return RangeToString.rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    /**
     * A partially constructed {@link ByteRange}, with the lower bound already provided.
     */
    public interface ByteRangeFrom {
        /**
         * Creates a {@link ByteRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code ByteRange}
         */
        ByteRange to(byte maxInclusive);

        /**
         * Creates a {@link ByteRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code ByteRange}
         */
        ByteRange until(byte maxExclusive);
    }

    private static class ByteRangeIterator implements Iterator<Byte> {
        private final byte max;
        private byte current;
        private boolean exhausted;

        public ByteRangeIterator(byte current, byte max) {
            this.current = current;
            this.max = max;
            exhausted = current > max;
        }

        @Override
        public boolean hasNext() {
            return !exhausted;
        }

        @Override
        public Byte next() {
            if (exhausted) {
                throw new NoSuchElementException();
            }
            byte result = current;
            if (current >= max) {
                exhausted = true;
            } else {
                current += 1;
            }
            return result;
        }
    }
}
