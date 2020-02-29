package dev.marksman.kraftwerk.aggregator;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

public interface Aggregator<A, Builder, Out> {
    Builder builder();

    Builder add(Builder builder, A element);

    Out build(Builder builder);

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
