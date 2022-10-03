package software.kes.kraftwerk.aggregator;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

/**
 * Defines a strategy for constructing values that are an aggregate of an element type (e.g. collections).
 *
 * @param <A>       the element type
 * @param <Builder> the type of a temporary "builder" object.
 *                  A builder may contain its own state.
 * @param <Out>     the type of the final result
 */
public interface Aggregator<A, Builder, Out> {
    /**
     * Creates a new builder.
     */
    Builder builder();

    /**
     * Adds an element to a builder.
     */
    Builder add(Builder builder, A element);

    /**
     * Creates the final result from the builder.
     * <p>
     * This method should be called at most once for a given builder, and once this method has been called,
     * {@code add} should no longer be called on it.
     */
    Out build(Builder builder);

    /**
     * Creates an {@code Aggregator}.
     *
     * @param createBuilderFn a function that instantiates a builder
     * @param addFn           a function that adds an element to a builder
     * @param buildFn         a function that takes a builder and returns the final resul
     * @param <A>             the element type
     * @param <Builder>       the type of a temporary "builder" object
     * @param <Out>           the type of the final result
     * @return {@code an Aggregator<A, Builder, Out>}
     */
    static <A, Builder, Out> Aggregator<A, Builder, Out> aggregator(Fn0<Builder> createBuilderFn,
                                                                    Fn2<Builder, A, Builder> addFn,
                                                                    Fn1<Builder, Out> buildFn) {
        return new Aggregator<A, Builder, Out>() {
            @Override
            public Builder builder() {
                return createBuilderFn.apply();
            }

            @Override
            public Builder add(Builder builder, A element) {
                return addFn.apply(builder, element);
            }

            @Override
            public Out build(Builder builder) {
                return buildFn.apply(builder);
            }
        };
    }
}
