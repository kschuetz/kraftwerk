package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn8;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product8Impl<A, B, C, D, E, F, G, H, Out> implements GeneratorState<Out> {
    private final GeneratorState<A> a;
    private final GeneratorState<B> b;
    private final GeneratorState<C> c;
    private final GeneratorState<D> d;
    private final GeneratorState<E> e;
    private final GeneratorState<F> f;
    private final GeneratorState<G> g;
    private final GeneratorState<H> h;
    private final Fn8<A, B, C, D, E, F, G, H, Out> combine;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        Result<? extends Seed, A> r1 = a.run(input);
        Result<? extends Seed, B> r2 = b.run(r1.getNextState());
        Result<? extends Seed, C> r3 = c.run(r2.getNextState());
        Result<? extends Seed, D> r4 = d.run(r3.getNextState());
        Result<? extends Seed, E> r5 = e.run(r4.getNextState());
        Result<? extends Seed, F> r6 = f.run(r5.getNextState());
        Result<? extends Seed, G> r7 = g.run(r6.getNextState());
        Result<? extends Seed, H> r8 = h.run(r7.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue(), r8.getValue());
        return result(r8.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G, H, Out> Product8Impl<A, B, C, D, E, F, G, H, Out> product8Impl(GeneratorState<A> a,
                                                                                                       GeneratorState<B> b,
                                                                                                       GeneratorState<C> c,
                                                                                                       GeneratorState<D> d,
                                                                                                       GeneratorState<E> e,
                                                                                                       GeneratorState<F> f,
                                                                                                       GeneratorState<G> g,
                                                                                                       GeneratorState<H> h,
                                                                                                       Fn8<A, B, C, D, E, F, G, H, Out> fn) {
        return new Product8Impl<>(a, b, c, d, e, f, g, h, fn);
    }

    public static <A, B, C, D, E, F, G, H, Out> GeneratorState<Trace<Out>> tracedProduct8Impl(Generator<Out> source,
                                                                                              GeneratorState<Trace<A>> a,
                                                                                              GeneratorState<Trace<B>> b,
                                                                                              GeneratorState<Trace<C>> c,
                                                                                              GeneratorState<Trace<D>> d,
                                                                                              GeneratorState<Trace<E>> e,
                                                                                              GeneratorState<Trace<F>> f,
                                                                                              GeneratorState<Trace<G>> g,
                                                                                              GeneratorState<Trace<H>> h,
                                                                                              Fn8<A, B, C, D, E, F, G, H, Out> combine) {
        return product8Impl(a, b, c, d, e, f, g, h,
                (ta, tb, tc, td, te, tf, tg, th) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult(), tf.getResult(), tg.getResult(), th.getResult()),
                        source, asList(ta, tb, tc, td, te, tf, tg, th)));
    }

}
