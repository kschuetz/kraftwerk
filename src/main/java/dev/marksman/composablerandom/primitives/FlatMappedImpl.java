package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlatMappedImpl<In, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<In> operand;
    private final Fn1<? super In, ? extends GeneratorImpl<Out>> fn;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        Result<? extends LegacySeed, In> result1 = operand.run(input);
        return fn.apply(result1.getValue())
                .run(result1.getNextState());
    }

    public static <In, Out> FlatMappedImpl<In, Out> flatMappedImpl(GeneratorImpl<In> operand,
                                                                   Fn1<? super In, ? extends GeneratorImpl<Out>> fn) {
        return new FlatMappedImpl<>(operand, fn);
    }

}
