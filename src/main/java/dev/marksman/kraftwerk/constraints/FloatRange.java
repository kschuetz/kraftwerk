package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeWidth;

public final class FloatRange implements Constraint<Float> {
    private static final FloatRange FULL = new FloatRange(Float.MIN_VALUE, true, Float.MAX_VALUE, true);

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

    public static FloatRange fullRange() {
        return FULL;
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
            return Math.nextAfter(min, Double.POSITIVE_INFINITY);
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
            return Math.nextAfter(max, Float.NEGATIVE_INFINITY);
        } else {
            return max;
        }
    }

    public float width() {
        return maxExclusive() - minInclusive();
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FloatRange)) return false;
        final FloatRange other = (FloatRange) o;
        if (Float.compare(this.min, other.min) != 0) return false;
        if (this.minIncluded != other.minIncluded) return false;
        if (Float.compare(this.max, other.max) != 0) return false;
        return this.maxIncluded == other.maxIncluded;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + Float.floatToIntBits(this.min);
        result = result * PRIME + (this.minIncluded ? 79 : 97);
        result = result * PRIME + Float.floatToIntBits(this.max);
        result = result * PRIME + (this.maxIncluded ? 79 : 97);
        return result;
    }

    public interface FloatRangeFrom {
        FloatRange to(float maxInclusive);

        FloatRange until(float maxExclusive);
    }

}
