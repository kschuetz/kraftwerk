package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn5;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product5Impl<A, B, C, D, E, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<A> a;
    private final GeneratorImpl<B> b;
    private final GeneratorImpl<C> c;
    private final GeneratorImpl<D> d;
    private final GeneratorImpl<E> e;
    private final Fn5<A, B, C, D, E, Out> combine;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        Result<? extends LegacySeed, A> r1 = a.run(input);
        Result<? extends LegacySeed, B> r2 = b.run(r1.getNextState());
        Result<? extends LegacySeed, C> r3 = c.run(r2.getNextState());
        Result<? extends LegacySeed, D> r4 = d.run(r3.getNextState());
        Result<? extends LegacySeed, E> r5 = e.run(r4.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue());
        return result(r5.getNextState(), result);
    }

    public static <A, B, C, D, E, Out> Product5Impl<A, B, C, D, E, Out> product5Impl(GeneratorImpl<A> a,
                                                                                     GeneratorImpl<B> b,
                                                                                     GeneratorImpl<C> c,
                                                                                     GeneratorImpl<D> d,
                                                                                     GeneratorImpl<E> e,
                                                                                     Fn5<A, B, C, D, E, Out> combine) {
        return new Product5Impl<>(a, b, c, d, e, combine);
    }

    public static <A, B, C, D, E, Out> GeneratorImpl<Trace<Out>> tracedProduct5Impl(Generator<Out> source,
                                                                                    GeneratorImpl<Trace<A>> a,
                                                                                    GeneratorImpl<Trace<B>> b,
                                                                                    GeneratorImpl<Trace<C>> c,
                                                                                    GeneratorImpl<Trace<D>> d,
                                                                                    GeneratorImpl<Trace<E>> e,
                                                                                    Fn5<A, B, C, D, E, Out> combine) {
        return product5Impl(a, b, c, d, e,
                (ta, tb, tc, td, te) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult()),
                        source, asList(ta, tb, tc, td, te)));
    }

}
