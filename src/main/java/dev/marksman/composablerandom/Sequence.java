package dev.marksman.composablerandom;

import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.enhancediterables.ImmutableIterable;

class Sequence {

    static <A> Generate<ImmutableIterable<A>> sequence(Iterable<Generate<A>> gs) {
        return Generate.<A, VectorBuilder<A>, ImmutableIterable<A>>aggregate(Vector::builder,
                VectorBuilder::add, VectorBuilder::build, gs);
    }

}
