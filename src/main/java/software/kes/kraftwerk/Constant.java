package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;

import static software.kes.kraftwerk.Result.result;

final class Constant<A> implements Generator<A> {
    private static final Maybe<String> LABEL = Maybe.just("constant");

    private final A value;

    private Constant(A value) {
        this.value = value;
    }

    static <A> Constant<A> constant(A value) {
        return new Constant<>(value);
    }

    @Override
    public GenerateFn<A> createGenerateFn(GeneratorParameters generatorParameters) {
        return input -> result(input, value);
    }

    @Override
    public Maybe<String> getLabel() {
        return LABEL;
    }
}
