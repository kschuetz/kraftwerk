package software.kes.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.math.BigDecimal;

class RangeInputValidation {

    static <A extends Comparable<A>> void validateRangeInclusive(A min, A max) {
        if (max.compareTo(min) < 0) {
            throw new IllegalArgumentException("max must be >= min");
        }
    }

    static <A extends Comparable<A>> void validateRangeExclusive(A min, A max) {
        if (max.compareTo(min) <= 0) {
            throw new IllegalArgumentException("max must be > min");
        }
    }

    static void validateExclusiveBound(double bound) {
        if (bound <= 0.0d) {
            throw new IllegalArgumentException("max must be > 0");
        }
    }

    static void validateExclusiveBound(float bound) {
        if (bound <= 0.0f) {
            throw new IllegalArgumentException("max must be > 0");
        }
    }

    static void validateExclusiveBound(BigDecimal bound) {
        if (LTE.lte(BigDecimal.ZERO, bound)) {
            throw new IllegalArgumentException("max must be > 0");
        }
    }

    static void validateRangeWidth(double minExclusive, double maxExclusive) {
        // if both min and max are excluded, we need to be sure at least one
        // representable double exists between min and max
        if (maxExclusive == Math.nextAfter(minExclusive, Double.POSITIVE_INFINITY)) {
            throw new IllegalArgumentException("range is too narrow");
        }

    }

    static void validateRangeWidth(float minExclusive, float maxExclusive) {
        // if both min and max are excluded, we need to be sure at least one
        // representable float exists between min and max
        if (maxExclusive == Math.nextAfter(minExclusive, Float.POSITIVE_INFINITY)) {
            throw new IllegalArgumentException("range is too narrow");
        }

    }

}
