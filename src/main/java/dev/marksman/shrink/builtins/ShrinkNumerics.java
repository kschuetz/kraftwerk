package dev.marksman.shrink.builtins;

import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.shrink.Shrink;

import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;
import static dev.marksman.shrink.util.LazyCons.lazyCons;

public class ShrinkNumerics {

    private static Shrink<Integer> INT = new Shrink<Integer>() {
        @Override
        public ImmutableIterable<Integer> apply(Integer input) {
            if (input == 0) {
                return emptyImmutableIterable();
            } else {
                return halves(input);
            }
        }

        private ImmutableIterable<Integer> halves(Integer x) {
            int q = x / 2;
            if (q == 0) {
                return ImmutableIterable.of(0);
            } else {
                return lazyCons(q, () -> lazyCons(-q, () -> halves(q)));
            }
        }

    };
}
