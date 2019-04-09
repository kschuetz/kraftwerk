package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn5;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product5Impl<A, B, C, D, E, Out> implements CompiledGenerator<Out> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;
    private final CompiledGenerator<E> e;
    private final Fn5<A, B, C, D, E, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.run(r4.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue());
        return result(r5.getNextState(), result);
    }

    public static <A, B, C, D, E, Out> Product5Impl<A, B, C, D, E, Out> product5Impl(CompiledGenerator<A> a,
                                                                                     CompiledGenerator<B> b,
                                                                                     CompiledGenerator<C> c,
                                                                                     CompiledGenerator<D> d,
                                                                                     CompiledGenerator<E> e,
                                                                                     Fn5<A, B, C, D, E, Out> combine) {
        return new Product5Impl<>(a, b, c, d, e, combine);
    }

}
