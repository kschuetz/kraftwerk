package dev.marksman.kraftwerk;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.marksman.kraftwerk.Initialize.createInitialSeed;
import static dev.marksman.kraftwerk.Initialize.randomInitialSeed;
import static dev.marksman.kraftwerk.StandardParameters.defaultParameters;

public class GeneratedStream<A> implements Iterator<A> {
    private final Generate<A> gen;
    private Seed currentState;

    private GeneratedStream(Generate<A> gen, Seed initialSeed) {
        this.gen = gen;
        this.currentState = initialSeed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public A next() {
        Result<? extends Seed, A> run;
        synchronized (this) {
            run = this.gen.apply(currentState);
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

    public static <A> GeneratedStream<A> streamFrom(Generate<A> gen, Seed initialSeed) {
        return new GeneratedStream<>(gen, initialSeed);
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, Parameters parameters, Seed initialSeed) {
        return new GeneratedStream<>(gen.prepare(parameters), initialSeed);
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, long initialSeedValue) {
        return streamFrom(gen, defaultParameters(), createInitialSeed(initialSeedValue));
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, Parameters parameters) {
        return streamFrom(gen, defaultParameters(), randomInitialSeed());
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen, Parameters parameters, long initialSeedValue) {
        return streamFrom(gen, defaultParameters(), createInitialSeed(initialSeedValue));
    }

    public static <A> GeneratedStream<A> streamFrom(Generator<A> gen) {
        return streamFrom(gen, defaultParameters(), randomInitialSeed());
    }

}