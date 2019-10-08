package dev.marksman.composablerandom;

import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

public interface GeneratorImpl<A> {
    Result<? extends Seed, A> run(Seed input);

    default Result<? extends Seed, ImmutableNonEmptyIterable<A>> runShrinking(Seed input) {
        return run(input).fmap(r -> Vector.of(r));
    }
}
