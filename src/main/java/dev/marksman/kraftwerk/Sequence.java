package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.enhancediterables.ImmutableIterable;

class Sequence {

    static <A> Generator<ImmutableIterable<A>> sequence(Iterable<Generator<A>> gs) {
        return Generators.<A, VectorBuilder<A>, ImmutableIterable<A>>aggregate(Vector::builder,
                VectorBuilder::add, VectorBuilder::build, gs);
    }

}
