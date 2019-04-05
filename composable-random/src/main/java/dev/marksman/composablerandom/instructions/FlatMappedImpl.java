package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FlatMappedImpl<In, Out> implements Generate<Out> {
    private final Fn1<? super In, ? extends Generate<Out>> fn;
    private final Generate<In> operand;

    @Override
    public Result<? extends RandomState, Out> generate(RandomState input) {
        Result<? extends RandomState, In> result1 = operand.generate(input);
        return fn.apply(result1.getValue())
                .generate(result1.getNextState());
    }

    public static <In, Out> FlatMappedImpl<In, Out> flatMappedImpl(Fn1<? super In, ? extends Generate<Out>> fn,
                                                                   Generate<In> operand) {
        return new FlatMappedImpl<>(fn, operand);
    }

}
