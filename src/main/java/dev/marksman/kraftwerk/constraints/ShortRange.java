package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

public final class ShortRange implements Constraint<Short> {
    private static final ShortRange FULL = new ShortRange(Short.MIN_VALUE, Short.MAX_VALUE);

    private final short minInclusive;
    private final short maxInclusive;

    private ShortRange(short minInclusive, short maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public static ShortRangeFrom from(short minInclusive) {
        return new ShortRangeFrom() {
            @Override
            public ShortRange to(short maxInclusive) {
                return inclusive(minInclusive, maxInclusive);
            }

            @Override
            public ShortRange until(short maxExclusive) {
                return exclusive(minInclusive, maxExclusive);
            }
        };
    }

    public static ShortRange inclusive(short minInclusive, short maxInclusive) {
        validateRangeInclusive(minInclusive, maxInclusive);
        return new ShortRange(minInclusive, maxInclusive);
    }

    public static ShortRange exclusive(short maxExclusive) {
        return exclusive((short) 0, maxExclusive);
    }

    public static ShortRange exclusive(short minInclusive, short maxExclusive) {
        validateRangeExclusive(minInclusive, maxExclusive);
        return new ShortRange(minInclusive, (short) (maxExclusive - 1));
    }

    public static ShortRange fullRange() {
        return FULL;
    }

    public short minInclusive() {
        return minInclusive;
    }

    public short maxInclusive() {
        return maxInclusive;
    }

    @Override
    public boolean includes(Short n) {
        return n >= minInclusive && n <= maxInclusive;
    }

    public ShortRange withMinInclusive(short minInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public ShortRange withMaxInclusive(short maxInclusive) {
        return inclusive(minInclusive, maxInclusive);
    }

    public ShortRange withMaxExclusive(short maxExclusive) {
        return exclusive(minInclusive, maxExclusive);
    }

    public interface ShortRangeFrom {
        ShortRange to(short maxInclusive);

        ShortRange until(short maxExclusive);
    }

}
