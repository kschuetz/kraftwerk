package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.marksman.composablerandom.Initialize.createInitialState;
import static dev.marksman.composablerandom.Initialize.randomInitialState;

public class OldGeneratedStream<A> implements Iterator<A> {
    private final OldGenerator<A> generator;
    private State currentState;

    private OldGeneratedStream(OldGenerator<A> generator, State initialState) {
        this.generator = generator;
        this.currentState = initialState;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public A next() {
        Result<State, A> run;
        synchronized (this) {
            run = generator.run(currentState);
            currentState = run.getNextState();
        }
        return run.getValue();
    }

    public ArrayList<A> next(int n) {
        if (n > 0) {
            ArrayList<A> result = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                result.add(next());
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public void writeToArray(A[] target) {
        writeToArray(target, 0, target.length);
    }

    public void writeToArray(A[] target, int startIndex) {
        writeToArray(target, startIndex, target.length);
    }

    public void writeToArray(A[] target, int startIndex, int endIndexExclusive) {
        endIndexExclusive = Math.max(startIndex, endIndexExclusive);
        if (startIndex < 0) {
            throw new IndexOutOfBoundsException("invalid startIndex: " + startIndex);
        }
        if (endIndexExclusive > target.length) {
            throw new ArrayIndexOutOfBoundsException("invalid endIndex: " + endIndexExclusive);
        }
        for (int i = startIndex; i < endIndexExclusive; i++) {
            target[i] = next();
        }
    }

    public static <A> OldGeneratedStream<A> streamFrom(OldGenerator<A> generator, State initialState) {
        return new OldGeneratedStream<>(generator, initialState);
    }

    public static <A> OldGeneratedStream<A> streamFrom(OldGenerator<A> generator, RandomState initialState) {
        return streamFrom(generator, createInitialState(initialState));
    }

    public static <A> OldGeneratedStream<A> streamFrom(OldGenerator<A> generator, long initialSeedValue) {
        return streamFrom(generator, createInitialState(initialSeedValue));
    }

    public static <A> OldGeneratedStream<A> streamFrom(OldGenerator<A> generator, OldContext context) {
        return streamFrom(generator, randomInitialState(context));
    }

    public static <A> OldGeneratedStream<A> streamFrom(OldGenerator<A> generator, OldContext context, long initialSeedValue) {
        return streamFrom(generator, createInitialState(initialSeedValue, context));
    }

    public static <A> OldGeneratedStream<A> streamFrom(OldGenerator<A> generator) {
        return streamFrom(generator, randomInitialState());
    }

}
