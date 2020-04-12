package dev.marksman.kraftwerk.constraints;

final class ConcreteByteRange implements ByteRange {
    private static final ByteRange FULL = new ConcreteByteRange(Byte.MIN_VALUE, Byte.MAX_VALUE);

    private final byte min;
    private final byte max;

    ConcreteByteRange(byte min, byte max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public byte min() {
        return min;
    }

    @Override
    public byte max() {
        return max;
    }

    static ByteRange concreteByteRange() {
        return FULL;
    }

    static ByteRange concreteByteRangeInclusive(byte min, byte max) {
        if (max < min) {
            throw new IllegalArgumentException("max must be >= min");
        }
        return new ConcreteByteRange(min, max);
    }

    static ByteRange concreteByteRangeExclusive(byte min, byte maxExclusive) {
        if (maxExclusive <= min) {
            throw new IllegalArgumentException("max must be > min");
        }
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
