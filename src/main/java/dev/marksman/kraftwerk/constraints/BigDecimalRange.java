package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GT;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

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

    public static BigDecimalRangeFrom from(BigDecimal min) {
        return new BigDecimalRangeFrom() {
            @Override
            public BigDecimalRange to(BigDecimal maxInclusive) {
                return bigDecimalRange(min, true, maxInclusive, true);
            }

            @Override
            public BigDecimalRange until(BigDecimal maxExclusive) {
                return bigDecimalRange(min, true, maxExclusive, false);
            }
        };
    }

    public static BigDecimalRangeFrom fromExclusive(BigDecimal min) {
        return new BigDecimalRangeFrom() {
            @Override
            public BigDecimalRange to(BigDecimal maxInclusive) {
                return bigDecimalRange(min, false, maxInclusive, true);
            }

            @Override
            public BigDecimalRange until(BigDecimal maxExclusive) {
                return bigDecimalRange(min, false, maxExclusive, false);
            }
        };
    }

    public static BigDecimalRange inclusive(BigDecimal minInclusive, BigDecimal maxInclusive) {
        return bigDecimalRange(minInclusive, true, maxInclusive, true);
    }

    public static BigDecimalRange exclusive(BigDecimal minInclusive, BigDecimal maxExclusive) {
        return bigDecimalRange(minInclusive, true, maxExclusive, false);
    }

    public static BigDecimalRange exclusive(BigDecimal maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return bigDecimalRange(BigDecimal.ZERO, true, maxExclusive, false);
    }

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

    public BigDecimalRange withMinInclusive(BigDecimal minInclusive) {
        return bigDecimalRange(minInclusive, true, max, maxIncluded);
    }

    public BigDecimalRange withMaxInclusive(BigDecimal maxInclusive) {
        return bigDecimalRange(min, minIncluded, maxInclusive, true);
    }

    public BigDecimalRange withMinExclusive(BigDecimal minExclusive) {
        return bigDecimalRange(minExclusive, false, max, maxIncluded);
    }

    public BigDecimalRange withMaxExclusive(BigDecimal maxExclusive) {
        return bigDecimalRange(min, minIncluded, maxExclusive, false);
    }

    public BigDecimalRange negate() {
        return bigDecimalRange(max.negate(), maxIncluded, min.negate(), minIncluded);
    }

    public interface BigDecimalRangeFrom {
        BigDecimalRange to(BigDecimal maxInclusive);

        BigDecimalRange until(BigDecimal maxExclusive);
    }

}
