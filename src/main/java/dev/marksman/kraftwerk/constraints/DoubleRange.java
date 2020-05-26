package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeWidth;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class DoubleRange implements Constraint<Double> {
    private final double min;
    private final boolean minIncluded;
    private final double max;
    private final boolean maxIncluded;

    private DoubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    public static DoubleRangeFrom from(double min) {
        return new DoubleRangeFrom() {
            @Override
            public DoubleRange to(double maxInclusive) {
                return doubleRange(min, true, maxInclusive, true);
            }

            @Override
            public DoubleRange until(double maxExclusive) {
                return doubleRange(min, true, maxExclusive, false);
            }
        };
    }

    public static DoubleRangeFrom fromExclusive(double min) {
        return new DoubleRangeFrom() {
            @Override
            public DoubleRange to(double maxInclusive) {
                return doubleRange(min, false, maxInclusive, true);
            }

            @Override
            public DoubleRange until(double maxExclusive) {
                return doubleRange(min, false, maxExclusive, false);
            }
        };
    }

    public static DoubleRange inclusive(double minInclusive, double maxInclusive) {
        return doubleRange(minInclusive, true, maxInclusive, true);
    }

    public static DoubleRange exclusive(double minInclusive, double maxExclusive) {
        return doubleRange(minInclusive, true, maxExclusive, false);
    }

    public static DoubleRange exclusive(double maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return doubleRange(0.0d, true, maxExclusive, false);
    }

    public static DoubleRange doubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            validateRangeInclusive(min, max);
        } else {
            validateRangeExclusive(min, max);

            if (!(minIncluded || maxIncluded)) {
                validateRangeWidth(min, max);
            }
        }
        return new DoubleRange(min, minIncluded, max, maxIncluded);
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public boolean minIncluded() {
        return minIncluded;
    }

    public boolean maxIncluded() {
        return maxIncluded;
    }

    @Override
    public boolean includes(Double value) {
        return (minIncluded ? value >= min : value > min) &&
                (maxIncluded ? value <= max : value < max);
    }

    public double minInclusive() {
        if (minIncluded) {
            return min;
        } else {
            return Math.nextAfter(min, Double.POSITIVE_INFINITY);
        }
    }

    public double minExclusive() {
        if (minIncluded) {
            return Math.nextAfter(min, Double.NEGATIVE_INFINITY);
        } else {
            return min;
        }
    }

    public double maxInclusive() {
        if (maxIncluded) {
            return max;
        } else {
            return Math.nextAfter(max, Double.NEGATIVE_INFINITY);
        }
    }

    public double maxExclusive() {
        if (maxIncluded) {
            return Math.nextAfter(max, Double.POSITIVE_INFINITY);
        } else {
            return max;
        }
    }

    public DoubleRange withMinInclusive(double minInclusive) {
        return doubleRange(minInclusive, true, max, maxIncluded);
    }

    public DoubleRange withMaxInclusive(double maxInclusive) {
        return doubleRange(min, minIncluded, maxInclusive, true);
    }

    public DoubleRange withMinExclusive(double minExclusive) {
        return doubleRange(minExclusive, false, max, maxIncluded);
    }

    public DoubleRange withMaxExclusive(double maxExclusive) {
        return doubleRange(min, minIncluded, maxExclusive, false);
    }

    public DoubleRange negate() {
        return doubleRange(-max, maxIncluded, -min, minIncluded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubleRange that = (DoubleRange) o;

        if (Double.compare(that.min, min) != 0) return false;
        if (minIncluded != that.minIncluded) return false;
        if (Double.compare(that.max, max) != 0) return false;
        return maxIncluded == that.maxIncluded;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(min);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (minIncluded ? 1 : 0);
        temp = Double.doubleToLongBits(max);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (maxIncluded ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), min, minIncluded, max, maxIncluded);
    }

    public interface DoubleRangeFrom {
        DoubleRange to(double maxInclusive);

        DoubleRange until(double maxExclusive);
    }

}
