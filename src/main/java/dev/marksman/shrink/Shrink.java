package dev.marksman.shrink;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;

public interface Shrink<A> {
    ImmutableFiniteIterable<A> apply(A input);

    static <A> Shrink<A> none() {
        return input -> emptyImmutableIterable();
    }

}
