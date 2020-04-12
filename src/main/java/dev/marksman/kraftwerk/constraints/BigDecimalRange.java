package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GT;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.constraints.ConcreteBigDecimalRange.concreteBigDecimalRange;
import static dev.marksman.kraftwerk.constraints.ConcreteBigDecimalRange.concreteBigDecimalRangeFrom;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;

public interface BigDecimalRange extends Constraint<BigDecimal> {
    BigDecimal min();

    BigDecimal max();

    boolean minIncluded();

    boolean maxIncluded();

    @Override
    default boolean includes(BigDecimal value) {
        return (minIncluded() ? GTE.gte(min(), value) : GT.gt(min(), value)) &&
                (maxIncluded() ? LTE.lte(max(), value) : LT.lt(max(), value));
    }

    default BigDecimalRange withMinInclusive(BigDecimal min) {
        return concreteBigDecimalRange(min, true, max(), maxIncluded());
    }

    default BigDecimalRange withMaxInclusive(BigDecimal max) {
        return concreteBigDecimalRange(min(), minIncluded(), max, true);
    }

    default BigDecimalRange withMinExclusive(BigDecimal minExclusive) {
        return concreteBigDecimalRange(minExclusive, false, max(), maxIncluded());
    }

    default BigDecimalRange withMaxExclusive(BigDecimal maxExclusive) {
        return concreteBigDecimalRange(min(), minIncluded(), maxExclusive, false);
    }

    default BigDecimalRange negate() {
        return concreteBigDecimalRange(max().negate(), maxIncluded(), min().negate(), minIncluded());
    }

    static BigDecimalRangeFrom from(BigDecimal min) {
        return concreteBigDecimalRangeFrom(min, true);
    }

    static BigDecimalRangeFrom fromExclusive(BigDecimal min) {
        return concreteBigDecimalRangeFrom(min, false);
    }

    static BigDecimalRange inclusive(BigDecimal min, BigDecimal max) {
        return concreteBigDecimalRange(min, true, max, true);
    }

    static BigDecimalRange exclusive(BigDecimal min, BigDecimal maxExclusive) {
        return concreteBigDecimalRange(min, true, maxExclusive, false);
    }

    static BigDecimalRange exclusive(BigDecimal maxExclusive) {
        validateExclusiveBound(maxExclusive);
        return concreteBigDecimalRange(BigDecimal.ZERO, true, maxExclusive, false);
    }

    static BigDecimalRange bigDecimalRange(BigDecimal min, boolean minIncluded, BigDecimal max, boolean maxIncluded) {
        return concreteBigDecimalRange(min, minIncluded, max, maxIncluded);
    }

    interface BigDecimalRangeFrom {
        BigDecimalRange to(BigDecimal max);

        BigDecimalRange until(BigDecimal maxExclusive);
    }

}
