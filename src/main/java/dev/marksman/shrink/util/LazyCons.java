package dev.marksman.shrink.util;

import com.jnape.palatable.lambda.functions.Fn0;
import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

public class LazyCons {
    public static <A> ImmutableNonEmptyIterable<A> lazyCons(A head, Fn0<ImmutableIterable<A>> tailSupplier) {
        return new ImmutableNonEmptyIterable<A>() {
            @Override
            public ImmutableIterable<A> tail() {
                return tailSupplier.apply();
            }

            @Override
            public A head() {
                return head;
            }
        };
    }
}
