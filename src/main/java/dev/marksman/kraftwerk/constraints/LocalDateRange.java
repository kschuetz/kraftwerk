package dev.marksman.kraftwerk.constraints;

import java.time.LocalDate;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class LocalDateRange implements Constraint<LocalDate> {
    private final LocalDate minInclusive;
    private final LocalDate maxInclusive;

    private LocalDateRange(LocalDate minInclusive, LocalDate maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public static LocalDateRangeFrom from(LocalDate minInclusive) {
        return new LocalDateRangeFrom() {
            @Override
            public LocalDateRange to(LocalDate maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public LocalDateRange until(LocalDate maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    public static LocalDateRange inclusive(LocalDate minInclusive, LocalDate maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new LocalDateRange(minInclusive, maxInclusive);
    }

    public static LocalDateRange exclusive(LocalDate minInclusive, LocalDate maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new LocalDateRange(minInclusive, maxExclusive.minusDays(1));
    }

    public LocalDate minInclusive() {
        return minInclusive;
    }

    public LocalDate maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(LocalDate date) {
        return !(date.isBefore(minInclusive) || date.isAfter(maxInclusive));
    }

    public LocalDateRange withMinInclusive(LocalDate min) {
        return inclusive(min, maxInclusive);
    }

    public LocalDateRange withMaxInclusive(LocalDate max) {
        return inclusive(minInclusive, max);
    }

    public LocalDateRange withMaxExclusive(LocalDate max) {
        return exclusive(minInclusive, max);
    }

    public interface LocalDateRangeFrom {
        LocalDateRange to(LocalDate maxInclusive);

        LocalDateRange until(LocalDate maxExclusive);
    }

}
