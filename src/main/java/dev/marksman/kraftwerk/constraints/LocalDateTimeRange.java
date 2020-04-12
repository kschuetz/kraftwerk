package dev.marksman.kraftwerk.constraints;

import java.time.LocalDateTime;

import static dev.marksman.kraftwerk.constraints.ConcreteLocalDateTimeRange.concreteLocalDateTimeRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteLocalDateTimeRange.concreteLocalDateTimeRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteLocalDateTimeRange.concreteLocalDateTimeRangeInclusive;

public interface LocalDateTimeRange {
    LocalDateTime minInclusive();

    LocalDateTime maxInclusive();

    default boolean contains(LocalDateTime dateTime) {
        return !(dateTime.isBefore(minInclusive()) || dateTime.isAfter(maxInclusive()));
    }

    default LocalDateTimeRange withMin(LocalDateTime min) {
        return concreteLocalDateTimeRangeInclusive(min, maxInclusive());
    }

    default LocalDateTimeRange withMaxInclusive(LocalDateTime max) {
        return concreteLocalDateTimeRangeInclusive(minInclusive(), max);
    }

    default LocalDateTimeRange withMaxExclusive(LocalDateTime max) {
        return concreteLocalDateTimeRangeExclusive(minInclusive(), max);
    }

    static LocalDateTimeRangeFrom from(LocalDateTime min) {
        return concreteLocalDateTimeRangeFrom(min);
    }

    static LocalDateTimeRange inclusive(LocalDateTime min, LocalDateTime max) {
        return concreteLocalDateTimeRangeInclusive(min, max);
    }

    static LocalDateTimeRange exclusive(LocalDateTime min, LocalDateTime maxExclusive) {
        return concreteLocalDateTimeRangeExclusive(min, maxExclusive);
    }

    interface LocalDateTimeRangeFrom {
        LocalDateTimeRange to(LocalDateTime max);

        LocalDateTimeRange until(LocalDateTime maxExclusive);
    }
}
