package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import dev.marksman.composablerandom.SizeSelector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SizedImpl<A> implements GeneratorState<A> {
    private final SizeSelector sizeSelector;
    private final Fn1<Integer, GeneratorState<A>> fn;

    @Override
    public Result<? extends Seed, A> run(Seed input) {
        Result<? extends Seed, Integer> sizeResult = sizeSelector.selectSize(input);
        return fn.apply(sizeResult.getValue())
                .run(sizeResult.getNextState());
    }

    public static <A> SizedImpl<A> sizedImpl(SizeSelector sizeSelector, Fn1<Integer, GeneratorState<A>> fn) {
        return new SizedImpl<>(sizeSelector, fn);
    }
}
