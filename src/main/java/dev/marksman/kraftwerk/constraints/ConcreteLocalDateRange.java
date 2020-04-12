package dev.marksman.kraftwerk.constraints;

import java.time.LocalDate;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteLocalDateRange implements LocalDateRange {

    private final LocalDate min;
    private final LocalDate max;

    ConcreteLocalDateRange(LocalDate min, LocalDate max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public LocalDate minInclusive() {
        return min;
    }

    @Override
    public LocalDate maxInclusive() {
        return max;
    }

    static LocalDateRange concreteLocalDateRangeInclusive(LocalDate min, LocalDate max) {
        validateRangeInclusive(min, max);
        return new ConcreteLocalDateRange(min, max);
    }

    static LocalDateRange concreteLocalDateRangeExclusive(LocalDate min, LocalDate maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
        return new ConcreteLocalDateRange(min, maxExclusive.minusDays(1));
    }

    static LocalDateRangeFrom concreteLocalDateRangeFrom(LocalDate min) {
        return new LocalDateRangeFrom() {
            @Override
            public LocalDateRange to(LocalDate max) {
                return concreteLocalDateRangeInclusive(min, max);
            }

            @Override
            public LocalDateRange until(LocalDate maxExclusive) {
                return concreteLocalDateRangeExclusive(min, maxExclusive);
            }
        };
    }

}
