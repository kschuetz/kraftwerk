package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MappedImpl<In, Out> implements Generator<Out> {
    private final Fn1<In, Out> fn;
    private final Generator<In> operand;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        return operand.run(input).fmap(fn);
    }

    public static <In, Out> MappedImpl<In, Out> mappedImpl(Fn1<In, Out> fn,
                                                           Generator<In> operand) {
        return new MappedImpl<>(fn, operand);
    }

}
