package dev.marksman.kraftwerk.constraints;

final class ConcreteIntRange implements IntRange {
    private final int min;
    private final int max;

    ConcreteIntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int min() {
        return min;
    }

    @Override
    public int max() {
        return max;
    }

    static IntRange concreteIntRangeInclusive(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("max must be >= min");
        }
        return new ConcreteIntRange(min, max);
    }

    static IntRange concreteIntRangeExclusive(int min, int maxExclusive) {
        if (maxExclusive <= min) {
            throw new IllegalArgumentException("max must be > min");
        }
        return new ConcreteIntRange(min, maxExclusive - 1);
    }

    static IntRangeFrom concreteIntRangeFrom(int min) {
        return new IntRangeFrom() {
            @Override
            public IntRange to(int max) {
                return concreteIntRangeInclusive(min, max);
            }

            @Override
            public IntRange until(int maxExclusive) {
                return concreteIntRangeExclusive(min, maxExclusive - 1);
            }
        };
    }

}
