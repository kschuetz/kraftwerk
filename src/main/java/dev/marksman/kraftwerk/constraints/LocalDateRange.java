package dev.marksman.kraftwerk.constraints;

import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class LocalDateRange implements Constraint<LocalDate>, Iterable<LocalDate> {
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

    @Override
    public Iterator<LocalDate> iterator() {
        return new LocalDateRangeIterator(minInclusive, maxInclusive);
    }

    public LocalDate minInclusive() {
        return minInclusive;
    }

    public LocalDate maxInclusive() {
        return maxInclusive;
    }

    public Period toPeriod() {
        return Period.between(minInclusive, maxInclusive.plusDays(1));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalDateRange that = (LocalDateRange) o;

        if (!minInclusive.equals(that.minInclusive)) return false;
        return maxInclusive.equals(that.maxInclusive);
    }

    @Override
    public int hashCode() {
        int result = minInclusive.hashCode();
        result = 31 * result + maxInclusive.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    public interface LocalDateRangeFrom {
        LocalDateRange to(LocalDate maxInclusive);

        LocalDateRange until(LocalDate maxExclusive);
    }

    private static class LocalDateRangeIterator implements Iterator<LocalDate> {
        private final LocalDate max;
        private LocalDate current;

        public LocalDateRangeIterator(LocalDate current, LocalDate max) {
            this.current = current;
            this.max = max;
        }

        @Override
        public boolean hasNext() {
            return current.compareTo(max) <= 0;
        }

        @Override
        public LocalDate next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            LocalDate result = current;
            current = current.plusDays(1);
            return result;
        }
    }
}
