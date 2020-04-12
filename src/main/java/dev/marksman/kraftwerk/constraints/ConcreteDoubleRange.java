package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeWidth;

final class ConcreteDoubleRange implements DoubleRange {
    private static final DoubleRange FULL = new ConcreteDoubleRange(Double.MIN_VALUE, true, Double.MAX_VALUE, true);

    private final double min;
    private final boolean minIncluded;
    private final double max;
    private final boolean maxIncluded;

    ConcreteDoubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
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

    static DoubleRange concreteDoubleRange() {
        return FULL;
    }

    static DoubleRange concreteDoubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            validateRangeInclusive(min, max);
        } else {
            validateRangeExclusive(min, max);

            if (!(minIncluded || maxIncluded)) {
                validateRangeWidth(min, max);
            }
        }
        return new ConcreteDoubleRange(min, minIncluded, max, maxIncluded);
    }

    static DoubleRangeFrom concreteDoubleRangeFrom(double min, boolean minIncluded) {
        return new DoubleRangeFrom() {
            @Override
            public DoubleRange to(double max) {
                return concreteDoubleRange(min, minIncluded, max, true);
            }

            @Override
            public DoubleRange until(double maxExclusive) {
                return concreteDoubleRange(min, minIncluded, maxExclusive, false);
            }
        };
    }

}
