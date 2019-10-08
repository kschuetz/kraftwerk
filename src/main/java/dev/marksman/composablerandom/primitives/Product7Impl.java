package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn7;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product7Impl<A, B, C, D, E, F, G, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<A> a;
    private final GeneratorImpl<B> b;
    private final GeneratorImpl<C> c;
    private final GeneratorImpl<D> d;
    private final GeneratorImpl<E> e;
    private final GeneratorImpl<F> f;
    private final GeneratorImpl<G> g;
    private final Fn7<A, B, C, D, E, F, G, Out> combine;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        Result<? extends Seed, A> r1 = a.run(input);
        Result<? extends Seed, B> r2 = b.run(r1.getNextState());
        Result<? extends Seed, C> r3 = c.run(r2.getNextState());
        Result<? extends Seed, D> r4 = d.run(r3.getNextState());
        Result<? extends Seed, E> r5 = e.run(r4.getNextState());
        Result<? extends Seed, F> r6 = f.run(r5.getNextState());
        Result<? extends Seed, G> r7 = g.run(r6.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue(), r7.getValue());
        return result(r7.getNextState(), result);
    }

    public static <A, B, C, D, E, F, G, Out> Product7Impl<A, B, C, D, E, F, G, Out> product7Impl(GeneratorImpl<A> a,
                                                                                                 GeneratorImpl<B> b,
                                                                                                 GeneratorImpl<C> c,
                                                                                                 GeneratorImpl<D> d,
                                                                                                 GeneratorImpl<E> e,
                                                                                                 GeneratorImpl<F> f,
                                                                                                 GeneratorImpl<G> g,
                                                                                                 Fn7<A, B, C, D, E, F, G, Out> combine) {
        return new Product7Impl<>(a, b, c, d, e, f, g, combine);
    }

    public static <A, B, C, D, E, F, G, Out> GeneratorImpl<Trace<Out>> tracedProduct7Impl(Generator<Out> source,
                                                                                          GeneratorImpl<Trace<A>> a,
                                                                                          GeneratorImpl<Trace<B>> b,
                                                                                          GeneratorImpl<Trace<C>> c,
                                                                                          GeneratorImpl<Trace<D>> d,
                                                                                          GeneratorImpl<Trace<E>> e,
                                                                                          GeneratorImpl<Trace<F>> f,
                                                                                          GeneratorImpl<Trace<G>> g,
                                                                                          Fn7<A, B, C, D, E, F, G, Out> combine) {
        return product7Impl(a, b, c, d, e, f, g,
                (ta, tb, tc, td, te, tf, tg) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult(), tf.getResult(), tg.getResult()),
                        source, asList(ta, tb, tc, td, te, tf, tg)));
    }

}
