package dev.marksman.kraftwerk.constraints;

import java.time.LocalTime;

import static dev.marksman.kraftwerk.constraints.ConcreteLocalTimeRange.concreteLocalTimeRange;
import static dev.marksman.kraftwerk.constraints.ConcreteLocalTimeRange.concreteLocalTimeRangeFrom;

public interface LocalTimeRange extends Constraint<LocalTime> {
    LocalTime minInclusive();

    LocalTime max();

    boolean maxIncluded();

    @Override
    default boolean includes(LocalTime time) {
        return !(time.isBefore(minInclusive()) || time.isAfter(max()));
    }

    default LocalTimeRange withMin(LocalTime min) {
        return concreteLocalTimeRange(min, max(), maxIncluded());
    }

    default LocalTimeRange withMaxInclusive(LocalTime max) {
        return concreteLocalTimeRange(minInclusive(), max, true);
    }

    default LocalTimeRange withMaxExclusive(LocalTime maxExclusive) {
        return concreteLocalTimeRange(minInclusive(), maxExclusive, false);
    }

    static LocalTimeRangeFrom from(LocalTime min) {
        return concreteLocalTimeRangeFrom(min);
    }

    static LocalTimeRange inclusive(LocalTime min, LocalTime max) {
        return concreteLocalTimeRange(min, max, true);
    }

    static LocalTimeRange exclusive(LocalTime min, LocalTime maxExclusive) {
        return concreteLocalTimeRange(min, maxExclusive, false);
    }

    static LocalTimeRange fullRange() {
        return concreteLocalTimeRange();
    }

    interface LocalTimeRangeFrom {
        LocalTimeRange to(LocalTime max);

        LocalTimeRange until(LocalTime maxExclusive);
    }
}
