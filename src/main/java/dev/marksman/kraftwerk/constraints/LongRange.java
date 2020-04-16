package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class LongRange implements Constraint<Long> {
    private static final LongRange FULL = new LongRange(Long.MIN_VALUE, Long.MAX_VALUE);

    private final long minInclusive;
    private final long maxInclusive;

    private LongRange(long minInclusive, long maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public static LongRangeFrom from(long minInclusive) {
        return new LongRangeFrom() {
            @Override
            public LongRange to(long maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public LongRange until(long maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    public static LongRange inclusive(long minInclusive, long maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new LongRange(minInclusive, maxInclusive);
    }

    public static LongRange exclusive(long minInclusive, long maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new LongRange(minInclusive, maxExclusive - 1);
    }

    public static LongRange exclusive(long maxExclusive) {
        return exclusive(0, maxExclusive);
    }

    public static LongRange fullRange() {
        return FULL;
    }

    public long minInclusive() {
        return minInclusive;
    }

    public long maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Long n) {
        return n >= minInclusive && n <= maxInclusive;
    }

    public LongRange withMinInclusive(long minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public LongRange withMaxInclusive(long maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public LongRange withMaxExclusive(long maxExclusive) {
        return inclusive(minInclusive, maxExclusive);
    }

    public interface LongRangeFrom {
        LongRange to(long maxInclusive);

        LongRange until(long maxExclusive);
    }

}
