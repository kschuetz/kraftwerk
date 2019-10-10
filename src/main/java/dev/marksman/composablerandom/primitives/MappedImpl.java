package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import dev.marksman.composablerandom.spike.ShrinkTree;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MappedImpl<In, Out> implements GeneratorImpl<Out> {
    private final Fn1<In, Out> fn;
    private final GeneratorImpl<In> operand;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        return operand.run(input).fmap(fn);
    }

    @Override
    public Result<? extends Seed, ShrinkTree<Out>> runShrinking3(Seed input) {
        return operand.runShrinking3(input).fmap(st -> st.fmap(fn));
    }

    public static <In, Out> MappedImpl<In, Out> mappedImpl(Fn1<In, Out> fn,
                                                           GeneratorImpl<In> operand) {
        return new MappedImpl<>(fn, operand);
    }

}
