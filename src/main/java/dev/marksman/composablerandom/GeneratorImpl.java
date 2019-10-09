package dev.marksman.composablerandom;

import dev.marksman.composablerandom.spike.ShrinkNode;
import dev.marksman.composablerandom.spike.ShrinkTree;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.shrink.util.SingletonIterable;

import static dev.marksman.composablerandom.spike.ShrinkNode.shrinkNode;
import static dev.marksman.shrink.util.SingletonIterable.singletonIterable;

public interface GeneratorImpl<A> {
    Result<? extends Seed, A> run(Seed input);

    default Result<? extends Seed, ImmutableNonEmptyFiniteIterable<A>> runShrinking(Seed input) {
        return run(input).fmap(SingletonIterable::singletonIterable);
    }

    default Result<? extends Seed, ImmutableNonEmptyFiniteIterable<ShrinkNode<A>>> runShrinking2(Seed input) {
        return run(input).fmap(r -> singletonIterable(shrinkNode(r)));
    }

    default Result<? extends Seed, ShrinkTree<A>> runShrinking3(Seed input) {
        return run(input).fmap(r -> () -> r);
    }

}
