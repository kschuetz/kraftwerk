package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn6;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product6Impl<A, B, C, D, E, F, Out> implements CompiledGenerator<Out> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;
    private final CompiledGenerator<E> e;
    private final CompiledGenerator<F> f;
    private final Fn6<A, B, C, D, E, F, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.run(r4.getNextState());
        Result<? extends RandomState, F> r6 = f.run(r5.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue());
        return result(r6.getNextState(), result);
    }

    public static <A, B, C, D, E, F, Out> Product6Impl<A, B, C, D, E, F, Out> product6Impl(CompiledGenerator<A> a,
                                                                                           CompiledGenerator<B> b,
                                                                                           CompiledGenerator<C> c,
                                                                                           CompiledGenerator<D> d,
                                                                                           CompiledGenerator<E> e,
                                                                                           CompiledGenerator<F> f,
                                                                                           Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6Impl<>(a, b, c, d, e, f, combine);
    }

}
