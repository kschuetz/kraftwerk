package dev.marksman.kraftwerk.constraints;

final class ConcreteDoubleRange implements DoubleRange {
    private static final DoubleRange FULL = new ConcreteDoubleRange(Double.MIN_VALUE, true, Double.MAX_VALUE, true);

    private final double min;
    private final boolean minIncluded;
    private final double max;
    private final boolean maxIncluded;

    ConcreteDoubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
        this.min = min;
        this.max = max;
        this.minIncluded = minIncluded;
        this.maxIncluded = maxIncluded;
    }

    @Override
    public double min() {
        return min;
    }

    @Override
    public double max() {
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

    static DoubleRange concreteDoubleRange() {
        return FULL;
    }

    static DoubleRange concreteDoubleRange(double min, boolean minIncluded, double max, boolean maxIncluded) {
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
                // representable double exists between min and max
                if (max == Math.nextAfter(min, Double.POSITIVE_INFINITY)) {
                    throw new IllegalArgumentException("range is too narrow");
                }
            }
        }
        return new ConcreteDoubleRange(min, minIncluded, max, maxIncluded);
    }

    static DoubleRangeFrom concreteDoubleRangeFrom(double min, boolean minIncluded) {
        return new DoubleRangeFrom() {
            @Override
            public DoubleRange to(double max) {
                return concreteDoubleRange(min, minIncluded, max, true);
            }

            @Override
            public DoubleRange until(double maxExclusive) {
                return concreteDoubleRange(min, minIncluded, maxExclusive, false);
            }
        };
    }

}
