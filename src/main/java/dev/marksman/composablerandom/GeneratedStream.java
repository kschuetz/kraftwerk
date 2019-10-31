package dev.marksman.composablerandom;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.marksman.composablerandom.DefaultInterpreter.defaultInterpreter;
import static dev.marksman.composablerandom.Initialize.createInitialSeed;
import static dev.marksman.composablerandom.Initialize.randomInitialSeed;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;

public class GeneratedStream<A> implements Iterator<A> {
    private final GeneratorImpl<A> gen;
    private LegacySeed currentState;

    private GeneratedStream(GeneratorImpl<A> gen, LegacySeed initialSeed) {
        this.gen = gen;
        this.currentState = initialSeed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public A next() {
        Result<? extends LegacySeed, A> run;
        synchronized (this) {
            run = this.gen.run(currentState);
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

    public static <A> GeneratedStream<A> streamFrom(GeneratorImpl<A> gen, LegacySeed initialSeed) {
        return new GeneratedStream<>(gen, initialSeed);
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, LegacySeed initialSeed) {
        return new GeneratedStream<>(compile(gen), initialSeed);
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, long initialSeedValue) {
        return streamFrom(compile(gen), createInitialSeed(initialSeedValue));
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, Parameters parameters) {
        return streamFrom(compile(gen), randomInitialSeed());
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, Parameters parameters, long initialSeedValue) {
        return streamFrom(compile(gen), createInitialSeed(initialSeedValue));
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen) {
        return streamFrom(compile(gen), randomInitialSeed());
    }

    private static <A> GeneratorImpl<A> compile(Generator<A> gen) {
        Parameters parameters = defaultParameters();
        return defaultInterpreter().compile(parameters, gen);
    }

}
