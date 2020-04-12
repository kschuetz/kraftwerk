package dev.marksman.kraftwerk.constraints;

import java.time.LocalTime;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteLocalTimeRange implements LocalTimeRange {

    private static final LocalTimeRange FULL = new ConcreteLocalTimeRange(LocalTime.MIN, LocalTime.MAX, true);

    private final LocalTime min;
    private final LocalTime max;
    private final boolean maxIncluded;

    ConcreteLocalTimeRange(LocalTime min, LocalTime max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.maxIncluded = maxIncluded;
    }

    @Override
    public LocalTime minInclusive() {
        return min;
    }

    @Override
    public LocalTime max() {
        return max;
    }

    @Override
    public boolean maxIncluded() {
        return maxIncluded;
    }

    static LocalTimeRange concreteLocalTimeRange(LocalTime min, LocalTime max, boolean maxIncluded) {
        if (maxIncluded) {
            validateRangeInclusive(min, max);
        } else {
            validateRangeExclusive(min, max);
        }
        return new ConcreteLocalTimeRange(min, max, maxIncluded);
    }

    static LocalTimeRange concreteLocalTimeRange() {
        return FULL;
    }

    static LocalTimeRangeFrom concreteLocalTimeRangeFrom(LocalTime min) {
        return new LocalTimeRangeFrom() {
            @Override
            public LocalTimeRange to(LocalTime max) {
                return concreteLocalTimeRange(min, max, true);
            }

            @Override
            public LocalTimeRange until(LocalTime maxExclusive) {
                return concreteLocalTimeRange(min, maxExclusive, false);
            }
        };
    }

}
