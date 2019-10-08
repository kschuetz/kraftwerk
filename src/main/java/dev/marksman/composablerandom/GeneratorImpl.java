package dev.marksman.composablerandom;

import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.shrink.util.SingletonIterable;

public interface GeneratorImpl<A> {
    Result<? extends Seed, A> run(Seed input);

    default Result<? extends Seed, ImmutableNonEmptyFiniteIterable<A>> runShrinking(Seed input) {
        return run(input).fmap(SingletonIterable::singletonIterable);
    }
}
