package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeWidth;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@code float}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link FloatRange#inclusive}, {@link FloatRange#exclusive}),
 * <p>
 * or by using {@link FloatRange#from}:
 * <pre>
 * FloatRange.from(0.0).to(10.0)      // inclusive upper bound
 * FloatRange.from(0.0).until(10.0)   // exclusive upper bound
 * </pre>
 */
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

    /**
     * Partially constructs a {@code FloatRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code FloatRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link FloatRangeFrom}
     */
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

    /**
     * Partially constructs a {@code FloatRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code FloatRange}.
     *
     * @param minExclusive the lower bound (exclusive) of the range
     * @return a {@link FloatRangeFrom}
     */
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

    /**
     * Creates a {@code FloatRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static FloatRange inclusive(float minInclusive, float maxInclusive) {
        return floatRange(minInclusive, true, maxInclusive, true);
    }

    /**
     * Creates a {@code FloatRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static FloatRange exclusive(float minInclusive, float maxExclusive) {
        return floatRange(minInclusive, true, maxExclusive, false);
    }

    /**
     * Creates a {@code FloatRange} from {@code 0}..{@code maxExclusive}.
     */
    public static FloatRange exclusive(float maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return floatRange(0.0f, true, maxExclusive, false);
    }

    /**
     * Creates a {@code FloatRange}.
     */
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

    /**
     * Creates a new {@code FloatRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code FloatRange}
     */
    public FloatRange withMinInclusive(float minInclusive) {
        return floatRange(minInclusive, true, max, maxIncluded);
    }

    /**
     * Creates a new {@code FloatRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code FloatRange}
     */
    public FloatRange withMaxInclusive(float maxInclusive) {
        return floatRange(min, minIncluded, maxInclusive, true);
    }

    /**
     * Creates a new {@code FloatRange} that is the same as this one, with a new lower bound.
     *
     * @param minExclusive the new lower bound (exclusive) for the range; must be less than this range's upper bound
     * @return a {@code FloatRange}
     */
    public FloatRange withMinExclusive(float minExclusive) {
        return floatRange(minExclusive, false, max, maxIncluded);
    }

    /**
     * Creates a new {@code FloatRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code FloatRange}
     */
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

    /**
     * A partially constructed {@link FloatRange}, with the lower bound already provided.
     */
    public interface FloatRangeFrom {
        /**
         * Creates a {@link FloatRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code FloatRange}
         */
        FloatRange to(float maxInclusive);

        /**
         * Creates a {@link FloatRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code FloatRange}
         */
        FloatRange until(float maxExclusive);
    }
}
