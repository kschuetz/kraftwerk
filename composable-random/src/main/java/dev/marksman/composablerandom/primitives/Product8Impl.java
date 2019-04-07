package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product8Impl<A, B, C, D, E, F, G, H> implements CompiledGenerator<Tuple8<A, B, C, D, E, F, G, H>> {
    private final CompiledGenerator<A> a;
    private final CompiledGenerator<B> b;
    private final CompiledGenerator<C> c;
    private final CompiledGenerator<D> d;
    private final CompiledGenerator<E> e;
    private final CompiledGenerator<F> f;
    private final CompiledGenerator<G> g;
    private final CompiledGenerator<H> h;

    @Override
    public Result<? extends RandomState, Tuple8<A, B, C, D, E, F, G, H>> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.run(r4.getNextState());
        Result<? extends RandomState, F> r6 = f.run(r5.getNextState());
        Result<? extends RandomState, G> r7 = g.run(r6.getNextState());
        Result<? extends RandomState, H> r8 = h.run(r7.getNextState());
        Tuple8<A, B, C, D, E, F, G, H> result = tuple(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue(), r8.getValue());
        return result(r8.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G, H> Product8Impl<A, B, C, D, E, F, G, H> product8Impl(CompiledGenerator<A> a,
                                                                                             CompiledGenerator<B> b,
                                                                                             CompiledGenerator<C> c,
                                                                                             CompiledGenerator<D> d,
                                                                                             CompiledGenerator<E> e,
                                                                                             CompiledGenerator<F> f,
                                                                                             CompiledGenerator<G> g,
                                                                                             CompiledGenerator<H> h) {
        return new Product8Impl<>(a, b, c, d, e, f, g, h);
    }


}
