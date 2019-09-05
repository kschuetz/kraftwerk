package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn5;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product5Impl<A, B, C, D, E, Out> implements GeneratorState<Out> {
    private final GeneratorState<A> a;
    private final GeneratorState<B> b;
    private final GeneratorState<C> c;
    private final GeneratorState<D> d;
    private final GeneratorState<E> e;
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

    public static <A, B, C, D, E, Out> Product5Impl<A, B, C, D, E, Out> product5Impl(GeneratorState<A> a,
                                                                                     GeneratorState<B> b,
                                                                                     GeneratorState<C> c,
                                                                                     GeneratorState<D> d,
                                                                                     GeneratorState<E> e,
                                                                                     Fn5<A, B, C, D, E, Out> combine) {
        return new Product5Impl<>(a, b, c, d, e, combine);
    }

    public static <A, B, C, D, E, Out> GeneratorState<Trace<Out>> tracedProduct5Impl(Generator<Out> source,
                                                                                     GeneratorState<Trace<A>> a,
                                                                                     GeneratorState<Trace<B>> b,
                                                                                     GeneratorState<Trace<C>> c,
                                                                                     GeneratorState<Trace<D>> d,
                                                                                     GeneratorState<Trace<E>> e,
                                                                                     Fn5<A, B, C, D, E, Out> combine) {
        return product5Impl(a, b, c, d, e,
                (ta, tb, tc, td, te) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult()),
                        source, asList(ta, tb, tc, td, te)));
    }

}
