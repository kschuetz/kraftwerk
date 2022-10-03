package software.kes.kraftwerk.constraints;

import static software.kes.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@code double}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link DoubleRange#inclusive}, {@link DoubleRange#exclusive}),
 * <p>
 * or by using {@link DoubleRange#from}:
 * <pre>
 * DoubleRange.from(0.0).to(10.0)      // inclusive upper bound
 * DoubleRange.from(0.0).until(10.0)   // exclusive upper bound
 * </pre>
 */
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

    /**
     * Partially constructs a {@code DoubleRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code DoubleRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link DoubleRangeFrom}
     */
    public static DoubleRangeFrom from(double minInclusive) {
        return new DoubleRangeFrom() {
            @Override
            public DoubleRange to(double maxInclusive) {
                return doubleRange(minInclusive, true, maxInclusive, true);
            }

            @Override
            public DoubleRange until(double maxExclusive) {
                return doubleRange(minInclusive, true, maxExclusive, false);
            }
        };
    }

    /**
     * Partially constructs a {@code DoubleRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code DoubleRange}.
     *
     * @param minExclusive the lower bound (exclusive) of the range
     * @return a {@link DoubleRangeFrom}
     */
    public static DoubleRangeFrom fromExclusive(double minExclusive) {
        return new DoubleRangeFrom() {
            @Override
            public DoubleRange to(double maxInclusive) {
                return doubleRange(minExclusive, false, maxInclusive, true);
            }

            @Override
            public DoubleRange until(double maxExclusive) {
                return doubleRange(minExclusive, false, maxExclusive, false);
            }
        };
    }

    /**
     * Creates a {@code DoubleRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static DoubleRange inclusive(double minInclusive, double maxInclusive) {
        return doubleRange(minInclusive, true, maxInclusive, true);
    }

    /**
     * Creates a {@code DoubleRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static DoubleRange exclusive(double minInclusive, double maxExclusive) {
        return doubleRange(minInclusive, true, maxExclusive, false);
    }

    /**
     * Creates a {@code DoubleRange} from {@code 0}..{@code maxExclusive}.
     */
    public static DoubleRange exclusive(double maxExclusive) {
        RangeInputValidation.validateExclusiveBound(maxExclusive);
        return doubleRange(0.0d, true, maxExclusive, false);
    }

    /**
     * Creates a {@code DoubleRange}.
     */
    public static DoubleRange doubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            RangeInputValidation.validateRangeInclusive(min, max);
        } else {
            RangeInputValidation.validateRangeExclusive(min, max);

            if (!(minIncluded || maxIncluded)) {
                RangeInputValidation.validateRangeWidth(min, max);
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

    /**
     * Creates a new {@code DoubleRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code DoubleRange}
     */
    public DoubleRange withMinInclusive(double minInclusive) {
        return doubleRange(minInclusive, true, max, maxIncluded);
    }

    /**
     * Creates a new {@code DoubleRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code DoubleRange}
     */
    public DoubleRange withMaxInclusive(double maxInclusive) {
        return doubleRange(min, minIncluded, maxInclusive, true);
    }

    /**
     * Creates a new {@code DoubleRange} that is the same as this one, with a new lower bound.
     *
     * @param minExclusive the new lower bound (exclusive) for the range; must be less than this range's upper bound
     * @return a {@code DoubleRange}
     */
    public DoubleRange withMinExclusive(double minExclusive) {
        return doubleRange(minExclusive, false, max, maxIncluded);
    }

    /**
     * Creates a new {@code DoubleRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code DoubleRange}
     */
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

    /**
     * A partially constructed {@link DoubleRange}, with the lower bound already provided.
     */
    public interface DoubleRangeFrom {
        /**
         * Creates a {@link DoubleRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code DoubleRange}
         */
        DoubleRange to(double maxInclusive);

        /**
         * Creates a {@link DoubleRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code DoubleRange}
         */
        DoubleRange until(double maxExclusive);
    }
}
