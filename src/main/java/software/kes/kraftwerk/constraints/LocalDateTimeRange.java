package software.kes.kraftwerk.constraints;

import java.time.LocalDateTime;

/**
 * A range of {@link LocalDateTime}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link LocalDateTimeRange#inclusive}, {@link LocalDateTimeRange#exclusive}),
 * <p>
 * or by using {@link LocalDateTimeRange#from}:
 * <pre>
 * LocalDateTimeRange.from(LocalDateTime.of(2020, 1, 1, 12, 0, 0)).to(LocalDateTime.of(2021, 1, 1, 12, 0, 0))      // inclusive upper bound
 * LocalDateTimeRange.from(LocalDateTime.of(2020, 1, 1, 12, 0, 0)).until(LocalDateTime.of(2021, 1, 1, 12, 0, 0))   // exclusive upper bound
 * </pre>
 */
public final class LocalDateTimeRange implements Constraint<LocalDateTime> {
    private final LocalDateTime minInclusive;
    private final LocalDateTime maxInclusive;

    private LocalDateTimeRange(LocalDateTime minInclusive, LocalDateTime maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code LocalDateTimeRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code LocalDateTimeRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link LocalDateTimeRangeFrom}
     */
    public static LocalDateTimeRangeFrom from(LocalDateTime minInclusive) {
        return new LocalDateTimeRangeFrom() {
            @Override
            public LocalDateTimeRange to(LocalDateTime maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public LocalDateTimeRange until(LocalDateTime maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code LocalDateTimeRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static LocalDateTimeRange inclusive(LocalDateTime minInclusive, LocalDateTime maxInclusive) {
        RangeInputValidation.validateRangeInclusive(minInclusive, maxInclusive);
        return new LocalDateTimeRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code LocalDateTimeRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static LocalDateTimeRange exclusive(LocalDateTime minInclusive, LocalDateTime maxExclusive) {
        RangeInputValidation.validateRangeExclusive(minInclusive, maxExclusive);
        return new LocalDateTimeRange(minInclusive, maxExclusive.minusNanos(1));
    }

    public LocalDateTime minInclusive() {
        return minInclusive;
    }

    public LocalDateTime maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(LocalDateTime dateTime) {
        return !(dateTime.isBefore(minInclusive) || dateTime.isAfter(maxInclusive));
    }

    /**
     * Creates a new {@code LocalDateTimeRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code LocalDateTimeRange}
     */
    public LocalDateTimeRange withMinInclusive(LocalDateTime minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code LocalDateTimeRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code LocalDateTimeRange}
     */
    public LocalDateTimeRange withMaxInclusive(LocalDateTime maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code LocalDateTimeRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code LocalDateTimeRange}
     */
    public LocalDateTimeRange withMaxExclusive(LocalDateTime maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalDateTimeRange that = (LocalDateTimeRange) o;

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
        return RangeToString.rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    /**
     * A partially constructed {@link LocalDateTimeRange}, with the lower bound already provided.
     */
    public interface LocalDateTimeRangeFrom {
        /**
         * Creates a {@link LocalDateTimeRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code LocalDateTimeRange}
         */
        LocalDateTimeRange to(LocalDateTime maxInclusive);

        /**
         * Creates a {@link LocalDateTimeRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code LocalDateTimeRange}
         */
        LocalDateTimeRange until(LocalDateTime maxExclusive);
    }
}
