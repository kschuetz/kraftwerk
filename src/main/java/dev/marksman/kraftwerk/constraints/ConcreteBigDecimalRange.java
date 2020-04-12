package dev.marksman.kraftwerk.constraints;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteBigDecimalRange implements BigDecimalRange {

    private final BigDecimal min;
    private final boolean minIncluded;
    private final BigDecimal max;
    private final boolean maxIncluded;

    ConcreteBigDecimalRange(BigDecimal min, boolean minIncluded, BigDecimal max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    @Override
    public BigDecimal min() {
        return min;
    }

    @Override
    public BigDecimal max() {
        return max;
    }

    @Override
    public boolean minIncluded() {
        return minIncluded;
    }

    @Override
    public boolean maxIncluded() {
        return maxIncluded;
    }

    static BigDecimalRange concreteBigDecimalRange(BigDecimal min, boolean minIncluded, BigDecimal max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            validateRangeInclusive(min, max);
        } else {
            validateRangeExclusive(min, max);
        }
        return new ConcreteBigDecimalRange(min, minIncluded, max, maxIncluded);
    }

    static BigDecimalRangeFrom concreteBigDecimalRangeFrom(BigDecimal min, boolean minIncluded) {
        return new BigDecimalRangeFrom() {
            @Override
            public BigDecimalRange to(BigDecimal max) {
                return concreteBigDecimalRange(min, minIncluded, max, true);
            }

            @Override
            public BigDecimalRange until(BigDecimal maxExclusive) {
                return concreteBigDecimalRange(min, minIncluded, maxExclusive, false);
            }
        };
    }

}
