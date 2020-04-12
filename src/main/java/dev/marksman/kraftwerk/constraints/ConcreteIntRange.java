package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteIntRange implements IntRange {
    private static final IntRange FULL = new ConcreteIntRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

    private final int min;
    private final int max;

    ConcreteIntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int minInclusive() {
        return min;
    }

    @Override
    public int maxInclusive() {
        return max;
    }

    static IntRange concreteIntRange() {
        return FULL;
    }

    static IntRange concreteIntRangeInclusive(int min, int max) {
        validateRangeInclusive(min, max);
        return new ConcreteIntRange(min, max);
    }

    static IntRange concreteIntRangeExclusive(int min, int maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
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
                return concreteIntRangeExclusive(min, maxExclusive);
            }
        };
    }

}
