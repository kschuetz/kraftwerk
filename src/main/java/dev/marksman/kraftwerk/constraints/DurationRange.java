package dev.marksman.kraftwerk.constraints;

import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;

import java.time.Duration;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class DurationRange implements Constraint<Duration> {
    private final Duration minInclusive;
    private final Duration maxInclusive;

    private DurationRange(Duration minInclusive, Duration maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static DurationRange inclusive(Duration minInclusive, Duration maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new DurationRange(minInclusive, maxInclusive);
    }

    public static DurationRange inclusive(Duration maxInclusive) {
        return inclusive(Duration.ZERO, maxInclusive);
    }

    public static DurationRange exclusive(Duration minInclusive, Duration maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new DurationRange(minInclusive, maxExclusive.minusDays(1));
    }

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

    public DurationRange withMinInclusive(Duration minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public DurationRange withMaxInclusive(Duration maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

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

    public interface DurationFrom {
        DurationRange to(Duration maxInclusive);

        DurationRange until(Duration maxExclusive);
    }

}
