package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GT;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.math.BigDecimal;

import static dev.marksman.kraftwerk.constraints.ConcreteBigDecimalRange.concreteBigDecimalRange;
import static dev.marksman.kraftwerk.constraints.ConcreteBigDecimalRange.concreteBigDecimalRangeFrom;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateExclusiveBound;

public interface BigDecimalRange {
    BigDecimal min();

    BigDecimal max();

    boolean minIncluded();

    boolean maxIncluded();

//    default BigDecimal minInclusive() {
//        if (minIncluded()) {
//            return min();
//        } else {
//            return Math.nextAfter(min(), Double.POSITIVE_INFINITY);
//        }
//    }
//
//    default BigDecimal maxInclusive() {
//        if (maxIncluded()) {
//            return max();
//        } else {
//            return Math.nextAfter(max(), Double.NEGATIVE_INFINITY);
//        }
//    }
//
//    default BigDecimal maxExclusive() {
//        if (maxIncluded()) {
//            return Math.nextAfter(max(), Double.NEGATIVE_INFINITY);
//        } else {
//            return max();
//        }
//    }
//
//    default BigDecimal width() {
//        return maxExclusive() - minInclusive();
//    }

    default boolean contains(BigDecimal n) {
        return (minIncluded() ? GTE.gte(min(), n) : GT.gt(min(), n)) &&
                (maxIncluded() ? LTE.lte(max(), n) : LT.lt(max(), n));
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
