package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteByteRange implements ByteRange {
    private static final ByteRange FULL = new ConcreteByteRange(Byte.MIN_VALUE, Byte.MAX_VALUE);

    private final byte min;
    private final byte max;

    ConcreteByteRange(byte min, byte max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public byte minInclusive() {
        return min;
    }

    @Override
    public byte maxInclusive() {
        return max;
    }

    static ByteRange concreteByteRange() {
        return FULL;
    }

    static ByteRange concreteByteRangeInclusive(byte min, byte max) {
        validateRangeInclusive(min, max);
        return new ConcreteByteRange(min, max);
    }

    static ByteRange concreteByteRangeExclusive(byte min, byte maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
        return new ConcreteByteRange(min, (byte) (maxExclusive - 1));
    }

    static ByteRangeFrom concreteByteRangeFrom(byte min) {
        return new ByteRangeFrom() {
            @Override
            public ByteRange to(byte max) {
                return concreteByteRangeInclusive(min, max);
            }

            @Override
            public ByteRange until(byte maxExclusive) {
                return concreteByteRangeExclusive(min, maxExclusive);
            }
        };
    }

}
