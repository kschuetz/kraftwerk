package dev.marksman.kraftwerk.constraints;

final class ConcreteLongRange implements LongRange {
    private final long min;
    private final long max;

    ConcreteLongRange(long min, long max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public long min() {
        return min;
    }

    @Override
    public long max() {
        return max;
    }

    static LongRange concreteLongRangeInclusive(long min, long max) {
        if (max < min) {
            throw new IllegalArgumentException("max must be >= min");
        }
        return new ConcreteLongRange(min, max);
    }

    static LongRange concreteLongRangeExclusive(long min, long maxExclusive) {
        if (maxExclusive <= min) {
            throw new IllegalArgumentException("max must be > min");
        }
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
