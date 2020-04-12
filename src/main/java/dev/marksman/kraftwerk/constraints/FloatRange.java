package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteFloatRange.concreteFloatRange;
import static dev.marksman.kraftwerk.constraints.ConcreteFloatRange.concreteFloatRangeFrom;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;

public interface FloatRange extends Constraint<Float> {
    float min();

    float max();

    boolean minIncluded();

    boolean maxIncluded();

    default float minInclusive() {
        if (minIncluded()) {
            return min();
        } else {
            return Math.nextAfter(min(), Float.POSITIVE_INFINITY);
        }
    }

    default float maxInclusive() {
        if (maxIncluded()) {
            return max();
        } else {
            return Math.nextAfter(max(), Float.NEGATIVE_INFINITY);
        }
    }

    default float maxExclusive() {
        if (maxIncluded()) {
            return Math.nextAfter(max(), Float.NEGATIVE_INFINITY);
        } else {
            return max();
        }
    }

    default float width() {
        return maxExclusive() - minInclusive();
    }

    @Override
    default boolean includes(Float value) {
        return (minIncluded() ? value >= min() : value > min()) &&
                (maxIncluded() ? value <= max() : value < max());
    }

    default FloatRange withMinInclusive(float min) {
        return concreteFloatRange(min, true, max(), maxIncluded());
    }

    default FloatRange withMaxInclusive(float max) {
        return concreteFloatRange(min(), minIncluded(), max, true);
    }

    default FloatRange withMinExclusive(float minExclusive) {
        return concreteFloatRange(minExclusive, false, max(), maxIncluded());
    }

    default FloatRange withMaxExclusive(float maxExclusive) {
        return concreteFloatRange(min(), minIncluded(), maxExclusive, false);
    }

    default FloatRange negate() {
        return concreteFloatRange(-max(), maxIncluded(), -min(), minIncluded());
    }

    static FloatRangeFrom from(float min) {
        return concreteFloatRangeFrom(min, true);
    }

    static FloatRangeFrom fromExclusive(float min) {
        return concreteFloatRangeFrom(min, false);
    }

    static FloatRange inclusive(float min, float max) {
        return concreteFloatRange(min, true, max, true);
    }

    static FloatRange exclusive(float min, float maxExclusive) {
        return concreteFloatRange(min, true, maxExclusive, false);
    }

    static FloatRange exclusive(float maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return concreteFloatRange(0.0f, true, maxExclusive, false);
    }

    static FloatRange floatRange(float min, boolean minIncluded, float max, boolean maxIncluded) {
        return concreteFloatRange(min, minIncluded, max, maxIncluded);
    }

    static FloatRange fullRange() {
        return concreteFloatRange();
    }

    interface FloatRangeFrom {
        FloatRange to(float max);

        FloatRange until(float maxExclusive);
    }

}
