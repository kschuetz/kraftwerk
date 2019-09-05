package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlatMappedImpl<In, Out> implements GeneratorState<Out> {
    private final Fn1<? super In, ? extends GeneratorState<Out>> fn;
    private final GeneratorState<In> operand;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        Result<? extends Seed, In> result1 = operand.run(input);
        return fn.apply(result1.getValue())
                .run(result1.getNextState());
    }

    public static <In, Out> FlatMappedImpl<In, Out> flatMappedImpl(Fn1<? super In, ? extends GeneratorState<Out>> fn,
                                                                   GeneratorState<In> operand) {
        return new FlatMappedImpl<>(fn, operand);
    }

}
