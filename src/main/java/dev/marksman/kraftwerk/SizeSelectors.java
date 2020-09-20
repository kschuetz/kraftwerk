package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.core.BuildingBlocks;

import static dev.marksman.kraftwerk.Result.result;

final class SizeSelectors {
    private static final int DEFAULT_RANGE = 16;

    private SizeSelectors() {
    }

    private static Result<Seed, Boolean> shouldUsePreferred(Seed input) {
        Result<? extends Seed, Integer> s1 = BuildingBlocks.nextIntBounded(7, input);
        return result(s1.getNextState(), s1.getValue() < 2);
    }

    public static MinMaxPreferred minMaxPreferredSizeSelector(int min, int max, int preferred) {
        return new MinMaxPreferred(min, max, preferred);
    }

    public static MinMax minMaxSizeSelector(int min, int max) {
        min = Math.max(0, min);
        max = Math.max(max, min);
        return new MinMax(min, max);
    }

    public static MinPreferred minPreferredSizeSelector(int min, int preferred) {
        min = Math.max(0, min);
        preferred = Math.max(0, preferred);
        return new MinPreferred(min, preferred);
    }

    public static MinOnly minOnlySizeSelector(int min) {
        min = Math.max(0, min);
        return new MinOnly(min);
    }

    public static MaxPreferred maxPreferredSizeSelector(int max, int preferred) {
        max = Math.max(0, max);
        preferred = Math.max(0, preferred);
        return new MaxPreferred(max, preferred);
    }

    public static MaxOnly maxOnlySizeSelector(int max) {
        max = Math.max(0, max);
        return new MaxOnly(max);
    }

    public static PreferredOnly preferredOnlySizeSelector(int preferred) {
        preferred = Math.max(0, preferred);
        return new PreferredOnly(preferred);
    }

    public static NoSizeParameters noSizeParametersSizeSelector() {
        return NoSizeParameters.INSTANCE;
    }

    public static SizeSelector sizeSelector(SizeParameters sp) {
        return sp.getMinSize()
                .match(_a -> sp.getMaxSize()
                                .match(_b -> sp.getPreferredSize()
                                                .match(_c -> noSizeParametersSizeSelector(),
                                                        SizeSelectors::preferredOnlySizeSelector),
                                        maxSize -> sp.getPreferredSize()
                                                .match(_d -> maxOnlySizeSelector(maxSize),
                                                        preferred -> maxPreferredSizeSelector(maxSize, preferred))),
                        minSize -> sp.getMaxSize()
                                .match(_e -> sp.getPreferredSize()
                                                .match(_f -> minOnlySizeSelector(minSize),
                                                        preferred -> minPreferredSizeSelector(minSize, preferred)),
                                        maxSize -> sp.getPreferredSize()
                                                .match(_g -> minMaxSizeSelector(minSize, maxSize),
                                                        preferred -> minMaxPreferredSizeSelector(minSize, maxSize, preferred))
                                ));
    }

    public static final class MinMaxPreferred implements SizeSelector {
        private final int min;
        private final int max;
        private final int preferred;

        private MinMaxPreferred(int min, int max, int preferred) {
            this.min = min;
            this.max = max;
            this.preferred = preferred;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            Result<Seed, Boolean> s1 = shouldUsePreferred(input);
            if (s1.getValue()) {
                return result(s1.getNextState(), preferred);
            } else {
                return BuildingBlocks.nextIntBetween(min, max, s1.getNextState());
            }
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getPreferred() {
            return this.preferred;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinMaxPreferred that = (MinMaxPreferred) o;

            if (min != that.min) return false;
            if (max != that.max) return false;
            return preferred == that.preferred;
        }

        @Override
        public int hashCode() {
            int result = min;
            result = 31 * result + max;
            result = 31 * result + preferred;
            return result;
        }

        @Override
        public String toString() {
            return "MinMaxPreferred{" +
                    "min=" + min +
                    ", max=" + max +
                    ", preferred=" + preferred +
                    '}';
        }
    }

    public static final class MinMax implements SizeSelector {
        private final int min;
        private final int max;

