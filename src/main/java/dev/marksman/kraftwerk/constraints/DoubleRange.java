package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteDoubleRange.concreteDoubleRange;
import static dev.marksman.kraftwerk.constraints.ConcreteDoubleRange.concreteDoubleRangeFrom;

public interface DoubleRange {
    double min();

    double max();

    boolean minIncluded();

    boolean maxIncluded();

    default double minInclusive() {
        if (minIncluded()) {
            return min();
        } else {
            return Math.nextAfter(min(), Double.POSITIVE_INFINITY);
        }
    }

    default double maxInclusive() {
        if (maxIncluded()) {
            return max();
        } else {
            return Math.nextAfter(max(), Double.NEGATIVE_INFINITY);
        }
    }

    default boolean contains(double n) {
        return (minIncluded() ? n >= min() : n > min()) &&
                (maxIncluded() ? n <= max() : n < max());
    }

    default DoubleRange withMinInclusive(double min) {
        return concreteDoubleRange(min, true, max(), maxIncluded());
    }

    default DoubleRange withMaxInclusive(double max) {
        return concreteDoubleRange(min(), minIncluded(), max, true);
    }

    default DoubleRange withMinExclusive(double minExclusive) {
        return concreteDoubleRange(minExclusive, false, max(), maxIncluded());
    }

    default DoubleRange withMaxExclusive(double maxExclusive) {
        return concreteDoubleRange(min(), minIncluded(), maxExclusive, false);
    }

    static DoubleRangeFrom from(double min) {
        return concreteDoubleRangeFrom(min, true);
    }

    static DoubleRangeFrom fromExclusive(double min) {
        return concreteDoubleRangeFrom(min, false);
    }

    static DoubleRange inclusive(double min, double max) {
        return concreteDoubleRange(min, true, max, true);
    }

    static DoubleRange exclusive(double min, double maxExclusive) {
        return concreteDoubleRange(min, true, maxExclusive, false);
    }

    static DoubleRange exclusive(int maxExclusive) {
        return concreteDoubleRange(0.0d, true, maxExclusive, false);
    }

    static DoubleRange doubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        return concreteDoubleRange(min, minIncluded, max, maxIncluded);
    }

    static DoubleRange fullRange() {
        return concreteDoubleRange();
    }

    interface DoubleRangeFrom {
        DoubleRange to(double max);

        DoubleRange until(double maxExclusive);
    }

}
