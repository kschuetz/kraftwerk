package dev.marksman.shrink.util;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;

import static dev.marksman.enhancediterables.FiniteIterable.emptyFiniteIterable;

public class SingletonIterable {

    public static <A> ImmutableNonEmptyFiniteIterable<A> singletonIterable(A value) {
        return new ImmutableNonEmptyFiniteIterable<A>() {
            @Override
            public ImmutableFiniteIterable<A> tail() {
                return emptyFiniteIterable();
            }

            @Override
            public A head() {
                return value;
            }
        };
    }
}
