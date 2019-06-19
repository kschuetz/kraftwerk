package dev.marksman.composablerandom;

import java.time.LocalDate;

import static dev.marksman.composablerandom.Generate.generateLongExclusive;
import static java.time.temporal.ChronoUnit.DAYS;

class Temporal {

    static Generate<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        return generateLocalDateExclusive(min, max.plusDays(1));
    }

    static Generate<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        long span = DAYS.between(origin, bound);
        return generateLongExclusive(span).fmap(origin::plusDays);
    }

    static Generate<LocalDate> generateLocalDateForYear(int year) {
        return generateLocalDateExclusive(LocalDate.of(year, 1, 1),
                LocalDate.of(year + 1, 1, 1));
    }

}
