package dev.marksman.kraftwerk.spike;

import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import dev.marksman.kraftwerk.Generate;
import dev.marksman.kraftwerk.Result;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.ValueSupplyIterator;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ValueSupply<A> implements ImmutableNonEmptyIterable<A> {
    private final Generate<A> gen;
    private final A firstValue;
    private final Seed state;

    public ValueSupply(Generate<A> gen, Seed initialState) {
        this.gen = gen;
        Result<? extends Seed, A> r1 = gen.apply(initialState);
        this.firstValue = r1.getValue();
        this.state = r1.getNextState();
    }

    @Override
    public A head() {
        return firstValue;
    }

    @Override
    public ImmutableIterable<A> tail() {
        return () -> new ValueSupplyIterator<>(gen, state);
    }

    public Stream<A> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
