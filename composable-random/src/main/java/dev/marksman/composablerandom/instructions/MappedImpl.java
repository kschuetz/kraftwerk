package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MappedImpl<In, Out> implements Generate<Out> {
    private final Fn1<In, Out> fn;
    private final Generate<In> operand;

    @Override
    public Result<? extends RandomState, Out> generate(RandomState input) {
        return operand.generate(input).fmap(fn);
    }

    public static <In, Out> MappedImpl<In, Out> mappedImpl(Fn1<In, Out> fn,
                                                           Generate<In> operand) {
        return new MappedImpl<>(fn, operand);
    }

}
