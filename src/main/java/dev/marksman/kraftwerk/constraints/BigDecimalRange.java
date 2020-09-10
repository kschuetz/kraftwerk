package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GT;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@link BigDecimal}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link BigDecimalRange#inclusive}, {@link BigDecimalRange#exclusive}),
 * <p>
 * or by using {@link BigDecimalRange#from}:
 * <pre>
 * BigDecimalRange.from(BigDecimal.ZERO).to(BigDecimal.TEN)      // inclusive upper bound
 * BigDecimalRange.from(BigDecimal.ZERO).until(BigDecimal.TEN)   // exclusive upper bound
 * </pre>
 */
public final class BigDecimalRange implements Constraint<BigDecimal> {
    private final BigDecimal min;
    private final boolean minIncluded;
    private final BigDecimal max;
    private final boolean maxIncluded;

    private BigDecimalRange(BigDecimal min, boolean minIncluded, BigDecimal max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    /**
     * Partially constructs a {@code BigDecimalRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code BigDecimalRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link BigDecimalRangeFrom}
     */
    public static BigDecimalRangeFrom from(BigDecimal minInclusive) {
        return new BigDecimalRangeFrom() {
            @Override
            public BigDecimalRange to(BigDecimal maxInclusive) {
                return bigDecimalRange(minInclusive, true, maxInclusive, true);
            }

            @Override
            public BigDecimalRange until(BigDecimal maxExclusive) {
                return bigDecimalRange(minInclusive, true, maxExclusive, false);
            }
        };
    }

    /**
     * Partially constructs a {@code BigDecimalRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code BigDecimalRange}.
     *
     * @param minExclusive the lower bound (exclusive) of the range
     * @return a {@link BigDecimalRangeFrom}
     */
    public static BigDecimalRangeFrom fromExclusive(BigDecimal minExclusive) {
        return new BigDecimalRangeFrom() {
            @Override
            public BigDecimalRange to(BigDecimal maxInclusive) {
                return bigDecimalRange(minExclusive, false, maxInclusive, true);
            }

            @Override
            public BigDecimalRange until(BigDecimal maxExclusive) {
                return bigDecimalRange(minExclusive, false, maxExclusive, false);
            }
        };
    }

    /**
     * Creates a {@code BigDecimalRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static BigDecimalRange inclusive(BigDecimal minInclusive, BigDecimal maxInclusive) {
        return bigDecimalRange(minInclusive, true, maxInclusive, true);
    }

    /**
     * Creates a {@code BigDecimalRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static BigDecimalRange exclusive(BigDecimal minInclusive, BigDecimal maxExclusive) {
        return bigDecimalRange(minInclusive, true, maxExclusive, false);
    }

    /**
     * Creates a {@code BigDecimalRange} from {@code 0}..{@code maxExclusive}.
     */
    public static BigDecimalRange exclusive(BigDecimal maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return bigDecimalRange(BigDecimal.ZERO, true, maxExclusive, false);
    }

    /**
     * Creates a {@code BigDecimalRange}.
     */
    public static BigDecimalRange bigDecimalRange(BigDecimal min, boolean minIncluded, BigDecimal max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            validateRangeInclusive(min, max);
        } else {
            validateRangeExclusive(min, max);
        }
        return new BigDecimalRange(min, minIncluded, max, maxIncluded);
    }

    public BigDecimal min() {
        return min;
    }

    public BigDecimal max() {
        return max;
    }

    public boolean minIncluded() {
        return minIncluded;
    }

    public boolean maxIncluded() {
        return maxIncluded;
    }

    @Override
    public boolean includes(BigDecimal value) {
        return (minIncluded ? GTE.gte(min, value) : GT.gt(min, value)) &&
                (maxIncluded ? LTE.lte(max, value) : LT.lt(max, value));
    }

    /**
     * Creates a new {@code BigDecimalRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code BigDecimalRange}
     */
    public BigDecimalRange withMinInclusive(BigDecimal minInclusive) {
        return bigDecimalRange(minInclusive, true, max, maxIncluded);
    }

    /**
     * Creates a new {@code BigDecimalRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code BigDecimalRange}
     */
    public BigDecimalRange withMaxInclusive(BigDecimal maxInclusive) {
        return bigDecimalRange(min, minIncluded, maxInclusive, true);
    }

    /**
     * Creates a new {@code BigDecimalRange} that is the same as this one, with a new lower bound.
     *
     * @param minExclusive the new lower bound (exclusive) for the range; must be less than this range's upper bound
     * @return a {@code BigDecimalRange}
     */
    public BigDecimalRange withMinExclusive(BigDecimal minExclusive) {
        return bigDecimalRange(minExclusive, false, max, maxIncluded);
    }

    /**
     * Creates a new {@code BigDecimalRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code BigDecimalRange}
     */
    public BigDecimalRange withMaxExclusive(BigDecimal maxExclusive) {
        return bigDecimalRange(min, minIncluded, maxExclusive, false);
    }

    public BigDecimalRange negate() {
        return bigDecimalRange(max.negate(), maxIncluded, min.negate(), minIncluded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BigDecimalRange that = (BigDecimalRange) o;

        if (minIncluded != that.minIncluded) return false;
        if (maxIncluded != that.maxIncluded) return false;
        if (!min.equals(that.min)) return false;
        return max.equals(that.max);
    }

    @Override
    public int hashCode() {
        int result = min.hashCode();
        result = 31 * result + (minIncluded ? 1 : 0);
        result = 31 * result + max.hashCode();
        result = 31 * result + (maxIncluded ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), min, minIncluded, max, maxIncluded);
    }

    /**
     * A partially constructed {@link BigDecimalRange}, with the lower bound already provided.
     */
    public interface BigDecimalRangeFrom {
        /**
         * Creates a {@link BigDecimalRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code BigDecimalRange}
         */
        BigDecimalRange to(BigDecimal maxInclusive);

        /**
         * Creates a {@link BigDecimalRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code BigDecimalRange}
         */
        BigDecimalRange until(BigDecimal maxExclusive);
    }
}
