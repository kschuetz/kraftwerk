package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.constraints.DurationRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LocalDateRange;
import dev.marksman.kraftwerk.constraints.LocalDateTimeRange;
import dev.marksman.kraftwerk.constraints.LocalTimeRange;
import dev.marksman.kraftwerk.constraints.LongRange;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;

import static java.time.temporal.ChronoUnit.DAYS;

class Temporal {

    private static final long NANOS_PER_DAY = 60 * 60 * 24 * 1_000_000_000L;

    static Generator<LocalDate> generateLocalDate(LocalDateRange range) {
        LocalDate origin = range.minInclusive();
        LocalDate bound = range.maxInclusive().plusDays(1);
        long span = DAYS.between(origin, bound);
        if (span <= 1) {
            return Generators.constant(origin);
        } else {
            return Generators.generateLong(LongRange.exclusive(span)).fmap(origin::plusDays);
        }
    }

    static Generator<LocalDate> generateLocalDateForYear(Year year) {
        return Generators.generateInt(IntRange.from(1).to(year.length())).fmap(year::atDay);
    }

    static Generator<LocalDate> generateLocalDateForMonth(YearMonth yearMonth) {
        return Generators.generateInt(IntRange.from(1).to(yearMonth.lengthOfMonth()))
                .fmap(yearMonth::atDay);
    }

    static Generator<LocalTime> generateLocalTime() {
        return Generators.generateLong(LongRange.exclusive(NANOS_PER_DAY))
                .fmap(LocalTime::ofNanoOfDay);
    }

    static Generator<LocalTime> generateLocalTime(LocalTimeRange range) {
        LocalTime max = range.maxIncluded() ? range.max() : range.max().minusNanos(1);
        long t0 = range.minInclusive().toNanoOfDay();
        long span = max.toNanoOfDay() - t0;
        if (span <= 0) {
            return Generators.constant(range.minInclusive());
        } else {
            return Generators.generateLong(LongRange.inclusive(0L, span))
                    .fmap(n -> LocalTime.ofNanoOfDay(t0 + n));
        }
    }

    static Generator<LocalDateTime> generateLocalDateTime(LocalDateRange range) {
        return generateLocalDateTime(LocalDateTimeRange.inclusive(range.minInclusive().atStartOfDay(),
                range.maxInclusive().atTime(LocalTime.MAX)));
    }

    static Generator<LocalDateTime> generateLocalDateTime(LocalDateTimeRange range) {
        LocalDateTime min = range.minInclusive();
        LocalDateTime max = range.maxInclusive();
        if (max.isBefore(min)) {
            return Generators.constant(min);
        }
        LocalDate firstDay = min.toLocalDate();
        LocalDate lastDay = max.toLocalDate();
        LocalTime timeOnFirstDay = min.toLocalTime();
        LocalTime timeOnLastDay = min.toLocalTime();
        if (firstDay.equals(lastDay)) {
            return generateLocalTime(LocalTimeRange.inclusive(timeOnFirstDay, timeOnLastDay))
                    .fmap(firstDay::atTime);
        }
        return generateLocalDate(LocalDateRange.inclusive(firstDay, lastDay))
                .flatMap(day -> {
                    Generator<LocalTime> ltg;
                    if (day.equals(firstDay)) {
                        ltg = generateLocalTime(LocalTimeRange.inclusive(timeOnFirstDay, LocalTime.MAX));
                    } else if (day.equals(lastDay)) {
                        ltg = generateLocalTime(LocalTimeRange.inclusive(LocalTime.MIN, timeOnLastDay));
                    } else {
                        ltg = generateLocalTime();
                    }
                    return ltg.fmap(day::atTime);
                });
    }

    static Generator<Duration> generateDuration(DurationRange range) {
        Duration min = range.minInclusive();
        Duration max = range.maxInclusive();
        return generateDuration(max.minus(min)).fmap(min::plus);
    }

    private static Generator<Duration> generateDuration(Duration max) {
        if (max.isZero() || max.isNegative()) {
            return Generators.constant(Duration.ZERO);
        } else {
            long maxSeconds = max.getSeconds();
            int extraNanos = max.getNano();

            if (maxSeconds <= 0) {
                return Generators.generateLong(LongRange.inclusive(0, extraNanos))
                        .fmap(Duration::ofNanos);
            } else {
                return Generators.generateLong(LongRange.inclusive(0, maxSeconds))
                        .flatMap(s -> {
                            int maxNanos = s == maxSeconds ? extraNanos : 999_999_999;
                            return Generators.generateInt(IntRange.from(0).to(maxNanos))
                                    .fmap(ns -> Duration.ofSeconds(s, ns));
                        });
            }
        }
    }

}
