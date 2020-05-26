package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeWidth;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class FloatRange implements Constraint<Float> {
    private final float min;
    private final boolean minIncluded;
    private final float max;
    private final boolean maxIncluded;

    private FloatRange(float min, boolean minIncluded, float max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    public static FloatRangeFrom from(float minInclusive) {
        return new FloatRangeFrom() {
            @Override
            public FloatRange to(float maxInclusive) {
                return floatRange(minInclusive, true, maxInclusive, true);
            }

            @Override
            public FloatRange until(float maxExclusive) {
                return floatRange(minInclusive, true, maxExclusive, false);
            }
        };
    }

    public static FloatRangeFrom fromExclusive(float minExclusive) {
        return new FloatRangeFrom() {
            @Override
            public FloatRange to(float maxInclusive) {
                return floatRange(minExclusive, false, maxInclusive, true);
            }

            @Override
            public FloatRange until(float maxExclusive) {
                return floatRange(minExclusive, false, maxExclusive, false);
            }
        };
    }

    public static FloatRange inclusive(float minInclusive, float maxInclusive) {
        return floatRange(minInclusive, true, maxInclusive, true);
    }

    public static FloatRange exclusive(float minInclusive, float maxExclusive) {
        return floatRange(minInclusive, true, maxExclusive, false);
    }

    public static FloatRange exclusive(float maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return floatRange(0.0f, true, maxExclusive, false);
    }

    public static FloatRange floatRange(float min, boolean minIncluded, float max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            validateRangeInclusive(min, max);
        } else {
            validateRangeExclusive(min, max);

            if (!(minIncluded || maxIncluded)) {
                validateRangeWidth(min, max);
            }
        }
        return new FloatRange(min, minIncluded, max, maxIncluded);
    }

    public float min() {
        return min;
    }

    public float max() {
        return max;
    }

    public boolean minIncluded() {
        return minIncluded;
    }

    public boolean maxIncluded() {
        return maxIncluded;
    }

    public float minInclusive() {
        if (minIncluded) {
            return min;
        } else {
            return Math.nextAfter(min, Float.POSITIVE_INFINITY);
        }
    }

    public double minExclusive() {
        if (minIncluded) {
            return Math.nextAfter(min, Float.NEGATIVE_INFINITY);
        } else {
            return min;
        }
    }

    public float maxInclusive() {
        if (maxIncluded) {
            return max;
        } else {
            return Math.nextAfter(max, Float.NEGATIVE_INFINITY);
        }
    }

    public float maxExclusive() {
        if (maxIncluded) {
            return Math.nextAfter(max, Float.POSITIVE_INFINITY);
        } else {
            return max;
        }
    }

    @Override
    public boolean includes(Float value) {
        return (minIncluded ? value >= min : value > min) &&
                (maxIncluded ? value <= max : value < max);
    }

    public FloatRange withMinInclusive(float min) {
        return floatRange(min, true, max, maxIncluded);
    }

    public FloatRange withMaxInclusive(float max) {
        return floatRange(min, minIncluded, max, true);
    }

    public FloatRange withMinExclusive(float minExclusive) {
        return floatRange(minExclusive, false, max, maxIncluded);
    }

    public FloatRange withMaxExclusive(float maxExclusive) {
        return floatRange(min, minIncluded, maxExclusive, false);
    }

    public FloatRange negate() {
        return floatRange(-max, maxIncluded, -min, minIncluded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatRange that = (FloatRange) o;

        if (Float.compare(that.min, min) != 0) return false;
        if (minIncluded != that.minIncluded) return false;
        if (Float.compare(that.max, max) != 0) return false;
        return maxIncluded == that.maxIncluded;
    }

    @Override
    public int hashCode() {
        int result = (min != +0.0f ? Float.floatToIntBits(min) : 0);
        result = 31 * result + (minIncluded ? 1 : 0);
        result = 31 * result + (max != +0.0f ? Float.floatToIntBits(max) : 0);
        result = 31 * result + (maxIncluded ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), min, minIncluded, max, maxIncluded);
    }

    public interface FloatRangeFrom {
        FloatRange to(float maxInclusive);

        FloatRange until(float maxExclusive);
    }

}
