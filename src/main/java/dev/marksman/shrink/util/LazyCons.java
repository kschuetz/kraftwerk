package dev.marksman.shrink.util;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;

public class LazyCons {
    public static <A> ImmutableNonEmptyFiniteIterable<A> lazyCons(A head, Fn0<ImmutableFiniteIterable<A>> tailSupplier) {
        return new ImmutableNonEmptyFiniteIterable<A>() {
            @Override
            public ImmutableFiniteIterable<A> tail() {
                return tailSupplier.apply();
            }

            @Override
            public A head() {
                return head;
            }
        };
    }
}
