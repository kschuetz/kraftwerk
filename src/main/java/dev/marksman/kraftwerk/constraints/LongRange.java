package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteLongRange.concreteLongRange;
import static dev.marksman.kraftwerk.constraints.ConcreteLongRange.concreteLongRangeExclusive;
import static dev.marksman.kraftwerk.constraints.ConcreteLongRange.concreteLongRangeFrom;
import static dev.marksman.kraftwerk.constraints.ConcreteLongRange.concreteLongRangeInclusive;

public interface LongRange extends Constraint<Long> {
    long minInclusive();

    long maxInclusive();

    @Override
    default boolean includes(Long n) {
        return n >= minInclusive() && n <= maxInclusive();
    }

    default LongRange withMin(long min) {
        return concreteLongRangeInclusive(min, maxInclusive());
    }

    default LongRange withMaxInclusive(long max) {
        return concreteLongRangeInclusive(minInclusive(), max);
    }

    default LongRange withMaxExclusive(long maxExclusive) {
        return concreteLongRangeInclusive(minInclusive(), maxExclusive);
    }

    static LongRangeFrom from(long min) {
        return concreteLongRangeFrom(min);
    }

    static LongRange inclusive(long min, long max) {
        return concreteLongRangeInclusive(min, max);
    }

    static LongRange exclusive(long min, long maxExclusive) {
        return concreteLongRangeExclusive(min, maxExclusive);
    }

    static LongRange exclusive(long maxExclusive) {
        return concreteLongRangeExclusive(0, maxExclusive);
    }

    static LongRange fullRange() {
        return concreteLongRange();
    }

    interface LongRangeFrom {
        LongRange to(long max);

        LongRange until(long maxExclusive);
    }
}
