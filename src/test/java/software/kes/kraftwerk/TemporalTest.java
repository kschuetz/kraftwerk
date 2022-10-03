package software.kes.kraftwerk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.kraftwerk.constraints.DurationRange;
import software.kes.kraftwerk.constraints.LocalDateRange;
import software.kes.kraftwerk.constraints.LocalDateTimeRange;
import software.kes.kraftwerk.constraints.LocalTimeRange;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Assert.assertAlwaysInRange;
import static testsupport.CoversRange.coversRange;

class TemporalTest {
    @Nested
    @DisplayName("generateMonth")
    class GenerateMonth {
        @Test
        void coversAllMonths() {
            int[] f = new int[12];
            Generators.generateMonth()
                    .run()
                    .take(500)
                    .forEach(month -> f[month.ordinal()] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("generateDayOfWeek")
    class GenerateDayOfWeek {
        @Test
        void coversAllDaysOfWeek() {
            int[] f = new int[7];
            Generators.generateDayOfWeek()
                    .run()
                    .take(500)
                    .forEach(dayOfWeek -> f[dayOfWeek.ordinal()] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("generateLocalDate")
    class GenerateLocalDate {
        @Test
        void alwaysInRange() {
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(2019, 12, 31))
                            .to(LocalDate.of(2021, 1, 1)),
                    Generators::generateLocalDate);
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(2020, 9, 27))
                            .to(LocalDate.of(2020, 9, 27)),
                    Generators::generateLocalDate);
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(1, 1, 1))
                            .to(LocalDate.of(99999, 12, 31)),
                    Generators::generateLocalDate);
        }

        @Test
        void testCoversRange() {
            int[] f = new int[367];
            Generators.generateLocalDate(LocalDateRange.from(LocalDate.of(2020, 1, 1))
                            .to(LocalDate.of(2020, 12, 31)))
                    .run()
                    .take(5000)
                    .forEach(date -> f[date.getDayOfYear()] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("generateLocalDateForYear")
    class GenerateLocalDateForYear {
        @Test
        void alwaysInRange() {
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(2020, 1, 1))
                            .to(LocalDate.of(2020, 12, 31)),
                    __ -> Generators.generateLocalDateForYear(Year.of(2020)));
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(1900, 1, 1))
                            .to(LocalDate.of(1900, 12, 31)),
                    __ -> Generators.generateLocalDateForYear(Year.of(1900)));
        }

        @Test
        void testCoversRange() {
            int[] f = new int[366];
            Generators.generateLocalDateForYear(Year.of(2020))
                    .run()
                    .take(5000)
                    .forEach(date -> f[date.getDayOfYear() - 1] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("generateLocalDateForMonth")
    class GenerateLocalDateForMonth {
        @Test
        void alwaysInRange() {
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(2020, 1, 1))
                            .to(LocalDate.of(2020, 1, 31)),
                    __ -> Generators.generateLocalDateForMonth(YearMonth.of(2020, 1)));
            assertAlwaysInRange(LocalDateRange.from(LocalDate.of(1900, 12, 1))
                            .to(LocalDate.of(1900, 12, 31)),
                    __ -> Generators.generateLocalDateForMonth(YearMonth.of(1900, 12)));
        }

        @Test
        void testCoversRange() {
            int[] f = new int[30];
            Generators.generateLocalDateForMonth(YearMonth.of(2020, 9))
                    .run()
                    .take(500)
                    .forEach(date -> f[date.getDayOfMonth() - 1] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("generateLocalTime")
    class GenerateLocalTime {
        @Test
        void alwaysInRange() {
            assertAlwaysInRange(LocalTimeRange.from(LocalTime.of(1, 2, 3))
                            .to(LocalTime.of(23, 58, 59)),
                    Generators::generateLocalTime);
        }

        @Test
        void testCoversRange() {
            int[] f = new int[1440];
            Generators.generateLocalTime()
                    .run()
                    .take(10000)
                    .forEach(t -> f[t.getHour() * 60 + t.getMinute()] += 1);

            assertTrue(coversRange(f));
        }
    }

    @Nested
    @DisplayName("generateLocalDateTime")
    class GenerateLocalDateTime {
        @Test
        void alwaysInRange() {
            assertAlwaysInRange(LocalDateTimeRange.from(LocalDate.of(2019, 12, 31).atStartOfDay())
                            .to(LocalDate.of(2021, 1, 1).atStartOfDay()),
                    Generators::generateLocalDateTime);
            assertAlwaysInRange(LocalDateTimeRange.from(LocalDateTime.of(2020, 9, 27, 11, 12, 13, 0))
                            .to(LocalDateTime.of(2020, 9, 27, 13, 14, 15, 0)),
                    Generators::generateLocalDateTime);
        }

        @Test
        void fromDateRangeAlwaysInRange() {
            LocalDate startDate = LocalDate.of(2020, 9, 1);
            LocalDate endDate = LocalDate.of(2020, 9, 1);
            LocalDateTimeRange dateTimeRange = LocalDateTimeRange.exclusive(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
            assertAlwaysInRange(dateTimeRange, __ -> Generators.generateLocalDateTime(LocalDateRange.inclusive(startDate, endDate)));
        }

        @Test
        void testCoversRange() {
            int[] daysOfYear = new int[366];
            int[] hoursOfDay = new int[24];
            Generators.generateLocalDateTime(LocalDateRange.from(LocalDate.of(2020, 1, 1))
                            .to(LocalDate.of(2020, 12, 31)))
                    .run()
                    .take(5000)
                    .forEach(dateTime -> {
                        daysOfYear[dateTime.getDayOfYear() - 1] += 1;
                        hoursOfDay[dateTime.getHour()] += 1;
                    });

            assertTrue(coversRange(daysOfYear));
            assertTrue(coversRange(hoursOfDay));
        }
    }

    @Nested
    @DisplayName("generateDuration")
    class GenerateDuration {
        @Test
        void alwaysInRange() {
            assertAlwaysInRange(DurationRange.from(Duration.ofSeconds(1)).to(Duration.ofSeconds(1)),
                    Generators::generateDuration);
            assertAlwaysInRange(DurationRange.from(Duration.ofSeconds(1)).to(Duration.ofDays(10)),
                    Generators::generateDuration);
            assertAlwaysInRange(DurationRange.from(Duration.ofDays(1)).to(Duration.ofDays(10)),
                    Generators::generateDuration);
        }
    }
}
