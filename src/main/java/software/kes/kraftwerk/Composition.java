package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

final class Composition {
    private Composition() {
    }

    static <A, B> Generator<B> flatMapped(Fn1<? super A, ? extends Generator<B>> fn, Generator<A> operand) {
        return new FlatMapped<>(operand, fn::apply);
    }

    private static class FlatMapped<In, A> implements Generator<A> {
        private static final Maybe<String> LABEL = Maybe.just("flatMap");

        private final Generator<In> operand;
        private final Fn1<? super In, ? extends Generator<A>> fn;

        private FlatMapped(Generator<In> operand, Fn1<? super In, ? extends Generator<A>> fn) {
            this.operand = operand;
            this.fn = fn;
        }

        @Override
        public GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters) {
            Fn1<Seed, Result<? extends Seed, In>> runner = operand.createGenerateFn(generatorParameters);
            return input -> {
                Result<? extends Seed, In> result1 = runner.apply(input);
                Generator<A> g2 = fn.apply(result1.getValue());
                return g2.createGenerateFn(generatorParameters).apply(result1.getNextState());
            };
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }
}
