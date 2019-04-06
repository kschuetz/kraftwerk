package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.SizeSelector;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SizedImpl<A> implements CompiledGenerator<A> {
    private final SizeSelector sizeSelector;
    private final Fn1<Integer, CompiledGenerator<A>> fn;

    @Override
    public Result<? extends RandomState, A> run(RandomState input) {
        Result<? extends RandomState, Integer> sizeResult = sizeSelector.selectSize(input);
        return fn.apply(sizeResult.getValue())
                .run(sizeResult.getNextState());
    }

    public static <A> SizedImpl<A> sizedImpl(SizeSelector sizeSelector, Fn1<Integer, CompiledGenerator<A>> fn) {
        return new SizedImpl<>(sizeSelector, fn);
    }
}
