package dev.marksman.composablerandom;

import java.time.*;

import static dev.marksman.composablerandom.Generate.*;
import static java.time.temporal.ChronoUnit.DAYS;

class Temporal {

    private static final long NANOS_PER_DAY = 60 * 60 * 24 * 1_000_000_000L;

    static Generate<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        if (!max.isAfter(min)) {
            return Generate.constant(min);
        } else {
            return generateLocalDateExclusive(min, max.plusDays(1));
        }
    }

    static Generate<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        long span = DAYS.between(origin, bound);
        if (span <= 1) {
            return Generate.constant(origin);
        } else {
            return generateLongExclusive(span).fmap(origin::plusDays);
        }
    }

    static Generate<LocalDate> generateLocalDateForYear(Year year) {
        return generateInt(1, year.length()).fmap(year::atDay);
    }

    static Generate<LocalDate> generateLocalDateForMonth(YearMonth yearMonth) {
        return generateInt(1, yearMonth.lengthOfMonth())
                .fmap(yearMonth::atDay);
    }

    static Generate<LocalTime> generateLocalTime() {
        return generateLongExclusive(NANOS_PER_DAY)
                .fmap(LocalTime::ofNanoOfDay);
    }

    static Generate<LocalTime> generateLocalTime(LocalTime min, LocalTime max) {
        long t0 = min.toNanoOfDay();
        long span = max.toNanoOfDay() - t0;
        if (span <= 0) {
            return Generate.constant(min);
        } else {
            return generateLong(0L, span)
                    .fmap(n -> LocalTime.ofNanoOfDay(t0 + n));
        }
    }

    static Generate<LocalDateTime> generateLocalDateTime(LocalDate min, LocalDate max) {
        return generateLocalDateTime(min.atStartOfDay(), max.atTime(LocalTime.MAX));
    }

    static Generate<LocalDateTime> generateLocalDateTime(LocalDateTime min, LocalDateTime max) {
        if (max.isBefore(min)) {
            return Generate.constant(min);
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
                    Generate<LocalTime> ltg;
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

    static Generate<Duration> generateDuration(Duration max) {
        if (max.isZero() || max.isNegative()) {
            return Generate.constant(Duration.ZERO);
        } else {
            long maxSeconds = max.getSeconds();
            int extraNanos = max.getNano();

            if (maxSeconds <= 0) {
                return Generate.generateLong(0, extraNanos)
                        .fmap(Duration::ofNanos);
            } else {
                return generateLong(0, maxSeconds)
                        .flatMap(s -> {
                            int maxNanos = s == maxSeconds ? extraNanos : 999_999_999;
                            return generateInt(0, maxNanos)
                                    .fmap(ns -> Duration.ofSeconds(s, ns));
                        });
            }
        }
    }

    static Generate<Duration> generateDuration(Duration min, Duration max) {
        return generateDuration(max.minus(min)).fmap(min::plus);
    }

}
