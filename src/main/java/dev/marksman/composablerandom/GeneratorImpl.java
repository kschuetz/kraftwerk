package dev.marksman.composablerandom;

import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

import static dev.marksman.shrink.util.SingletonIterable.singletonIterable;

public interface GeneratorImpl<A> {
    Result<? extends Seed, A> run(Seed input);

    default Result<? extends Seed, ImmutableNonEmptyIterable<A>> runShrinking(Seed input) {
        return run(input).fmap(r -> singletonIterable(r));
    }
}
