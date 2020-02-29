package dev.marksman.kraftwerk.aggregator;

import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.VectorBuilder;

import static dev.marksman.kraftwerk.aggregator.Aggregator.aggregator;

public final class Aggregators {

    public static <A> Aggregator<A, VectorBuilder<A>, ImmutableVector<A>> vectorAggregator() {
        return aggregator(VectorBuilder::builder, VectorBuilder::add, VectorBuilder::build);
    }

    public static <A> Aggregator<A, VectorBuilder<A>, ImmutableVector<A>> vectorAggregator(int initialCapacity) {
        return aggregator(() -> VectorBuilder.builder(initialCapacity), VectorBuilder::add, VectorBuilder::build);
    }

}
