package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn3;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product3Impl<A, B, C, Out> implements GeneratorState<Out> {
    private final GeneratorState<A> a;
    private final GeneratorState<B> b;
    private final GeneratorState<C> c;
    private final Fn3<A, B, C, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Result<? extends RandomState, C> r3 = c.run(r2.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue(), r3.getValue());
        return result(r3.getNextState(), result);
    }

    public static <A, B, C, Out> Product3Impl<A, B, C, Out> product3Impl(GeneratorState<A> a,
                                                                         GeneratorState<B> b,
                                                                         GeneratorState<C> c,
                                                                         Fn3<A, B, C, Out> combine) {
        return new Product3Impl<>(a, b, c, combine);
    }

    public static <A, B, C, Out> GeneratorState<Trace<Out>> tracedProduct3Impl(Generator<Out> source,
                                                                               GeneratorState<Trace<A>> a,
                                                                               GeneratorState<Trace<B>> b,
                                                                               GeneratorState<Trace<C>> c,
                                                                               Fn3<A, B, C, Out> combine) {
        return product3Impl(a, b, c,
                (ta, tb, tc) -> trace(combine.apply(ta.getResult(), tb.getResult(), tc.getResult()),
                        source, asList(ta, tb, tc)));
    }

}
