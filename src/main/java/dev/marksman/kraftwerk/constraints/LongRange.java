package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.ConcreteLongRange.*;

public interface LongRange {
    long min();

    long max();

    default boolean contains(long n) {
        return n >= min() && n <= max();
    }

    default LongRange withMin(long min) {
        return concreteLongRangeInclusive(min, max());
    }

    default LongRange withMax(long max) {
        return concreteLongRangeInclusive(min(), max);
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
