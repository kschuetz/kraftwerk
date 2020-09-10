package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.time.Duration;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

/**
 * A range of {@link Duration}s.  Like all ranges, it is immutable and its span always includes a minimum of one value.
 * <p>
 * Construct using one of the static methods ({@link DurationRange#inclusive}, {@link DurationRange#exclusive}),
 * <p>
 * or by using {@link DurationRange#from}:
 * <pre>
 * DurationRange.from(Duration.ZERO).to(Duration.ofDays(10))      // inclusive upper bound
 * DurationRange.from(Duration.ZERO).until(Duration.ofDays(10))   // exclusive upper bound
 * </pre>
 */
public final class DurationRange implements Constraint<Duration> {
    private final Duration minInclusive;
    private final Duration maxInclusive;

    private DurationRange(Duration minInclusive, Duration maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    /**
     * Partially constructs a {@code DurationRange} with its lower bound.
     * <p>
     * With the result, you can call {@code to} or {@code until} with the upper bound to create the {@code DurationRange}.
     *
     * @param minInclusive the lower bound (inclusive) of the range
     * @return a {@link DurationFrom}
     */
    public static DurationFrom from(Duration minInclusive) {
        return new DurationFrom() {
            @Override
            public DurationRange to(Duration maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public DurationRange until(Duration maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    /**
     * Creates a {@code DurationRange} from {@code minInclusive}..{@code maxInclusive}.
     */
    public static DurationRange inclusive(Duration minInclusive, Duration maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new DurationRange(minInclusive, maxInclusive);
    }

    /**
     * Creates a {@code DurationRange} from {@link Duration#ZERO}..{@code maxInclusive}.
     */
    public static DurationRange inclusive(Duration maxInclusive) {
        return inclusive(Duration.ZERO, maxInclusive);
    }

    /**
     * Creates a {@code DurationRange} from {@code minInclusive}..{@code maxExclusive}.
     */
    public static DurationRange exclusive(Duration minInclusive, Duration maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new DurationRange(minInclusive, maxExclusive.minusDays(1));
    }

    /**
     * Creates a {@code DurationRange} from {@link Duration#ZERO}..{@code maxExclusive}.
     */
    public static DurationRange exclusive(Duration maxExclusive) {
        return exclusive(Duration.ZERO, maxExclusive);
    }

    public Duration minInclusive() {
        return minInclusive;
    }

    public Duration maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Duration value) {
        return GTE.gte(minInclusive, value) && LTE.lte(maxInclusive).apply(value);
    }

    /**
     * Creates a new {@code DurationRange} that is the same as this one, with a new lower bound.
     *
     * @param minInclusive the new lower bound (inclusive) for the range; must not exceed this range's upper bound
     * @return a {@code DurationRange}
     */
    public DurationRange withMinInclusive(Duration minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code DurationRange} that is the same as this one, with a new upper bound.
     *
     * @param maxInclusive the new upper bound (inclusive) for the range; must not be less than this range's lower bound
     * @return a {@code DurationRange}
     */
    public DurationRange withMaxInclusive(Duration maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    /**
     * Creates a new {@code DurationRange} that is the same as this one, with a new upper bound.
     *
     * @param maxExclusive the new upper bound (exclusive) for the range; must be greater than this range's lower bound
     * @return a {@code DurationRange}
     */
    public DurationRange withMaxExclusive(Duration maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DurationRange that = (DurationRange) o;

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
     * A partially constructed {@link DurationRange}, with the lower bound already provided.
     */
    public interface DurationFrom {
        /**
         * Creates a {@link DurationRange} from the already provided lower bound to {@code maxInclusive}.
         *
         * @param maxInclusive the upper bound (inclusive) of the range
         * @return a {@code DurationRange}
         */
        DurationRange to(Duration maxInclusive);

        /**
         * Creates a {@link DurationRange} from the already provided lower bound to {@code maxExclusive}.
         *
         * @param maxExclusive the upper bound (exclusive) of the range
         * @return a {@code DurationRange}
         */
        DurationRange until(Duration maxExclusive);
    }
}
