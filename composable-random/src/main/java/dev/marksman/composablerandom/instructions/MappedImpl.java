package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MappedImpl<In, A> implements Generate<A> {
    private final Fn1<In, A> fn;
    private final Generate<In> operand;

    @Override
    public Result<? extends RandomState, A> generate(RandomState input) {
        return operand.generate(input).fmap(fn);
    }

    public static <In, A> MappedImpl<In, A> mappedImpl(Fn1<In, A> fn,
                                                       Generate<In> operand) {
        return new MappedImpl<>(fn, operand);
    }

}
