package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.kraftwerk.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Constant<A> implements Generator<A> {
    private static Maybe<String> LABEL = Maybe.just("constant");

    private final A value;

    @Override
    public Generate<A> prepare(GeneratorParameters generatorParameters) {
        return input -> result(input, value);
    }

    @Override
    public Maybe<String> getLabel() {
        return LABEL;
    }

    static <A> Constant<A> constant(A value) {
        return new Constant<>(value);
    }
}
