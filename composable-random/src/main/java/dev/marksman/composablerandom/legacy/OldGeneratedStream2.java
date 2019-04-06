package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.Context;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.marksman.composablerandom.Initialize.createInitialRandomState;
import static dev.marksman.composablerandom.Initialize.randomInitialRandomState;
import static dev.marksman.composablerandom.legacy.OldInterpreter.defaultInterpreter;

public class OldGeneratedStream2<A> implements Iterator<A> {
    private final Generator<A> generator;
    private final OldInterpreter interpreter;
    private RandomState currentState;

    private OldGeneratedStream2(Generator<A> generator, OldInterpreter interpreter, RandomState initialState) {
        this.generator = generator;
        this.interpreter = interpreter;
        this.currentState = initialState;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public A next() {
        Result<RandomState, A> run;
        synchronized (this) {
            run = interpreter.execute(currentState, generator);
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

    public static <A> OldGeneratedStream2<A> streamFrom(Generator<A> generator, OldInterpreter interpreter, RandomState initialState) {
        return new OldGeneratedStream2<>(generator, interpreter, initialState);
    }

    public static <A> OldGeneratedStream2<A> streamFrom(Generator<A> generator, long initialSeedValue) {
        return streamFrom(generator, defaultInterpreter(), createInitialRandomState(initialSeedValue));
    }

    public static <A> OldGeneratedStream2<A> streamFrom(Generator<A> generator, Context context) {
        return streamFrom(generator, defaultInterpreter(context), randomInitialRandomState());
    }

    public static <A> OldGeneratedStream2<A> streamFrom(Generator<A> generator, Context context, long initialSeedValue) {
        return streamFrom(generator, defaultInterpreter(context), createInitialRandomState(initialSeedValue));
    }

    public static <A> OldGeneratedStream2<A> streamFrom(Generator<A> generator) {
        return streamFrom(generator, defaultInterpreter(), randomInitialRandomState());
    }

}
