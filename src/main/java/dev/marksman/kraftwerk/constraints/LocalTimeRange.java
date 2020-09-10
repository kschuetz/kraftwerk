package dev.marksman.kraftwerk.constraints;

import java.time.LocalTime;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@link LocalTime}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link LocalTimeRange#inclusive}, {@link LocalTimeRange#exclusive}),
 * <p>
 * or by using {@link LocalTimeRange#from}:
 * <pre>
 * LocalTimeRange.from(LocalTime.of(0, 0)).to(LocalTime.of(12, 0))      // inclusive upper bound
 * LocalTimeRange.from(LocalTime.of(0, 0)).until(LocalTime.of(12, 0))   // exclusive upper bound
 * </pre>
 */
public final class LocalTimeRange implements Constraint<LocalTime> {
    private static final LocalTimeRange FULL = new LocalTimeRange(LocalTime.MIN, LocalTime.MAX, true);

    private final LocalTime minInclusive;
    private final LocalTime max;
    private final boolean maxIncluded;

    private LocalTimeRange(LocalTime minInclusive, LocalTime max, boolean maxIncluded) {
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxIncluded = maxIncluded;
    }

    /**
     * Partially constructs a {@code LocalTimeRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code LocalTimeRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link LocalTimeRangeFrom}
     */
    public static LocalTimeRangeFrom from(LocalTime minInclusive) {
        return new LocalTimeRangeFrom() {
            @Override
            public LocalTimeRange to(LocalTime maxInclusive) {
                return localTimeRange(minInclusive, maxInclusive, true);
            }

            @Override
            public LocalTimeRange until(LocalTime maxExclusive) {
                return localTimeRange(minInclusive, maxExclusive, false);
            }
        };
    }

    /**
     * Creates a {@code LocalTimeRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static LocalTimeRange inclusive(LocalTime minInclusive, LocalTime maxInclusive) {
        return localTimeRange(minInclusive, maxInclusive, true);
    }

    /**
     * Creates a {@code LocalTimeRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static LocalTimeRange exclusive(LocalTime minInclusive, LocalTime maxExclusive) {
        return localTimeRange(minInclusive, maxExclusive, false);
    }

    /**
     * Creates a {@code LocalTimeRange} that includes all possible {@code LocalTime}s ({@link LocalTime#MIN}..{@link LocalTime#MAX}).
     */
    public static LocalTimeRange fullRange() {
        return FULL;
    }

    /**
     * Creates a {@code LocalTimeRange}.
     */
    public static LocalTimeRange localTimeRange(LocalTime minInclusive, LocalTime max, boolean maxIncluded) {
        if (maxIncluded) {
            validateRangeInclusive(minInclusive, max);
        } else {
            validateRangeExclusive(minInclusive, max);
        }
        return new LocalTimeRange(minInclusive, max, maxIncluded);
    }

    public LocalTime minInclusive() {
        return minInclusive;
    }

    public LocalTime max() {
        return max;
    }

    public boolean maxIncluded() {
        return maxIncluded;
    }

    @Override
    public boolean includes(LocalTime time) {
        boolean beforeMin = time.isBefore(minInclusive);
        boolean afterMax = maxIncluded
                ? time.isAfter(max)
                : !(time.isBefore(max));

        return !(beforeMin || afterMax);
    }

    /**
     * Creates a new {@code LocalTimeRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code LocalTimeRange}
     */
    public LocalTimeRange withMinInclusive(LocalTime minInclusive) {
        return localTimeRange(minInclusive, max, maxIncluded);
    }

    /**
     * Creates a new {@code LocalTimeRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code LocalTimeRange}
     */
    public LocalTimeRange withMaxInclusive(LocalTime maxInclusive) {
        return localTimeRange(minInclusive, maxInclusive, true);
    }

    /**
     * Creates a new {@code LocalTimeRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code LocalTimeRange}
     */
    public LocalTimeRange withMaxExclusive(LocalTime maxExclusive) {
        return localTimeRange(minInclusive, maxExclusive, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalTimeRange that = (LocalTimeRange) o;

        if (maxIncluded != that.maxIncluded) return false;
        if (!minInclusive.equals(that.minInclusive)) return false;
        return max.equals(that.max);
    }

    @Override
    public int hashCode() {
        int result = minInclusive.hashCode();
        result = 31 * result + max.hashCode();
        result = 31 * result + (maxIncluded ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return rangeToString(getClass().getSimpleName(), minInclusive, true, max, maxIncluded);
    }

    /**
     * A partially constructed {@link LocalTimeRange}, with the lower bound already provided.
     */
    public interface LocalTimeRangeFrom {
        /**
         * Creates a {@link LocalTimeRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code LocalTimeRange}
         */
        LocalTimeRange to(LocalTime maxInclusive);

        /**
         * Creates a {@link LocalTimeRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code LocalTimeRange}
         */
        LocalTimeRange until(LocalTime maxExclusive);
    }
}
