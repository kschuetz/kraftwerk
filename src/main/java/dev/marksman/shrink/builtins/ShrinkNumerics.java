package dev.marksman.shrink.builtins;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.shrink.Shrink;

import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;
import static dev.marksman.shrink.util.LazyCons.lazyCons;

public class ShrinkNumerics {

    private static Shrink<Integer> INT = new Shrink<Integer>() {
        @Override
        public ImmutableFiniteIterable<Integer> apply(Integer input) {
            if (input == 0) {
                return emptyImmutableIterable();
            } else {
                return halves(input);
            }
        }

        private ImmutableFiniteIterable<Integer> halves(Integer x) {
            int q = x / 2;
            if (q == 0) {
                return ImmutableIterable.of(0);
            } else {
                return lazyCons(q, () -> lazyCons(-q, () -> halves(q)));
            }
        }

    };

    private static Shrink<Long> LONG = new Shrink<Long>() {
        @Override
        public ImmutableFiniteIterable<Long> apply(Long input) {
            if (input == 0) return emptyImmutableIterable();
            else return halves(input);
        }

        private ImmutableFiniteIterable<Long> halves(Long x) {
            long q = x / 2;
            if (q == 0) {
                return ImmutableIterable.of(0L);
            } else {
                return lazyCons(q, () -> lazyCons(-q, () -> halves(q)));
            }
        }
    };

    public static Shrink<Integer> shrinkInt() {
        return INT;
    }

    public static Shrink<Integer> shrinkIntBetween(int min, int max) {
        return Shrink.none();
    }

    public static Shrink<Integer> shrinkIntBounded(int bound) {
        return Shrink.none();
    }

    public static Shrink<Integer> shrinkIntExclusive(int origin, int bound) {
        return Shrink.none();
    }

    public static Shrink<Long> shrinkLong() {
        return LONG;
    }

    public static Shrink<Long> shrinkLongBetween(long min, long max) {
        return Shrink.none();
    }

    public static Shrink<Long> shrinkLongBounded(long bound) {
        return Shrink.none();
    }

    public static Shrink<Long> shrinkLongExclusive(long origin, long bound) {
        return Shrink.none();
    }

}
