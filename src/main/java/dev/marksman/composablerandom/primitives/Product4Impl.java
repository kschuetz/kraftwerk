package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn4;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product4Impl<A, B, C, D, Out> implements GeneratorState<Out> {
    private final GeneratorState<A> a;
    private final GeneratorState<B> b;
    private final GeneratorState<C> c;
    private final GeneratorState<D> d;
    private final Fn4<A, B, C, D, Out> combine;

    @Override
    public Result<? extends Seed, Out> run(Seed input) {
        Result<? extends Seed, A> r1 = a.run(input);
        Result<? extends Seed, B> r2 = b.run(r1.getNextState());
        Result<? extends Seed, C> r3 = c.run(r2.getNextState());
        Result<? extends Seed, D> r4 = d.run(r3.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue(), r4.getValue());
        return result(r4.getNextState(), result);
    }

    public static <A, B, C, D, Out> Product4Impl<A, B, C, D, Out> product4Impl(GeneratorState<A> a,
                                                                               GeneratorState<B> b,
                                                                               GeneratorState<C> c,
                                                                               GeneratorState<D> d,
                                                                               Fn4<A, B, C, D, Out> combine) {
        return new Product4Impl<>(a, b, c, d, combine);
    }

    public static <A, B, C, D, Out> GeneratorState<Trace<Out>> tracedProduct4Impl(Generator<Out> source,
                                                                                  GeneratorState<Trace<A>> a,
                                                                                  GeneratorState<Trace<B>> b,
                                                                                  GeneratorState<Trace<C>> c,
                                                                                  GeneratorState<Trace<D>> d,
                                                                                  Fn4<A, B, C, D, Out> combine) {
        return product4Impl(a, b, c, d,
                (ta, tb, tc, td) -> trace(
                        combine.apply(ta.getResult(), tb.getResult(), tc.getResult(), td.getResult()),
                        source, asList(ta, tb, tc, td)));
    }

}
