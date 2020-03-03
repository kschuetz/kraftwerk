package dev.marksman.kraftwerk.aggregator;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.monoid.Monoid;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.VectorBuilder;

import java.util.Collection;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.kraftwerk.aggregator.Aggregator.aggregator;

public final class Aggregators {

    private Aggregators() {

    }

    public static <A> Aggregator<A, VectorBuilder<A>, ImmutableVector<A>> vectorAggregator() {
        return aggregator(VectorBuilder::builder, VectorBuilder::add, VectorBuilder::build);
    }

    public static <A> Aggregator<A, VectorBuilder<A>, ImmutableVector<A>> vectorAggregator(int initialCapacity) {
        return aggregator(() -> VectorBuilder.builder(initialCapacity), VectorBuilder::add, VectorBuilder::build);
    }

    public static Aggregator<String, StringBuilder, String> stringAggregator() {
        return aggregator(StringBuilder::new, StringBuilder::append, StringBuilder::toString);
    }

    public static Aggregator<String, StringBuilder, String> stringAggregator(int initialCapacity) {
        return aggregator(() -> new StringBuilder(initialCapacity), StringBuilder::append, StringBuilder::toString);
    }

    public static Aggregator<Character, StringBuilder, String> charAggregator() {
        return aggregator(StringBuilder::new, StringBuilder::append, StringBuilder::toString);
    }

    public static Aggregator<Character, StringBuilder, String> charAggregator(int initialCapacity) {
        return aggregator(() -> new StringBuilder(initialCapacity), StringBuilder::append, StringBuilder::toString);
    }

    public static Aggregator<Maybe<String>, StringBuilder, String> maybeStringAggregator() {
        return aggregator(StringBuilder::new,
                (builder, maybeString) -> builder.append(maybeString.orElse("")),
                StringBuilder::toString);
    }

    public static Aggregator<Maybe<String>, StringBuilder, String> maybeStringAggregator(int initialCapacity) {
        return aggregator(() -> new StringBuilder(initialCapacity),
                (builder, maybeString) -> builder.append(maybeString.orElse("")),
                StringBuilder::toString);
    }

    public static <A, C extends Collection<A>> Aggregator<A, C, C> collectionAggregator(Fn0<C> constructCollection) {
        return aggregator(constructCollection, (collection, item) -> {
            collection.add(item);
            return collection;
        }, id());
    }

    public static <A> Aggregator<A, A, A> monoidAggregator(Monoid<A> monoid) {
        return aggregator(monoid::identity, monoid::apply, id());
    }

}
