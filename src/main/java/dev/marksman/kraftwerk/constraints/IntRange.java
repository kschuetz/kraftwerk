package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRange;
import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRangeInclusive;

public interface IntRange {
    int minInclusive();

    int maxInclusive();

    default boolean contains(int n) {
        return n >= minInclusive() && n <= maxInclusive();
    }

    default IntRange withMin(int min) {
        return concreteIntRangeInclusive(min, maxInclusive());
    }

    default IntRange withMaxInclusive(int max) {
        return concreteIntRangeInclusive(minInclusive(), max);
    }

    default IntRange withMaxExclusive(int maxExclusive) {
        return concreteIntRangeExclusive(minInclusive(), maxExclusive);
    }

    static IntRangeFrom from(int min) {
        return concreteIntRangeFrom(min);
    }

    static IntRange inclusive(int min, int max) {
        return concreteIntRangeInclusive(min, max);
    }

    static IntRange exclusive(int min, int maxExclusive) {
        return concreteIntRangeExclusive(min, maxExclusive);
    }

    static IntRange exclusive(int maxExclusive) {
        return concreteIntRangeExclusive(0, maxExclusive);
    }

    static IntRange fullRange() {
        return concreteIntRange();
    }

    interface IntRangeFrom {
        IntRange to(int max);

        IntRange until(int maxExclusive);
    }
}
