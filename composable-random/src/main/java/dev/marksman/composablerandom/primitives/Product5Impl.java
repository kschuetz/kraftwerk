package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product5Impl<A, B, C, D, E> implements CompiledGenerator<Tuple5<A, B, C, D, E>> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;
    private final CompiledGenerator<E> e;

    @Override
    public Result<? extends RandomState, Tuple5<A, B, C, D, E>> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.run(r4.getNextState());
        Tuple5<A, B, C, D, E> result = tuple(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue());
        return result(r5.getNextState(), result);
    }

    public static <A, B, C, D, E> Product5Impl<A, B, C, D, E> product5Impl(CompiledGenerator<A> a,
                                                                           CompiledGenerator<B> b,
                                                                           CompiledGenerator<C> c,
                                                                           CompiledGenerator<D> d,
                                                                           CompiledGenerator<E> e) {
        return new Product5Impl<>(a, b, c, d, e);
    }

}
