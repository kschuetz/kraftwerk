package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomImpl<A> implements CompiledGenerator<A> {
    private final Fn1<? super RandomState, Result<RandomState, A>> fn;

    @Override
    public Result<? extends RandomState, A> run(RandomState input) {
        return fn.apply(input);
    }

    public static <A> CustomImpl<A> customImpl(Fn1<? super RandomState, Result<RandomState, A>> fn) {
        return new CustomImpl<>(fn);
    }
}
