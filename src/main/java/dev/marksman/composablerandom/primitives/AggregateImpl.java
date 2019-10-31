package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.composablerandom.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.Trace.trace;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregateImpl<Elem, Builder, Out> implements GeneratorImpl<Out> {
    private final Fn0<Builder> initialBuilderSupplier;
    private final Fn2<Builder, Elem, Builder> addFn;
    private final Fn1<Builder, Out> buildFn;
    private final Iterable<GeneratorImpl<Elem>> elements;

    @Override
    public Result<? extends LegacySeed, Out> run(LegacySeed input) {
        LegacySeed current = input;
        Builder builder = initialBuilderSupplier.apply();

        for (GeneratorImpl<Elem> element : elements) {
            Result<? extends LegacySeed, Elem> next = element.run(current);
            builder = addFn.apply(builder, next.getValue());
            current = next.getNextState();
        }
        return result(current, buildFn.apply(builder));
    }

    public static <Elem, Builder, Out> AggregateImpl<Elem, Builder, Out> aggregateImpl(
            Fn0<Builder> initialBuilderSupplier,
            Fn2<Builder, Elem, Builder> addFn,
            Fn1<Builder, Out> buildFn,
            Iterable<GeneratorImpl<Elem>> elements) {
        return new AggregateImpl<>(initialBuilderSupplier, addFn, buildFn, elements);
    }

    public static <Elem, Builder, Out> GeneratorImpl<Trace<Out>> tracedAggregateImpl(
            Generator<Out> source,
            Fn0<Builder> initialBuilderSupplier,
            Fn2<Builder, Elem, Builder> addFn,
            Fn1<Builder, Out> buildFn,
            Iterable<GeneratorImpl<Trace<Elem>>> elements) {
        return aggregateImpl(
                () -> new TraceCollector<>(initialBuilderSupplier.apply()),
                (tc, tracedElem) -> {
                    tc.state = addFn.apply(tc.state, tracedElem.getResult());
                    tc.traces.add(tracedElem);
                    return tc;
                },
                tc -> trace(buildFn.apply(tc.state), source, tc.traces),
                elements);
    }

}
