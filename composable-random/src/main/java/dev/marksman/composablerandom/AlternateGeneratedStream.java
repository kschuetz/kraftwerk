package dev.marksman.composablerandom;

import dev.marksman.composablerandom.spike.AlternateInterpreter;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.marksman.composablerandom.Initialize.createInitialRandomState;
import static dev.marksman.composablerandom.Initialize.randomInitialRandomState;
import static dev.marksman.composablerandom.StandardContext.defaultContext;
import static dev.marksman.composablerandom.spike.AlternateInterpreter.alternateInterpreter;

public class AlternateGeneratedStream<A> implements Iterator<A> {
    private final Generate<A> generate;
    private RandomState currentState;

    private AlternateGeneratedStream(Generate<A> generate, RandomState initialState) {
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
            run = this.generate.generate(currentState);
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

    public static <A> AlternateGeneratedStream<A> alternateStreamFrom(Generate<A> generate, RandomState initialState) {
        return new AlternateGeneratedStream<>(generate, initialState);
    }

    public static <A> AlternateGeneratedStream<A> alternateStreamFrom(Generator<A> generator, long initialSeedValue) {
        return alternateStreamFrom(compile(generator), createInitialRandomState(initialSeedValue));
    }

    public static <A> AlternateGeneratedStream<A> alternateStreamFrom(Generator<A> generator, Context context) {
        return alternateStreamFrom(compile(generator), randomInitialRandomState());
    }

    public static <A> AlternateGeneratedStream<A> alternateStreamFrom(Generator<A> generator, Context context, long initialSeedValue) {
        return alternateStreamFrom(compile(generator), createInitialRandomState(initialSeedValue));
    }

    public static <A> AlternateGeneratedStream<A> alternateStreamFrom(Generator<A> generator) {
        return alternateStreamFrom(compile(generator), randomInitialRandomState());
    }

    private static <A> Generate<A> compile(Generator<A> generator) {
        AlternateInterpreter interpreter = alternateInterpreter(defaultContext());
        return interpreter.compile(generator.getInstruction());
    }

}
