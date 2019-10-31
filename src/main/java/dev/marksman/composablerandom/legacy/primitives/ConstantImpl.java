package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantImpl<A> implements GeneratorImpl<A> {
    private final A value;

    @Override
    public Result<LegacySeed, A> run(LegacySeed input) {
        return result(input, value);
    }

    public static <A> ConstantImpl<A> constantImpl(A value) {
        return new ConstantImpl<>(value);
    }
}

