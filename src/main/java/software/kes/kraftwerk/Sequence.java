package software.kes.kraftwerk;

import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.ImmutableVector;
import software.kes.enhancediterables.NonEmptyIterable;

import static software.kes.kraftwerk.Generators.aggregate;
import static software.kes.kraftwerk.aggregator.Aggregators.vectorAggregator;

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
