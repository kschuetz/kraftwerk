package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product4Impl<A, B, C, D, Out> implements Generator<Out> {
    private final Generator<A> a;
    private final Generator<B> b;
    private final Generator<C> c;
    private final Generator<D> d;
    private final Fn4<A, B, C, D, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Result<? extends RandomState, D> r4 = d.run(r3.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue());
        return result(r4.getNextState(), result);
    }

    public static <A, B, C, D, Out> Product4Impl<A, B, C, D, Out> product4Impl(Generator<A> a,
                                                                               Generator<B> b,
                                                                               Generator<C> c,
                                                                               Generator<D> d,
                                                                               Fn4<A, B, C, D, Out> combine) {
        return new Product4Impl<>(a, b, c, d, combine);
    }

    public static <A, B, C, D, Out> Generator<Trace<Out>> tracedProduct4Impl(Generate<Out> source,
                                                                             Generator<Trace<A>> a,
                                                                             Generator<Trace<B>> b,
                                                                             Generator<Trace<C>> c,
                                                                             Generator<Trace<D>> d,
                                                                             Fn4<A, B, C, D, Out> combine) {
        return product4Impl(a, b, c, d,
                (ta, tb, tc, td) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult()),
                        source, asList(ta, tb, tc, td)));
    }

}
