package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlatMappedImpl<In, Out> implements CompiledGenerator<Out> {
    private final Fn1<? super In, ? extends CompiledGenerator<Out>> fn;
    private final CompiledGenerator<In> operand;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, In> result1 = operand.run(input);
        return fn.apply(result1.getValue())
                .run(result1.getNextState());
    }

    public static <In, Out> FlatMappedImpl<In, Out> flatMappedImpl(Fn1<? super In, ? extends CompiledGenerator<Out>> fn,
                                                                   CompiledGenerator<In> operand) {
        return new FlatMappedImpl<>(fn, operand);
    }

}
