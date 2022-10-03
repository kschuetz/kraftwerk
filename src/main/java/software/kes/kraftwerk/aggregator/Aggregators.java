package software.kes.kraftwerk.aggregator;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.monoid.Monoid;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.VectorBuilder;

import java.util.Collection;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A collection of built-in {@link Aggregator}s.
 */
public final class Aggregators {

    private Aggregators() {

    }

    /**
     * Creates an {@link Aggregator} for {@link ImmutableVector}s.
     */
    public static <A> Aggregator<A, VectorBuilder<A>, ImmutableVector<A>> vectorAggregator() {
        return Aggregator.aggregator(VectorBuilder::builder, VectorBuilder::add, VectorBuilder::build);
    }

    /**
     * Creates an {@link Aggregator} for {@link ImmutableVector}s.
     *
     * @param initialCapacity a size hint for the final result.
     *                        The size of the final result may end up being larger or smaller than this.
     */
    public static <A> Aggregator<A, VectorBuilder<A>, ImmutableVector<A>> vectorAggregator(int initialCapacity) {
        return Aggregator.aggregator(() -> VectorBuilder.builder(initialCapacity), VectorBuilder::add, VectorBuilder::build);
    }

    /**
     * Creates an {@link Aggregator} for {@link String}s by concatenating other {@code String}s.
     */
    public static Aggregator<String, StringBuilder, String> stringAggregator() {
        return Aggregator.aggregator(StringBuilder::new, StringBuilder::append, StringBuilder::toString);
    }

    /**
     * Creates an {@link Aggregator} for {@link String}s by concatenating other {@code String}s.
     *
     * @param initialCapacity a length hint for the final result.
     *                        The size of the final result may end up being larger or smaller than this.
     */
    public static Aggregator<String, StringBuilder, String> stringAggregator(int initialCapacity) {
        return Aggregator.aggregator(() -> new StringBuilder(initialCapacity), StringBuilder::append, StringBuilder::toString);
    }

    /**
     * Creates an {@link Aggregator} for {@link String}s by concatenating {@link Character}s.
     */
    public static Aggregator<Character, StringBuilder, String> charAggregator() {
        return Aggregator.aggregator(StringBuilder::new, StringBuilder::append, StringBuilder::toString);
    }

    /**
     * Creates an {@link Aggregator} for {@link String}s by concatenating {@link Character}s.
     *
     * @param initialCapacity a length hint for the final result.
     *                        The size of the final result may end up being larger or smaller than this.
     */
    public static Aggregator<Character, StringBuilder, String> charAggregator(int initialCapacity) {
        return Aggregator.aggregator(() -> new StringBuilder(initialCapacity), StringBuilder::append, StringBuilder::toString);
    }

    /**
     * Creates an {@link Aggregator} for {@link String}s by concatenating {@link Maybe} values of {@code String}s.
     */
    public static Aggregator<Maybe<String>, StringBuilder, String> maybeStringAggregator() {
        return Aggregator.aggregator(StringBuilder::new,
                (builder, maybeString) -> builder.append(maybeString.orElse("")),
                StringBuilder::toString);
    }

    /**
     * Creates an {@link Aggregator} for {@link String}s by concatenating {@link Maybe} values of {@code String}s.
     *
     * @param initialCapacity a length hint for the final result.
     *                        The size of the final result may end up being larger or smaller than this.
     */
    public static Aggregator<Maybe<String>, StringBuilder, String> maybeStringAggregator(int initialCapacity) {
        return Aggregator.aggregator(() -> new StringBuilder(initialCapacity),
                (builder, maybeString) -> builder.append(maybeString.orElse("")),
                StringBuilder::toString);
    }

    /**
     * Creates an {@link Aggregator} for any {@link Collection} type.
     *
     * @param constructCollection a function that creates an empty collection of type {@code C}
     * @param <A>                 the element type
     * @param <C>                 the collection type
     * @return a {@code Collection} of type {@code C} with elements of type {@code A}
     */
    public static <A, C extends Collection<A>> Aggregator<A, C, C> collectionAggregator(Fn0<C> constructCollection) {
        return Aggregator.aggregator(constructCollection, (collection, item) -> {
            collection.add(item);
            return collection;
        }, id());
    }

    /**
     * Creates an {@link Aggregator} out of a {@link Monoid}.
     *
     * @param monoid a {@code Monoid} for type {@code A}
     * @param <A>    the element type and the type for the final result
     * @return an {@code A}
     */
    public static <A> Aggregator<A, A, A> monoidAggregator(Monoid<A> monoid) {
        return Aggregator.aggregator(monoid::identity, monoid::apply, id());
    }

}
