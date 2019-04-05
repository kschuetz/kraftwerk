package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product8Impl<A, B, C, D, E, F, G, H> implements Generate<Tuple8<A, B, C, D, E, F, G, H>> {
    private final Generate<A> a;
    private final Generate<B> b;
    private final Generate<C> c;
    private final Generate<D> d;
    private final Generate<E> e;
    private final Generate<F> f;
    private final Generate<G> g;
    private final Generate<H> h;

    @Override
    public Result<? extends RandomState, Tuple8<A, B, C, D, E, F, G, H>> generate(RandomState input) {
        Result<? extends RandomState, A> r1 = a.generate(input);
        Result<? extends RandomState, B> r2 = b.generate(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.generate(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.generate(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.generate(r4.getNextState());
        Result<? extends RandomState, F> r6 = f.generate(r5.getNextState());
        Result<? extends RandomState, G> r7 = g.generate(r6.getNextState());
        Result<? extends RandomState, H> r8 = h.generate(r7.getNextState());
        Tuple8<A, B, C, D, E, F, G, H> result = tuple(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue(), r8.getValue());
        return result(r8.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G, H> Product8Impl<A, B, C, D, E, F, G, H> product8Impl(Generate<A> a,
                                                                                             Generate<B> b,
                                                                                             Generate<C> c,
                                                                                             Generate<D> d,
                                                                                             Generate<E> e,
                                                                                             Generate<F> f,
                                                                                             Generate<G> g,
                                                                                             Generate<H> h) {
        return new Product8Impl<>(a, b, c, d, e, f, g, h);
    }


}
