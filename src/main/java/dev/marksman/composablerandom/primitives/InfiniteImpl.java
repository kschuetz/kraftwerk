package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.GeneratedStream;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InfiniteImpl<A> implements CompiledGenerator<ImmutableNonEmptyIterable<A>> {
    private final CompiledGenerator<A> inner;

    @Override
    public Result<? extends RandomState, ImmutableNonEmptyIterable<A>> run(RandomState input) {
        Result<? extends RandomState, Long> next = input.nextLong();
        return Result.result(next.getNextState(),
                createIterable(initStandardGen(next.getValue())));
    }

    // TODO:  EnhancedIterables should have unsafeCreate for NonEmptyIterable
    private ImmutableNonEmptyIterable<A> createIterable(RandomState initialState) {
        ImmutableIterable<A> iterable = () -> GeneratedStream.streamFrom(inner, initialState);
        return iterable.toNonEmpty().orElseThrow(AssertionError::new);
    }

    public static <A> InfiniteImpl<A> infiniteImpl(CompiledGenerator<A> inner) {
        return new InfiniteImpl<>(inner);
    }
}
