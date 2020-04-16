package dev.marksman.kraftwerk.constraints;

import java.time.LocalTime;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class LocalTimeRange implements Constraint<LocalTime> {
    private static final LocalTimeRange FULL = new LocalTimeRange(LocalTime.MIN, LocalTime.MAX, true);

    private final LocalTime minInclusive;
    private final LocalTime max;
    private final boolean maxIncluded;

    private LocalTimeRange(LocalTime minInclusive, LocalTime max, boolean maxIncluded) {
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxIncluded = maxIncluded;
    }

    public static LocalTimeRangeFrom from(LocalTime minInclusive) {
        return new LocalTimeRangeFrom() {
            @Override
            public LocalTimeRange to(LocalTime maxInclusive) {
                return localTimeRange(minInclusive, maxInclusive, true);
            }

            @Override
            public LocalTimeRange until(LocalTime maxExclusive) {
                return localTimeRange(minInclusive, maxExclusive, false);
            }
        };
    }

    public static LocalTimeRange inclusive(LocalTime minInclusive, LocalTime maxInclusive) {
        return localTimeRange(minInclusive, maxInclusive, true);
    }

    public static LocalTimeRange exclusive(LocalTime minInclusive, LocalTime maxExclusive) {
        return localTimeRange(minInclusive, maxExclusive, false);
    }

    public static LocalTimeRange fullRange() {
        return FULL;
    }

    public static LocalTimeRange localTimeRange(LocalTime minInclusive, LocalTime max, boolean maxIncluded) {
        if (maxIncluded) {
            validateRangeInclusive(minInclusive, max);
        } else {
            validateRangeExclusive(minInclusive, max);
        }
        return new LocalTimeRange(minInclusive, max, maxIncluded);
    }

    public LocalTime minInclusive() {
        return minInclusive;
    }

    public LocalTime max() {
        return max;
    }

    public boolean maxIncluded() {
        return maxIncluded;
    }

    @Override
    public boolean includes(LocalTime time) {
        boolean beforeMin = time.isBefore(minInclusive);
        boolean afterMax = maxIncluded
                ? !(time.isBefore(max))
                : time.isAfter(max);

        return !(beforeMin || afterMax);
    }

    public LocalTimeRange withMinInclusive(LocalTime minInclusive) {
        return localTimeRange(minInclusive, max, maxIncluded);
    }

    public LocalTimeRange withMaxInclusive(LocalTime max) {
        return localTimeRange(minInclusive, max, true);
    }

    public LocalTimeRange withMaxExclusive(LocalTime maxExclusive) {
        return localTimeRange(minInclusive, maxExclusive, false);
    }

    public interface LocalTimeRangeFrom {
        LocalTimeRange to(LocalTime maxInclusive);

        LocalTimeRange until(LocalTime maxExclusive);
    }

}
