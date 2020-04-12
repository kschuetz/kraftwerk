package dev.marksman.kraftwerk.constraints;

final class ConcreteFloatRange implements FloatRange {
    private static final FloatRange FULL = new ConcreteFloatRange(Float.MIN_VALUE, true, Float.MAX_VALUE, true);

    private final float min;
    private final boolean minIncluded;
    private final float max;
    private final boolean maxIncluded;

    ConcreteFloatRange(float min, boolean minIncluded, float max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    @Override
    public float min() {
        return min;
    }

    @Override
    public float max() {
        return max;
    }

    @Override
    public boolean minIncluded() {
        return minIncluded;
    }

    @Override
    public boolean maxIncluded() {
        return maxIncluded;
    }

    static FloatRange concreteFloatRange() {
        return FULL;
    }

    static FloatRange concreteFloatRange(float min, boolean minIncluded, float max, boolean maxIncluded) {
        if (minIncluded && maxIncluded) {
            if (max < min) {
                throw new IllegalArgumentException("max must be >= min");
            }
        } else {
            if (max <= min) {
                throw new IllegalArgumentException("max must be > min");
            }

            if (!(minIncluded || maxIncluded)) {
                // if both min and max are excluded, we need to be sure at least one
                // representable float exists between min and max
                if (max == Math.nextAfter(min, Float.POSITIVE_INFINITY)) {
                    throw new IllegalArgumentException("range is too narrow");
                }
            }
        }
        return new ConcreteFloatRange(min, minIncluded, max, maxIncluded);
    }

    static FloatRangeFrom concreteFloatRangeFrom(float min, boolean minIncluded) {
        return new FloatRangeFrom() {
            @Override
            public FloatRange to(float max) {
                return concreteFloatRange(min, minIncluded, max, true);
            }

            @Override
            public FloatRange until(float maxExclusive) {
                return concreteFloatRange(min, minIncluded, maxExclusive, false);
            }
        };
    }

}
