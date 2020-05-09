package dev.marksman.kraftwerk.constraints;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LocalDateRange)) return false;
        final LocalDateRange other = (LocalDateRange) o;
        final Object this$minInclusive = this.minInclusive;
        final Object other$minInclusive = other.minInclusive;
        if (this$minInclusive == null ? other$minInclusive != null : !this$minInclusive.equals(other$minInclusive))
            return false;
        final Object this$maxInclusive = this.maxInclusive;
        final Object other$maxInclusive = other.maxInclusive;
        return this$maxInclusive == null ? other$maxInclusive == null : this$maxInclusive.equals(other$maxInclusive);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $minInclusive = this.minInclusive;
        result = result * PRIME + ($minInclusive == null ? 43 : $minInclusive.hashCode());
        final Object $maxInclusive = this.maxInclusive;
        result = result * PRIME + ($maxInclusive == null ? 43 : $maxInclusive.hashCode());
        return result;
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
