package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static dev.marksman.kraftwerk.Result.result;

class Aggregation {

    static <A, Builder, Out> Generator<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                      Fn2<Builder, A, Builder> addFn,
                                                      Fn1<Builder, Out> buildFn,
                                                      Iterable<Generator<A>> elements) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, elements);
    }

    static <Elem, Builder, Out> Generator<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                         Fn2<Builder, Elem, Builder> addFn,
                                                         Fn1<Builder, Out> buildFn,
                                                         int size,
                                                         Generator<Elem> gen) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, gen));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class Aggregate<Elem, Builder, Out> implements Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("aggregate");

        private final Fn0<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generator<Elem>> elements;

        @Override
        public Generate<Out> prepare(Parameters parameters) {
            Iterable<Generate<Elem>> runners = Map.map(g -> g.prepare(parameters), elements);
            return input -> {
                Seed current = input;
                Builder builder = initialBuilderSupplier.apply();

                for (Generate<Elem> element : runners) {
                    Result<? extends Seed, Elem> next = element.apply(current);
                    builder = addFn.apply(builder, next.getValue());
                    current = next.getNextState();
                }
                return result(current, buildFn.apply(builder));
            };

        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }
}
