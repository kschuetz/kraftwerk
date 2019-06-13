package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HigherOrderImpl<A, B> implements CompiledGenerator<B> {
    private final CompiledGenerator<A> inner;
    private final Fn2<CompiledGenerator<A>, RandomState, Result<? extends RandomState, B>> f;

    @Override
    public Result<? extends RandomState, B> run(RandomState input) {
        return f.apply(inner, input);
    }

    public static <A, B> HigherOrderImpl<A, B> higherOrderImpl(CompiledGenerator<A> inner,
                                                               Fn2<CompiledGenerator<A>, RandomState, Result<? extends RandomState, B>> f) {
        return new HigherOrderImpl<>(inner, f);
    }
}
