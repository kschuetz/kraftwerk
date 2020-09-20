package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.enhancediterables.NonEmptyIterable;

import static dev.marksman.kraftwerk.Generators.aggregate;
import static dev.marksman.kraftwerk.aggregator.Aggregators.vectorAggregator;

final class Sequence {
    private Sequence() {
    }

    static <A> Generator<ImmutableVector<A>> sequence(Iterable<Generator<A>> gs) {
        return aggregate(vectorAggregator(), gs);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> sequenceNonEmpty(NonEmptyIterable<Generator<A>> gs) {
        return sequence(gs).fmap(ImmutableVector::toNonEmptyOrThrow);
    }
}
