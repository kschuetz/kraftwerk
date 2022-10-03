package software.kes.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.enhancediterables.ImmutableIterable;
import software.kes.enhancediterables.ImmutableNonEmptyIterable;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An infinite sequence of values of type {@code A}.
 * <p>
 * A {@code ValueSupply} does not have internal state, so it can be iterated multiple times, and for each time will return
 * the same sequence.
 * <p>
 * Each element of a {@code ValueSupply} is lazily computed, therefore, a {@code ValueSupply} only requires a small, constant
 * amount of memory.
 *
 * @param <A> the element type
 */
public final class ValueSupply<A> implements ImmutableNonEmptyIterable<A> {
    private final Fn1<Seed, Result<? extends Seed, A>> gen;
    private final A firstValue;
    private final Seed state;

    private ValueSupply(Fn1<Seed, Result<? extends Seed, A>> gen, Seed initialState) {
        this.gen = gen;
        Result<? extends Seed, A> r1 = gen.apply(initialState);
        this.firstValue = r1.getValue();
        this.state = r1.getNextState();
    }

    static <A> ValueSupply<A> valueSupply(Fn1<Seed, Result<? extends Seed, A>> gen, Seed initialState) {
        return new ValueSupply<>(gen, initialState);
    }

    @Override
    public A head() {
        return firstValue;
    }

    @Override
    public ImmutableIterable<A> tail() {
        return () -> new TailIterator<>(gen, state);
    }

    @Override
    public ValueSupply<A> drop(int count) {
        if (count <= 0) {
            return this;
        } else {
            Seed current = state;
            for (int i = 0; i < count; i++) {
                current = gen.apply(current).getNextState();
            }
            return new ValueSupply<>(gen, current);
        }
    }

    /**
     * Creates a {@link Stream} from this {@code ValueSupply}.
     *
     * @return a {@code Stream<A>}
     */
    public Stream<A> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private static class TailIterator<A> implements Iterator<A> {
        private final Fn1<Seed, Result<? extends Seed, A>> gen;
        private Seed state;

        private TailIterator(Fn1<Seed, Result<? extends Seed, A>> gen, Seed state) {
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
}
