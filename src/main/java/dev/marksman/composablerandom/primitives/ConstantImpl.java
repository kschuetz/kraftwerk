package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantImpl<A> implements GeneratorState<A> {
    private final A value;

    @Override
    public Result<RandomState, A> run(RandomState input) {
        return result(input, value);
    }

    public static <A> ConstantImpl<A> constantImpl(A value) {
        return new ConstantImpl<>(value);
    }
}

