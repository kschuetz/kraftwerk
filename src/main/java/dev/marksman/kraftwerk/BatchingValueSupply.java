package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.functions.builtin.fn1.Flatten;
import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import dev.marksman.enhancediterables.NonEmptyIterable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BatchingValueSupply<A> implements ValueSupply<A> {
    private final ImmutableNonEmptyIterable<A> delegate;

    public BatchingValueSupply(ValueSupply<NonEmptyIterable<A>> inner) {
        Iterable<A> flatten = Flatten.flatten(inner);
        ImmutableIterable<A> immutable = flatten::iterator;
        delegate = immutable.toNonEmpty().orElseThrow(AssertionError::new);
    }

    @Override
    public Stream stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public ImmutableIterable<A> tail() {
        return delegate.tail();
    }

    @Override
    public A head() {
        return delegate.head();
    }
}
