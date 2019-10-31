package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.SizeSelector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SizedImpl<A> implements GeneratorImpl<A> {
    private final SizeSelector sizeSelector;
    private final Fn1<Integer, GeneratorImpl<A>> fn;

    @Override
    public Result<? extends LegacySeed, A> run(LegacySeed input) {
        Result<? extends LegacySeed, Integer> sizeResult = sizeSelector.selectSize(input);
        return fn.apply(sizeResult.getValue())
                .run(sizeResult.getNextState());
    }

    public static <A> SizedImpl<A> sizedImpl(SizeSelector sizeSelector, Fn1<Integer, GeneratorImpl<A>> fn) {
        return new SizedImpl<>(sizeSelector, fn);
    }
}
