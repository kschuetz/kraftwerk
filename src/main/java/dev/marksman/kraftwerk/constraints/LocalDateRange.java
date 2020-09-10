package dev.marksman.kraftwerk.constraints;

import java.time.LocalDate;
import java.time.Period;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@link LocalDate}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link LocalDateRange#inclusive}, {@link LocalDateRange#exclusive}),
 * <p>
 * or by using {@link LocalDateRange#from}:
 * <pre>
 * LocalDateRange.from(LocalDate.of(2020, 1, 1)).to(LocalDate.of(2021, 1, 1))      // inclusive upper bound
 * LocalDateRange.from(LocalDate.of(2020, 1, 1)).until(LocalDate.of(2021, 1, 1))   // exclusive upper bound
 * </pre>
 */
public final class LocalDateRange implements Constraint<LocalDate>, Iterable<LocalDate> {
    private final LocalDate minInclusive;
    private final LocalDate maxInclusive;

    private LocalDateRange(LocalDate minInclusive, LocalDate maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code LocalDateRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code LocalDateRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link LocalDateRangeFrom}
     */
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

    /**
     * Creates a {@code LocalDateRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static LocalDateRange inclusive(LocalDate minInclusive, LocalDate maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new LocalDateRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code LocalDateRange} from {@code minInclusive}..{@code maxExclusive}.
     */
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

    /**
     * Creates a new {@code LocalDateRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code LocalDateRange}
     */
    public LocalDateRange withMinInclusive(LocalDate minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code LocalDateRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code LocalDateRange}
     */
    public LocalDateRange withMaxInclusive(LocalDate maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code LocalDateRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code LocalDateRange}
     */
    public LocalDateRange withMaxExclusive(LocalDate maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
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

    /**
     * A partially constructed {@link LocalDateRange}, with the lower bound already provided.
     */
    public interface LocalDateRangeFrom {
        /**
         * Creates a {@link LocalDateRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code LocalDateRange}
         */
        LocalDateRange to(LocalDate maxInclusive);

        /**
         * Creates a {@link LocalDateRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code LocalDateRange}
         */
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
