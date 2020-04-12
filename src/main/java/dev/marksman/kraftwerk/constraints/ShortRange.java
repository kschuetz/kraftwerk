package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteShortRange.concreteShortRange;
import static dev.marksman.kraftwerk.constraints.ConcreteShortRange.concreteShortRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteShortRange.concreteShortRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteShortRange.concreteShortRangeInclusive;

public interface ShortRange extends Constraint<Short> {
    short minInclusive();

    short maxInclusive();

    @Override
    default boolean includes(Short n) {
        return n >= minInclusive() && n <= maxInclusive();
    }

    default ShortRange withMin(short min) {
        return concreteShortRangeInclusive(min, maxInclusive());
    }

    default ShortRange withMaxInclusive(short max) {
        return concreteShortRangeInclusive(minInclusive(), max);
    }

    default ShortRange withMaxExclusive(short maxExclusive) {
        return concreteShortRangeExclusive(minInclusive(), maxExclusive);
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
