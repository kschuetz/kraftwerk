package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRange;
import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRangeInclusive;

public interface ByteRange extends Constraint<Byte> {
    byte minInclusive();

    byte maxInclusive();

    @Override
    default boolean includes(Byte value) {
        return value >= minInclusive() && value <= maxInclusive();
    }

    default ByteRange withMin(byte min) {
        return concreteByteRangeInclusive(min, maxInclusive());
    }

    default ByteRange withMaxInclusive(byte max) {
        return concreteByteRangeInclusive(minInclusive(), max);
    }

    default ByteRange withMaxExclusive(byte max) {
        return concreteByteRangeExclusive(minInclusive(), max);
    }

    static ByteRangeFrom from(byte min) {
        return concreteByteRangeFrom(min);
    }

    static ByteRange inclusive(byte min, byte max) {
        return concreteByteRangeInclusive(min, max);
    }

    static ByteRange exclusive(byte min, byte maxExclusive) {
        return concreteByteRangeExclusive(min, maxExclusive);
    }

    static ByteRange exclusive(byte maxExclusive) {
        return concreteByteRangeExclusive((byte) 0, maxExclusive);
    }

    static ByteRange fullRange() {
        return concreteByteRange();
    }

    interface ByteRangeFrom {
        ByteRange to(byte max);

        ByteRange until(byte maxExclusive);
    }
}
