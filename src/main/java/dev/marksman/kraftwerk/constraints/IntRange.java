package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRange;
import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteIntRange.concreteIntRangeInclusive;

public interface IntRange {
    int min();

    int max();

    default boolean contains(int n) {
        return n >= min() && n <= max();
    }

    default IntRange withMin(int min) {
        return concreteIntRangeInclusive(min, max());
    }

    default IntRange withMax(int max) {
        return concreteIntRangeInclusive(min(), max);
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
