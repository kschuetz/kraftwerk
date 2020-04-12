package dev.marksman.kraftwerk.constraints;

import java.time.LocalDate;

import static dev.marksman.kraftwerk.constraints.ConcreteLocalDateRange.concreteLocalDateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteLocalDateRange.concreteLocalDateRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteLocalDateRange.concreteLocalDateRangeInclusive;

public interface LocalDateRange {
    LocalDate minInclusive();

    LocalDate maxInclusive();

    default boolean contains(LocalDate date) {
        return !(date.isBefore(minInclusive()) || date.isAfter(maxInclusive()));
    }

    default LocalDateRange withMin(LocalDate min) {
        return concreteLocalDateRangeInclusive(min, maxInclusive());
    }

    default LocalDateRange withMaxInclusive(LocalDate max) {
        return concreteLocalDateRangeInclusive(minInclusive(), max);
    }

    default LocalDateRange withMaxExclusive(LocalDate max) {
        return concreteLocalDateRangeExclusive(minInclusive(), max);
    }

    static LocalDateRangeFrom from(LocalDate min) {
        return concreteLocalDateRangeFrom(min);
    }

    static LocalDateRange inclusive(LocalDate min, LocalDate max) {
        return concreteLocalDateRangeInclusive(min, max);
    }

    static LocalDateRange exclusive(LocalDate min, LocalDate maxExclusive) {
        return concreteLocalDateRangeExclusive(min, maxExclusive);
    }

    interface LocalDateRangeFrom {
        LocalDateRange to(LocalDate max);

        LocalDateRange until(LocalDate maxExclusive);
    }
}
