package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteShortRange.*;

public interface ShortRange {
    short min();

    short max();

    default boolean contains(short n) {
        return n >= min() && n <= max();
    }

    default ShortRange withMin(short min) {
        return concreteShortRangeInclusive(min, max());
    }

    default ShortRange withMax(short max) {
        return concreteShortRangeInclusive(min(), max);
    }

    static ShortRangeFrom from(short min) {
        return concreteShortRangeFrom(min);
    }

    static ShortRange inclusive(short min, short max) {
        return concreteShortRangeInclusive(min, max);
    }

    static ShortRange exclusive(short min, short maxExclusive) {
        return concreteShortRangeExclusive(min, maxExclusive);
    }

    static ShortRange exclusive(short maxExclusive) {
        return concreteShortRangeExclusive((short) 0, maxExclusive);
    }

    static ShortRange fullRange() {
        return concreteShortRange();
    }

    interface ShortRangeFrom {
        ShortRange to(short max);

        ShortRange until(short maxExclusive);
    }
}
