package dev.marksman.kraftwerk.constraints;

import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeExclusive;
import static dev.marksman.kraftwerk.constraints.RangeInputValidation.validateRangeInclusive;

final class ConcreteShortRange implements ShortRange {
    private static final ShortRange FULL = new ConcreteShortRange(Short.MIN_VALUE, Short.MAX_VALUE);

    private final short min;
    private final short max;

    ConcreteShortRange(short min, short max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public short min() {
        return min;
    }

    @Override
    public short max() {
        return max;
    }

    static ShortRange concreteShortRange() {
        return FULL;
    }

    static ShortRange concreteShortRangeInclusive(short min, short max) {
        validateRangeInclusive(min, max);
        return new ConcreteShortRange(min, max);
    }

    static ShortRange concreteShortRangeExclusive(short min, short maxExclusive) {
        validateRangeExclusive(min, maxExclusive);
        return new ConcreteShortRange(min, (short) (maxExclusive - 1));
    }

    static ShortRangeFrom concreteShortRangeFrom(short min) {
        return new ShortRangeFrom() {
            @Override
            public ShortRange to(short max) {
                return concreteShortRangeInclusive(min, max);
            }

            @Override
            public ShortRange until(short maxExclusive) {
                return concreteShortRangeExclusive(min, maxExclusive);
            }
        };
    }

}
