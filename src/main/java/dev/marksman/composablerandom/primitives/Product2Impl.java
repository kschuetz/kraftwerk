package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;
import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product2Impl<A, B, Out> implements GeneratorState<Out> {
    private final GeneratorState<A> a;
    private final GeneratorState<B> b;
    private final Fn2<A, B, Out> combine;

    @Override
    public Result<? extends RandomState, Out> run(RandomState input) {
        Result<? extends RandomState, A> r1 = a.run(input);
        Result<? extends RandomState, B> r2 = b.run(r1.getNextState());
        Out result = combine.apply(r1.getValue(), r2.getValue());
        return result(r2.getNextState(), result);
    }

    public static <A, B, Out> Product2Impl<A, B, Out> product2Impl(GeneratorState<A> a,
                                                                   GeneratorState<B> b,
                                                                   Fn2<A, B, Out> combine) {
        return new Product2Impl<>(a, b, combine);
    }

    public static <A, B, Out> GeneratorState<Trace<Out>> tracedProduct2Impl(Generator<Out> source,
                                                                            GeneratorState<Trace<A>> a,
                                                                            GeneratorState<Trace<B>> b,
                                                                            Fn2<A, B, Out> combine) {
        return product2Impl(a, b,
                (ta, tb) -> trace(combine.apply(ta.getResult(), tb.getResult()),
                        source, asList(ta, tb)));
    }

}
