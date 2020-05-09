package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeWidth;

public final class DoubleRange implements Constraint<Double> {
    private static final DoubleRange FULL = new DoubleRange(Double.MIN_VALUE, true, Double.MAX_VALUE, true);

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

    public static DoubleRange fullRange() {
        return FULL;
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
            return Math.nextAfter(min, Double.POSITIVE_INFINITY);
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
            return Math.nextAfter(max, Double.NEGATIVE_INFINITY);
        } else {
            return max;
        }
    }

    public double width() {
        return maxExclusive() - minInclusive();
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DoubleRange)) return false;
        final DoubleRange other = (DoubleRange) o;
        if (Double.compare(this.min, other.min) != 0) return false;
        if (this.minIncluded != other.minIncluded) return false;
        if (Double.compare(this.max, other.max) != 0) return false;
        return this.maxIncluded == other.maxIncluded;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $min = Double.doubleToLongBits(this.min);
        result = result * PRIME + (int) ($min >>> 32 ^ $min);
        result = result * PRIME + (this.minIncluded ? 79 : 97);
        final long $max = Double.doubleToLongBits(this.max);
        result = result * PRIME + (int) ($max >>> 32 ^ $max);
        result = result * PRIME + (this.maxIncluded ? 79 : 97);
        return result;
    }

    public interface DoubleRangeFrom {
        DoubleRange to(double maxInclusive);

        DoubleRange until(double maxExclusive);
    }

}
