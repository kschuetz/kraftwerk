package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRange;
import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteByteRange.concreteByteRangeInclusive;

public interface ByteRange {
    byte min();

    byte max();

    default boolean contains(byte n) {
        return n >= min() && n <= max();
    }

    default ByteRange withMin(byte min) {
        return concreteByteRangeInclusive(min, max());
    }

    default ByteRange withMax(byte max) {
        return concreteByteRangeInclusive(min(), max);
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
