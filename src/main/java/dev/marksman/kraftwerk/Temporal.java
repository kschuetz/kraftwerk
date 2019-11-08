package dev.marksman.kraftwerk;

import java.time.*;

import static java.time.temporal.ChronoUnit.DAYS;

class Temporal {

    private static final long NANOS_PER_DAY = 60 * 60 * 24 * 1_000_000_000L;

    static Generator<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        if (!max.isAfter(min)) {
            return Generators.constant(min);
        } else {
            return generateLocalDateExclusive(min, max.plusDays(1));
        }
    }

    static Generator<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        long span = DAYS.between(origin, bound);
        if (span <= 1) {
            return Generators.constant(origin);
        } else {
            return Generators.generateLongExclusive(span).fmap(origin::plusDays);
        }
    }

    static Generator<LocalDate> generateLocalDateForYear(Year year) {
        return Generators.generateInt(1, year.length()).fmap(year::atDay);
    }

    static Generator<LocalDate> generateLocalDateForMonth(YearMonth yearMonth) {
        return Generators.generateInt(1, yearMonth.lengthOfMonth())
                .fmap(yearMonth::atDay);
    }

    static Generator<LocalTime> generateLocalTime() {
        return Generators.generateLongExclusive(NANOS_PER_DAY)
                .fmap(LocalTime::ofNanoOfDay);
    }

    static Generator<LocalTime> generateLocalTime(LocalTime min, LocalTime max) {
        long t0 = min.toNanoOfDay();
        long span = max.toNanoOfDay() - t0;
        if (span <= 0) {
            return Generators.constant(min);
        } else {
            return Generators.generateLong(0L, span)
                    .fmap(n -> LocalTime.ofNanoOfDay(t0 + n));
        }
    }

    static Generator<LocalDateTime> generateLocalDateTime(LocalDate min, LocalDate max) {
        return generateLocalDateTime(min.atStartOfDay(), max.atTime(LocalTime.MAX));
    }

    static Generator<LocalDateTime> generateLocalDateTime(LocalDateTime min, LocalDateTime max) {
        if (max.isBefore(min)) {
            return Generators.constant(min);
        }
        LocalDate firstDay = min.toLocalDate();
        LocalDate lastDay = max.toLocalDate();
        LocalTime timeOnFirstDay = min.toLocalTime();
        LocalTime timeOnLastDay = min.toLocalTime();
        if (firstDay.equals(lastDay)) {
            return generateLocalTime(timeOnFirstDay, timeOnLastDay)
                    .fmap(firstDay::atTime);
        }
        return generateLocalDate(firstDay, lastDay)
                .flatMap(day -> {
                    Generator<LocalTime> ltg;
                    if (day.equals(firstDay)) {
                        ltg = generateLocalTime(timeOnFirstDay, LocalTime.MAX);
                    } else if (day.equals(lastDay)) {
                        ltg = generateLocalTime(LocalTime.MIN, timeOnLastDay);
                    } else {
                        ltg = generateLocalTime();
                    }
                    return ltg.fmap(day::atTime);
                });
    }

    static Generator<Duration> generateDuration(Duration max) {
        if (max.isZero() || max.isNegative()) {
            return Generators.constant(Duration.ZERO);
        } else {
            long maxSeconds = max.getSeconds();
            int extraNanos = max.getNano();

            if (maxSeconds <= 0) {
                return Generators.generateLong(0, extraNanos)
                        .fmap(Duration::ofNanos);
            } else {
                return Generators.generateLong(0, maxSeconds)
                        .flatMap(s -> {
                            int maxNanos = s == maxSeconds ? extraNanos : 999_999_999;
                            return Generators.generateInt(0, maxNanos)
                                    .fmap(ns -> Duration.ofSeconds(s, ns));
                        });
            }
        }
    }

    static Generator<Duration> generateDuration(Duration min, Duration max) {
        return generateDuration(max.minus(min)).fmap(min::plus);
    }

}
