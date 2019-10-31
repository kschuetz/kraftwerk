package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn8;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product8Impl<A, B, C, D, E, F, G, H, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<A> a;
    private final GeneratorImpl<B> b;
    private final GeneratorImpl<C> c;
    private final GeneratorImpl<D> d;
    private final GeneratorImpl<E> e;
    private final GeneratorImpl<F> f;
    private final GeneratorImpl<G> g;
    private final GeneratorImpl<H> h;
    private final Fn8<A, B, C, D, E, F, G, H, Out> combine;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        Result<? extends LegacySeed, A> r1 = a.run(input);
        Result<? extends LegacySeed, B> r2 = b.run(r1.getNextState());
        Result<? extends LegacySeed, C> r3 = c.run(r2.getNextState());
        Result<? extends LegacySeed, D> r4 = d.run(r3.getNextState());
        Result<? extends LegacySeed, E> r5 = e.run(r4.getNextState());
        Result<? extends LegacySeed, F> r6 = f.run(r5.getNextState());
        Result<? extends LegacySeed, G> r7 = g.run(r6.getNextState());
        Result<? extends LegacySeed, H> r8 = h.run(r7.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue(), r8.getValue());
        return result(r8.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G, H, Out> Product8Impl<A, B, C, D, E, F, G, H, Out> product8Impl(GeneratorImpl<A> a,
                                                                                                       GeneratorImpl<B> b,
                                                                                                       GeneratorImpl<C> c,
                                                                                                       GeneratorImpl<D> d,
                                                                                                       GeneratorImpl<E> e,
                                                                                                       GeneratorImpl<F> f,
                                                                                                       GeneratorImpl<G> g,
                                                                                                       GeneratorImpl<H> h,
                                                                                                       Fn8<A, B, C, D, E, F, G, H, Out> fn) {
        return new Product8Impl<>(a, b, c, d, e, f, g, h, fn);
    }

    public static <A, B, C, D, E, F, G, H, Out> GeneratorImpl<Trace<Out>> tracedProduct8Impl(Generator<Out> source,
                                                                                             GeneratorImpl<Trace<A>> a,
                                                                                             GeneratorImpl<Trace<B>> b,
                                                                                             GeneratorImpl<Trace<C>> c,
                                                                                             GeneratorImpl<Trace<D>> d,
                                                                                             GeneratorImpl<Trace<E>> e,
                                                                                             GeneratorImpl<Trace<F>> f,
                                                                                             GeneratorImpl<Trace<G>> g,
                                                                                             GeneratorImpl<Trace<H>> h,
                                                                                             Fn8<A, B, C, D, E, F, G, H, Out> combine) {
        return product8Impl(a, b, c, d, e, f, g, h,
                (ta, tb, tc, td, te, tf, tg, th) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult(), tf.getResult(), tg.getResult(), th.getResult()),
                        source, asList(ta, tb, tc, td, te, tf, tg, th)));
    }

}
