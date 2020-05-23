package dev.marksman.kraftwerk.constraints;

import java.time.LocalDateTime;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;
import static dev.marksman.kraftwerk.constraints.RangeToString.rangeToString;

public final class LocalDateTimeRange implements Constraint<LocalDateTime> {
    private final LocalDateTime minInclusive;
    private final LocalDateTime maxInclusive;

    private LocalDateTimeRange(LocalDateTime minInclusive, LocalDateTime maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

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

    public static LocalDateTimeRange inclusive(LocalDateTime minInclusive, LocalDateTime maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new LocalDateTimeRange(minInclusive, maxInclusive);
    }

    public static LocalDateTimeRange exclusive(LocalDateTime minInclusive, LocalDateTime maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
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

    public LocalDateTimeRange withMinInclusive(LocalDateTime minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public LocalDateTimeRange withMaxInclusive(LocalDateTime maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

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
        return rangeToString(getClass().getSimpleName(), minInclusive, true, maxInclusive, true);
    }

    public interface LocalDateTimeRangeFrom {
        LocalDateTimeRange to(LocalDateTime maxInclusive);

        LocalDateTimeRange until(LocalDateTime maxExclusive);
    }

}
