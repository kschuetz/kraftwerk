package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn6;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product6Impl<A, B, C, D, E, F, Out> implements GeneratorState<Out> {
    private final GeneratorState<A> a;
    private final GeneratorState<B> b;
    private final GeneratorState<C> c;
    private final GeneratorState<D> d;
    private final GeneratorState<E> e;
    private final GeneratorState<F> f;
    private final Fn6<A, B, C, D, E, F, Out> combine;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        Result<? extends Seed, A> r1 = a.run(input);
        Result<? extends Seed, B> r2 = b.run(r1.getNextState());
        Result<? extends Seed, C> r3 = c.run(r2.getNextState());
        Result<? extends Seed, D> r4 = d.run(r3.getNextState());
        Result<? extends Seed, E> r5 = e.run(r4.getNextState());
        Result<? extends Seed, F> r6 = f.run(r5.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue(),
                r5.getValue(), r6.getValue());
        return result(r6.getNextState(), result);
    }

    public static <A, B, C, D, E, F, Out> Product6Impl<A, B, C, D, E, F, Out> product6Impl(GeneratorState<A> a,
                                                                                           GeneratorState<B> b,
                                                                                           GeneratorState<C> c,
                                                                                           GeneratorState<D> d,
                                                                                           GeneratorState<E> e,
                                                                                           GeneratorState<F> f,
                                                                                           Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6Impl<>(a, b, c, d, e, f, combine);
    }

    public static <A, B, C, D, E, F, Out> GeneratorState<Trace<Out>> tracedProduct6Impl(Generator<Out> source,
                                                                                        GeneratorState<Trace<A>> a,
                                                                                        GeneratorState<Trace<B>> b,
                                                                                        GeneratorState<Trace<C>> c,
                                                                                        GeneratorState<Trace<D>> d,
                                                                                        GeneratorState<Trace<E>> e,
                                                                                        GeneratorState<Trace<F>> f,
                                                                                        Fn6<A, B, C, D, E, F, Out> combine) {
        return product6Impl(a, b, c, d, e, f,
                (ta, tb, tc, td, te, tf) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult(),
                                te.getResult(), tf.getResult()),
                        source, asList(ta, tb, tc, td, te, tf)));
    }

}
