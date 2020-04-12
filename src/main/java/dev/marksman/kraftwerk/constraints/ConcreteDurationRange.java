package dev.marksman.kraftwerk.constraints;

import java.time.Duration;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteDurationRange implements DurationRange {

    private final Duration min;
    private final Duration max;

    ConcreteDurationRange(Duration min, Duration max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Duration minInclusive() {
        return min;
    }

    @Override
    public Duration maxInclusive() {
        return max;
    }

    static DurationRange concreteDurationRangeInclusive(Duration min, Duration max) {
        validateRangeInclusive(min, max);
        return new ConcreteDurationRange(min, max);
    }

    static DurationRange concreteDurationRangeExclusive(Duration min, Duration maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
        return new ConcreteDurationRange(min, maxExclusive.minusDays(1));
    }

    static DurationFrom concreteDurationRangeFrom(Duration min) {
        return new DurationFrom() {
            @Override
            public DurationRange to(Duration max) {
                return concreteDurationRangeInclusive(min, max);
            }

            @Override
            public DurationRange until(Duration maxExclusive) {
                return concreteDurationRangeExclusive(min, maxExclusive);
            }
        };
    }

}
