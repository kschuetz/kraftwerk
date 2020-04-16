package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class IntRange implements Constraint<Integer> {
    private static final IntRange FULL = new IntRange(Integer.MIN_VALUE, Integer.MAX_VALUE);

    private final int minInclusive;
    private final int maxInclusive;

    private IntRange(int minInclusive, int maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public static IntRangeFrom from(int minInclusive) {
        return new IntRangeFrom() {
            @Override
            public IntRange to(int maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public IntRange until(int maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    public static IntRange inclusive(int minInclusive, int maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new IntRange(minInclusive, maxInclusive);
    }

    public static IntRange exclusive(int minInclusive, int maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new IntRange(minInclusive, maxExclusive - 1);
    }

    public static IntRange exclusive(int maxExclusive) {
        return exclusive(0, maxExclusive);
    }

    public static IntRange fullRange() {
        return FULL;
    }

    public int minInclusive() {
        return minInclusive;
    }

    public int maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Integer value) {
        return value >= minInclusive && value <= maxInclusive;
    }

    public IntRange withMinInclusive(int minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public IntRange withMaxInclusive(int maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public IntRange withMaxExclusive(int maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    public interface IntRangeFrom {
        IntRange to(int maxInclusive);

        IntRange until(int maxExclusive);
    }

}
