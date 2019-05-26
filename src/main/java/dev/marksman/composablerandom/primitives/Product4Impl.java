package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product4Impl<A, B, C, D, Out> implements CompiledGenerator<Out> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;
    private final Fn4<A, B, C, D, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue());
        return result(r4.getNextState(), result);
    }

    public static <A, B, C, D, Out> Product4Impl<A, B, C, D, Out> product4Impl(CompiledGenerator<A> a,
                                                                               CompiledGenerator<B> b,
                                                                               CompiledGenerator<C> c,
                                                                               CompiledGenerator<D> d,
                                                                               Fn4<A, B, C, D, Out> combine) {
        return new Product4Impl<>(a, b, c, d, combine);
    }

}
