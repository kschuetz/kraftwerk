package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomImpl<A> implements GeneratorImpl<A> {
    private final Fn1<? super LegacySeed, Result<LegacySeed, A>> fn;

    @Override
    public Result<? extends LegacySeed, A> run(LegacySeed input) {
        return fn.apply(input);
    }

    public static <A> CustomImpl<A> customImpl(Fn1<? super LegacySeed, Result<LegacySeed, A>> fn) {
        return new CustomImpl<>(fn);
    }
}
