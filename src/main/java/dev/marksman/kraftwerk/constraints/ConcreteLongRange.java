package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteLongRange implements LongRange {
    private static final LongRange FULL = new ConcreteLongRange(Long.MIN_VALUE, Long.MAX_VALUE);

    private final long min;
    private final long max;

    ConcreteLongRange(long min, long max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public long minInclusive() {
        return min;
    }

    @Override
    public long maxInclusive() {
        return max;
    }

    static LongRange concreteLongRange() {
        return FULL;
    }

    static LongRange concreteLongRangeInclusive(long min, long max) {
        validateRangeInclusive(min, max);
        return new ConcreteLongRange(min, max);
    }

    static LongRange concreteLongRangeExclusive(long min, long maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
        return new ConcreteLongRange(min, maxExclusive - 1);
    }

    static LongRangeFrom concreteLongRangeFrom(long min) {
        return new LongRangeFrom() {
            @Override
            public LongRange to(long max) {
                return concreteLongRangeInclusive(min, max);
            }

            @Override
            public LongRange until(long maxExclusive) {
                return concreteLongRangeExclusive(min, maxExclusive);
            }
        };
    }

}
