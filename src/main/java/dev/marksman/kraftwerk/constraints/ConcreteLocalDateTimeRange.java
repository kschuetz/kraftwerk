package dev.marksman.kraftwerk.constraints;

import java.time.LocalDateTime;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteLocalDateTimeRange implements LocalDateTimeRange {

    private final LocalDateTime min;
    private final LocalDateTime max;

    ConcreteLocalDateTimeRange(LocalDateTime min, LocalDateTime max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public LocalDateTime minInclusive() {
        return min;
    }

    @Override
    public LocalDateTime maxInclusive() {
        return max;
    }

    static LocalDateTimeRange concreteLocalDateTimeRangeInclusive(LocalDateTime min, LocalDateTime max) {
        validateRangeInclusive(min, max);
        return new ConcreteLocalDateTimeRange(min, max);
    }

    static LocalDateTimeRange concreteLocalDateTimeRangeExclusive(LocalDateTime min, LocalDateTime maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
        return new ConcreteLocalDateTimeRange(min, maxExclusive.minusDays(1));
    }

    static LocalDateTimeRangeFrom concreteLocalDateTimeRangeFrom(LocalDateTime min) {
        return new LocalDateTimeRangeFrom() {
            @Override
            public LocalDateTimeRange to(LocalDateTime max) {
                return concreteLocalDateTimeRangeInclusive(min, max);
            }

            @Override
            public LocalDateTimeRange until(LocalDateTime maxExclusive) {
                return concreteLocalDateTimeRangeExclusive(min, maxExclusive);
            }
        };
    }

}
