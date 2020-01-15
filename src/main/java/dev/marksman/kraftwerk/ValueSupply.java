package dev.marksman.kraftwerk;

import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ValueSupply<A> implements ImmutableNonEmptyIterable<A> {
    private final Generate<A> gen;
    private final A firstValue;
    private final Seed state;

    private ValueSupply(Generate<A> gen, Seed initialState) {
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
        return () -> new TailIterator<>(gen, state);
    }

    public Stream<A> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private static class TailIterator<A> implements Iterator<A> {
        private final Generate<A> gen;
        private Seed state;

        private TailIterator(Generate<A> gen, Seed state) {
            this.gen = gen;
            this.state = state;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public A next() {
            Result<? extends Seed, A> run;
            synchronized (this) {
                run = this.gen.apply(state);
                state = run.getNextState();
            }

            return run.getValue();
        }
    }

    static <A> ValueSupply<A> valueSupply(Generate<A> gen, Seed initialState) {
        return new ValueSupply<>(gen, initialState);
    }

}
