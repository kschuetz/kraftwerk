package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import software.kes.kraftwerk.aggregator.Aggregator;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

final class Aggregation {
    static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                      Iterable<Generator<A>> elements) {
        return new Aggregate<>(aggregator, elements);
    }

    static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                      int size,
                                                      Generator<A> gen) {
        return new Aggregate<>(aggregator, replicate(size, gen));
    }

    static class Aggregate<Elem, Builder, Out> implements Generator<Out> {
        private static final Maybe<String> LABEL = Maybe.just("aggregate");

        private final Aggregator<Elem, Builder, Out> aggregator;
        private final Iterable<Generator<Elem>> elements;

        private Aggregate(Aggregator<Elem, Builder, Out> aggregator, Iterable<Generator<Elem>> elements) {
            this.aggregator = aggregator;
            this.elements = elements;
        }

        @Override
        public GenerateFn<Out> createGenerateFn(GeneratorParameters generatorParameters) {
            Iterable<GenerateFn<Elem>> runners = Map.map(g -> g.createGenerateFn(generatorParameters), elements);
            return input -> {
                Seed current = input;
                Builder builder = aggregator.builder();

                for (GenerateFn<Elem> element : runners) {
                    Result<? extends Seed, Elem> next = element.apply(current);
                    builder = aggregator.add(builder, next.getValue());
                    current = next.getNextState();
                }
                return Result.result(current, aggregator.build(builder));
            };

        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }
}
