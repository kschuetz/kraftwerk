package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

class Composition {

    static <A, B> Generator<B> flatMapped(Fn1<? super A, ? extends Generator<B>> fn, Generator<A> operand) {
        return new FlatMapped<>(operand, fn::apply);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class FlatMapped<In, A> implements Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("flatMap");

        private final Generator<In> operand;
        private final Fn1<? super In, ? extends Generator<A>> fn;

        @Override
        public Generate<A> prepare(Parameters parameters) {
            Fn1<Seed, Result<? extends Seed, In>> runner = operand.prepare(parameters);
            return input -> {
                Result<? extends Seed, In> result1 = runner.apply(input);
                Generator<A> g2 = fn.apply(result1.getValue());
                return g2.prepare(parameters).apply(result1.getNextState());
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }
}
