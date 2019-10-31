package dev.marksman.composablerandom.legacy.primitives;

import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product4Impl<A, B, C, D, Out> implements GeneratorImpl<Out> {
    private final GeneratorImpl<A> a;
    private final GeneratorImpl<B> b;
    private final GeneratorImpl<C> c;
    private final GeneratorImpl<D> d;
    private final Fn4<A, B, C, D, Out> combine;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        Result<? extends LegacySeed, A> r1 = a.run(input);
        Result<? extends LegacySeed, B> r2 = b.run(r1.getNextState());
        Result<? extends LegacySeed, C> r3 = c.run(r2.getNextState());
        Result<? extends LegacySeed, D> r4 = d.run(r3.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue());
        return result(r4.getNextState(), result);
    }

    public static <A, B, C, D, Out> Product4Impl<A, B, C, D, Out> product4Impl(GeneratorImpl<A> a,
                                                                               GeneratorImpl<B> b,
                                                                               GeneratorImpl<C> c,
                                                                               GeneratorImpl<D> d,
                                                                               Fn4<A, B, C, D, Out> combine) {
        return new Product4Impl<>(a, b, c, d, combine);
    }

    public static <A, B, C, D, Out> GeneratorImpl<Trace<Out>> tracedProduct4Impl(Generator<Out> source,
                                                                                 GeneratorImpl<Trace<A>> a,
                                                                                 GeneratorImpl<Trace<B>> b,
                                                                                 GeneratorImpl<Trace<C>> c,
                                                                                 GeneratorImpl<Trace<D>> d,
                                                                                 Fn4<A, B, C, D, Out> combine) {
        return product4Impl(a, b, c, d,
                (ta, tb, tc, td) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult()),
                        source, asList(ta, tb, tc, td)));
    }

}
