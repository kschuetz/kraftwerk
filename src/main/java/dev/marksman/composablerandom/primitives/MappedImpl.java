package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MappedImpl<In, Out> implements GeneratorImpl<Out> {
    private final Fn1<In, Out> fn;
    private final GeneratorImpl<In> operand;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        return operand.run(input).fmap(fn);
    }

    public static <In, Out> MappedImpl<In, Out> mappedImpl(Fn1<In, Out> fn,
                                                           GeneratorImpl<In> operand) {
        return new MappedImpl<>(fn, operand);
    }

}