        private MinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBetween(min, max, input);
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinMax minMax = (MinMax) o;

            if (min != minMax.min) return false;
            return max == minMax.max;
        }

        @Override
        public int hashCode() {
            int result = min;
            result = 31 * result + max;
            return result;
        }

        @Override
        public String toString() {
            return "MinMax{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }

    public static final class MinPreferred implements SizeSelector {
        private final int min;
        private final int preferred;

        private MinPreferred(int min, int preferred) {
            this.min = min;
            this.preferred = preferred;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            Result<Seed, Boolean> s1 = shouldUsePreferred(input);
            if (s1.getValue()) {
                return result(s1.getNextState(), preferred);
            } else {
                return BuildingBlocks.nextIntBetween(min, min + DEFAULT_RANGE, s1.getNextState());
            }
        }

        public int getMin() {
            return this.min;
        }

        public int getPreferred() {
            return this.preferred;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinPreferred that = (MinPreferred) o;

            if (min != that.min) return false;
            return preferred == that.preferred;
        }

        @Override
        public int hashCode() {
            int result = min;
            result = 31 * result + preferred;
            return result;
        }

        @Override
        public String toString() {
            return "MinPreferred{" +
                    "min=" + min +
                    ", preferred=" + preferred +
                    '}';
        }
    }

    public static final class MinOnly implements SizeSelector {
        private final int min;

        private MinOnly(int min) {
            this.min = min;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBetween(min, min + DEFAULT_RANGE, input);
        }

        public int getMin() {
            return this.min;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MinOnly minOnly = (MinOnly) o;

            return min == minOnly.min;
        }

        @Override
        public int hashCode() {
            return min;
        }

        @Override
        public String toString() {
            return "MinOnly{" +
                    "min=" + min +
                    '}';
        }
    }

    public static final class MaxPreferred implements SizeSelector {
        private final int max;
        private final int preferred;

        private MaxPreferred(int max, int preferred) {
            this.max = max;
            this.preferred = preferred;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            Result<Seed, Boolean> s1 = shouldUsePreferred(input);
            if (s1.getValue()) {
                return result(s1.getNextState(), preferred);
            } else {
                return BuildingBlocks.nextIntBounded(max, s1.getNextState());
            }
        }

        public int getMax() {
            return this.max;
        }

        public int getPreferred() {
            return this.preferred;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MaxPreferred that = (MaxPreferred) o;

            if (max != that.max) return false;
            return preferred == that.preferred;
        }

        @Override
        public int hashCode() {
            int result = max;
            result = 31 * result + preferred;
            return result;
        }

        @Override
        public String toString() {
            return "MaxPreferred{" +
                    "max=" + max +
                    ", preferred=" + preferred +
                    '}';
        }
    }

    public static final class MaxOnly implements SizeSelector {
        private final int max;

        private MaxOnly(int max) {
            this.max = max;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBounded(max, input);
        }

        public int getMax() {
            return this.max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MaxOnly maxOnly = (MaxOnly) o;

            return max == maxOnly.max;
        }

        @Override
        public int hashCode() {
            return max;
        }

        @Override
        public String toString() {
            return "MaxOnly{" +
                    "max=" + max +
                    '}';
        }
    }

    public static final class PreferredOnly implements SizeSelector {
        private final int preferred;

        private PreferredOnly(int preferred) {
            this.preferred = preferred;
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return result(input, preferred);
        }

        public int getPreferred() {
            return this.preferred;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PreferredOnly that = (PreferredOnly) o;

            return preferred == that.preferred;
        }

        @Override
        public int hashCode() {
            return preferred;
        }

        @Override
        public String toString() {
            return "PreferredOnly{" +
                    "preferred=" + preferred +
                    '}';
        }
    }

    public static final class NoSizeParameters implements SizeSelector {
        private static final NoSizeParameters INSTANCE = new NoSizeParameters();

        private NoSizeParameters() {
        }

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBounded(DEFAULT_RANGE, input);
        }

        @Override
        public String toString() {
            return "NoSizeParameters{}";
        }
    }
}
