package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.enhancediterables.NonEmptyIterable;

class Sequence {

    static <A> Generator<ImmutableVector<A>> sequence(Iterable<Generator<A>> gs) {
        return Generators.<A, VectorBuilder<A>, ImmutableVector<A>>aggregate(Vector::builder,
                VectorBuilder::add, VectorBuilder::build, gs);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> sequenceNonEmpty(NonEmptyIterable<Generator<A>> gs) {
        return Generators.<A, VectorBuilder<A>, ImmutableVector<A>>aggregate(Vector::builder,
                VectorBuilder::add, VectorBuilder::build, gs)
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }
    
}
