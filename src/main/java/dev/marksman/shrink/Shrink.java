package dev.marksman.shrink;

import dev.marksman.enhancediterables.ImmutableIterable;

import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;

public interface Shrink<A> {
    ImmutableIterable<A> apply(A input);

    static <A> Shrink<A> none() {
        return input -> emptyImmutableIterable();
    }

}
