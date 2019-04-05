package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

import static dev.marksman.composablerandom.Result.result;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregateImpl<Elem, Builder, Output> implements Generate<Output> {
    private final Supplier<Builder> initialBuilderSupplier;
    private final Fn2<Builder, Elem, Builder> addFn;
    private final Fn1<Builder, Output> buildFn;
    private final Iterable<Generate<Elem>> instructions;

    @Override
    public Result<? extends RandomState, Output> generate(RandomState input) {
        RandomState current = input;
        Builder builder = initialBuilderSupplier.get();

        for (Generate<Elem> instruction : instructions) {
            Result<? extends RandomState, Elem> next = instruction.generate(current);
            builder = addFn.apply(builder, next.getValue());
            current = next.getNextState();
        }
        return result(current, buildFn.apply(builder));
    }

    public static <Elem, Builder, Output> AggregateImpl<Elem, Builder, Output> aggregateImpl(
            Supplier<Builder> initialBuilderSupplier,
            Fn2<Builder, Elem, Builder> addFn,
            Fn1<Builder, Output> buildFn,
            Iterable<Generate<Elem>> instructions) {
        return new AggregateImpl<>(initialBuilderSupplier, addFn, buildFn, instructions);
    }
}
