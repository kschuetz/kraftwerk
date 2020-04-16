package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class ByteRange implements Constraint<Byte> {
    private static final ByteRange FULL = new ByteRange(Byte.MIN_VALUE, Byte.MAX_VALUE);

    private final byte minInclusive;
    private final byte maxInclusive;

    private ByteRange(byte minInclusive, byte maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static ByteRange inclusive(byte minInclusive, byte maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new ByteRange(minInclusive, maxInclusive);
    }

    public static ByteRange exclusive(byte minInclusive, byte maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new ByteRange(minInclusive, (byte) (maxExclusive - 1));
    }

    public static ByteRange exclusive(byte maxExclusive) {
        validateRangeExclusive((byte) 0, maxExclusive);
        return new ByteRange((byte) 0, (byte) (maxExclusive - 1));
    }

    public static ByteRange fullRange() {
        return FULL;
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

    public ByteRange withMinInclusive(byte minInclusive) {
        byte max = maxInclusive;
        validateRangeInclusive(minInclusive, max);
        return new ByteRange(minInclusive, max);
    }

    public ByteRange withMaxInclusive(byte maxInclusive) {
        byte min = minInclusive;
        validateRangeInclusive(min, maxInclusive);
        return new ByteRange(min, maxInclusive);
    }

    public ByteRange withMaxExclusive(byte maxExclusive) {
        byte min = minInclusive;
        validateRangeExclusive(min, maxExclusive);
        return new ByteRange(min, (byte) (maxExclusive - 1));
    }

    public interface ByteRangeFrom {
        ByteRange to(byte maxInclusive);

        ByteRange until(byte maxExclusive);
    }

}
