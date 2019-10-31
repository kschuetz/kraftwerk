package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn6;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product6Impl<A, B, C, D, E, F, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<A> a;
    private final GeneratorImpl<B> b;
    private final GeneratorImpl<C> c;
    private final GeneratorImpl<D> d;
    private final GeneratorImpl<E> e;
    private final GeneratorImpl<F> f;
    private final Fn6<A, B, C, D, E, F, Out> combine;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        Result<? extends LegacySeed, A> r1 = a.run(input);
        Result<? extends LegacySeed, B> r2 = b.run(r1.getNextState());
        Result<? extends LegacySeed, C> r3 = c.run(r2.getNextState());
        Result<? extends LegacySeed, D> r4 = d.run(r3.getNextState());
        Result<? extends LegacySeed, E> r5 = e.run(r4.getNextState());
        Result<? extends LegacySeed, F> r6 = f.run(r5.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue());
        return result(r6.getNextState(), result);
    }

    public static <A, B, C, D, E, F, Out> Product6Impl<A, B, C, D, E, F, Out> product6Impl(GeneratorImpl<A> a,
                                                                                           GeneratorImpl<B> b,
                                                                                           GeneratorImpl<C> c,
                                                                                           GeneratorImpl<D> d,
                                                                                           GeneratorImpl<E> e,
                                                                                           GeneratorImpl<F> f,
                                                                                           Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6Impl<>(a, b, c, d, e, f, combine);
    }

    public static <A, B, C, D, E, F, Out> GeneratorImpl<Trace<Out>> tracedProduct6Impl(Generator<Out> source,
                                                                                       GeneratorImpl<Trace<A>> a,
                                                                                       GeneratorImpl<Trace<B>> b,
                                                                                       GeneratorImpl<Trace<C>> c,
                                                                                       GeneratorImpl<Trace<D>> d,
                                                                                       GeneratorImpl<Trace<E>> e,
                                                                                       GeneratorImpl<Trace<F>> f,
                                                                                       Fn6<A, B, C, D, E, F, Out> combine) {
        return product6Impl(a, b, c, d, e, f,
                (ta, tb, tc, td, te, tf) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult(), tf.getResult()),
                        source, asList(ta, tb, tc, td, te, tf)));
    }

}
