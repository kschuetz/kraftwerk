package dev.marksman.composablerandom;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.marksman.composablerandom.DefaultInterpreter.defaultInterpreter;
import static dev.marksman.composablerandom.Initialize.createInitialRandomState;
import static dev.marksman.composablerandom.Initialize.randomInitialRandomState;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;

public class GeneratedStream<A> implements Iterator<A> {
    private final Generator<A> generate;
    private RandomState currentState;

    private GeneratedStream(Generator<A> generate, RandomState initialState) {
        this.generate = generate;
        this.currentState = initialState;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public A next() {
        Result<? extends RandomState, A> run;
        synchronized (this) {
            run = this.generate.run(currentState);
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

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, RandomState initialState) {
        return new GeneratedStream<>(gen, initialState);
    }

    public static <A> GeneratedStream<A> streamFrom(Generate<A> gen, RandomState initialState) {
        return new GeneratedStream<>(compile(gen), initialState);
    }

    public static <A> GeneratedStream<A> streamFrom(Generate<A> gen, long initialSeedValue) {
        return streamFrom(compile(gen), createInitialRandomState(initialSeedValue));
    }

    public static <A> GeneratedStream<A> streamFrom(Generate<A> gen, Parameters parameters) {
        return streamFrom(compile(gen), randomInitialRandomState());
    }

    public static <A> GeneratedStream<A> streamFrom(Generate<A> gen, Parameters parameters, long initialSeedValue) {
        return streamFrom(compile(gen), createInitialRandomState(initialSeedValue));
    }

    public static <A> GeneratedStream<A> streamFrom(Generate<A> gen) {
        return streamFrom(compile(gen), randomInitialRandomState());
    }

    private static <A> Generator<A> compile(Generate<A> gen) {
        Parameters parameters = defaultParameters();
        return defaultInterpreter().compile(parameters, gen);
    }

}
