package software.kes.kraftwerk;

import software.kes.kraftwerk.constraints.DurationRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LocalDateRange;
import software.kes.kraftwerk.constraints.LocalDateTimeRange;
import software.kes.kraftwerk.constraints.LocalTimeRange;
import software.kes.kraftwerk.constraints.LongRange;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

import static java.time.temporal.ChronoUnit.DAYS;

final class Temporal {
    static final LocalDateRange DEFAULT_LOCAL_DATE_RANGE =
            LocalDateRange.from(LocalDate.of(1899, 1, 1))
                    .to(LocalDate.of(2199, 12, 31));
    static final DurationRange DEFAULT_DURATION_RANGE = DurationRange.from(Duration.ZERO).to(Duration.ofDays(1000));
    private static final Generator<Month> MONTH_GENERATOR = Enums.generateFromEnum(Month.class);
    private static final Generator<DayOfWeek> DAY_OF_WEEK_GENERATOR = Generators.generateFromEnum(DayOfWeek.class);
    private static final long NANOS_PER_DAY = 60 * 60 * 24 * 1_000_000_000L;

    private Temporal() {
    }

    static Generator<Month> generateMonth() {
        return MONTH_GENERATOR;
    }

    static Generator<DayOfWeek> generateDayOfWeek() {
        return DAY_OF_WEEK_GENERATOR;
    }

    static Generator<LocalDate> generateLocalDate() {
        return generateLocalDate(DEFAULT_LOCAL_DATE_RANGE);
    }

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
        // Choosing a month, then a day for that month, rather than just choosing a day of the year.
        // This is to allow bias towards the first and last days of each month, if bias is enabled.
        return generateMonth().flatMap(month -> generateLocalDateForMonth(YearMonth.of(year.getValue(), month)));
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

    static Generator<LocalDateTime> generateLocalDateTime() {
        return generateLocalDateTime(DEFAULT_LOCAL_DATE_RANGE);
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
        LocalTime timeOnLastDay = max.toLocalTime();
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

    static Generator<Duration> generateDuration() {
        return generateDuration(DEFAULT_DURATION_RANGE);
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
