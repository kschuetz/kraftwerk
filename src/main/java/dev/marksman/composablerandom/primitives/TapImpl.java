package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TapImpl<A, B> implements CompiledGenerator<B> {
    private final CompiledGenerator<A> inner;
    private final Fn2<CompiledGenerator<A>, RandomState, B> fn;

    @Override
    public Result<? extends RandomState, B> run(RandomState input) {
        return result(input.nextInt().getNextState(),
                fn.apply(inner, input));
    }

    public static <A, B> TapImpl<A, B> tapImpl(CompiledGenerator<A> inner,
                                               Fn2<CompiledGenerator<A>, RandomState, B> f) {
        return new TapImpl<>(inner, f);
    }
}
