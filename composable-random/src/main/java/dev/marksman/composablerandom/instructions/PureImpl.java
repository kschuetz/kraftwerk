package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PureImpl<A> implements Generate<A> {
    private final A value;

    @Override
    public Result<RandomState, A> generate(RandomState input) {
        return result(input, value);
    }

    public static <A> PureImpl<A> pureImpl(A value) {
        return new PureImpl<>(value);
    }
}

