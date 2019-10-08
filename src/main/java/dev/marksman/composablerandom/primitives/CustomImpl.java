package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomImpl<A> implements GeneratorImpl<A> {
    private final Fn1<? super Seed, Result<Seed, A>> fn;

    @Override
    public Result<? extends Seed, A> run(Seed input) {
        return fn.apply(input);
    }

    public static <A> CustomImpl<A> customImpl(Fn1<? super Seed, Result<Seed, A>> fn) {
        return new CustomImpl<>(fn);
    }
}
