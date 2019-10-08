package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantImpl<A> implements GeneratorImpl<A> {
    private final A value;

    @Override
    public Result<Seed, A> run(Seed input) {
        return result(input, value);
    }

    public static <A> ConstantImpl<A> constantImpl(A value) {
        return new ConstantImpl<>(value);
    }
}

