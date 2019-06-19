package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn7;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product7Impl<A, B, C, D, E, F, G, Out> implements Generator<Out> {
    private final Generator<A> a;
    private final Generator<B> b;
    private final Generator<C> c;
    private final Generator<D> d;
    private final Generator<E> e;
    private final Generator<F> f;
    private final Generator<G> g;
    private final Fn7<A, B, C, D, E, F, G, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Result<? extends RandomState, E> r5 = e.run(r4.getNextState());
        Result<? extends RandomState, F> r6 = f.run(r5.getNextState());
        Result<? extends RandomState, G> r7 = g.run(r6.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue());
        return result(r7.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G, Out> Product7Impl<A, B, C, D, E, F, G, Out> product7Impl(Generator<A> a,
                                                                                                 Generator<B> b,
                                                                                                 Generator<C> c,
                                                                                                 Generator<D> d,
                                                                                                 Generator<E> e,
                                                                                                 Generator<F> f,
                                                                                                 Generator<G> g,
                                                                                                 Fn7<A, B, C, D, E, F, G, Out> combine) {
        return new Product7Impl<>(a, b, c, d, e, f, g, combine);
    }

    public static <A, B, C, D, E, F, G, Out> Generator<Trace<Out>> tracedProduct7Impl(Generate<Out> source,
                                                                                      Generator<Trace<A>> a,
                                                                                      Generator<Trace<B>> b,
                                                                                      Generator<Trace<C>> c,
                                                                                      Generator<Trace<D>> d,
                                                                                      Generator<Trace<E>> e,
                                                                                      Generator<Trace<F>> f,
                                                                                      Generator<Trace<G>> g,
                                                                                      Fn7<A, B, C, D, E, F, G, Out> combine) {
        return product7Impl(a, b, c, d, e, f, g,
                (ta, tb, tc, td, te, tf, tg) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult(), tf.getResult(), tg.getResult()),
                        source, asList(ta, tb, tc, td, te, tf, tg)));
    }

}
