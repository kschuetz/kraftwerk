package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;

import java.time.LocalDate;

import static dev.marksman.composablerandom.builtin.Generators.generateLongExclusive;
import static java.time.temporal.ChronoUnit.DAYS;

class Temporal {

    static Generator<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        return generateLocalDateExclusive(min, max.plusDays(1));
    }

    static Generator<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        long span = DAYS.between(origin, bound);
        return generateLongExclusive(span).fmap(origin::plusDays);
    }

    static Generator<LocalDate> generateLocalDateForYear(int year) {
        return generateLocalDateExclusive(LocalDate.of(year, 1, 1),
                LocalDate.of(year + 1, 1, 1));
    }

}
