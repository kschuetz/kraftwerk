package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantImpl<A> implements CompiledGenerator<A> {
    private final A value;

    @Override
    public Result<RandomState, A> run(RandomState input) {
        return result(input, value);
    }

    public static <A> ConstantImpl<A> constantImpl(A value) {
        return new ConstantImpl<>(value);
    }
}

