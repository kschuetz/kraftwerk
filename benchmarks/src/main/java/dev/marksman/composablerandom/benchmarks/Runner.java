package dev.marksman.composablerandom.benchmarks;

import dev.marksman.composablerandom.*;
import dev.marksman.composablerandom.legacy.OldGeneratedStream;
import dev.marksman.composablerandom.random.StandardGen;

import java.util.function.Function;

import static dev.marksman.composablerandom.DefaultInterpreter.defaultInterpreter;

public class Runner {

    public static <A> void runOld(String label, int iterations, OldGenerator<A> generator) {
        OldGeneratedStream<A> stream = OldGeneratedStream.streamFrom(generator);
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            stream.next();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("old " + label + ": " + t + " ms");
    }

    public static <A> void run(String label, int iterations, Generator<A> generator) {
        DefaultInterpreter interpreter = defaultInterpreter();
        RandomState currentState = StandardGen.initStandardGen();

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<RandomState, A> result = interpreter.execute(currentState, generator);
            currentState = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println(label + ": " + t + " ms");
    }

    public static <A> void runAlternate(String label, int iterations, Generator<A> generator) {
        AlternateGeneratedStream<A> stream = AlternateGeneratedStream.alternateStreamFrom(generator);

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            stream.next();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("alternate " + label + ": " + t + " ms");
    }

    public static <A> void runRandomState(String label, int iterations, Function<RandomState, Result<? extends RandomState, A>> fn) {
        RandomState currentState = StandardGen.initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends RandomState, A> result = fn.apply(currentState);
            currentState = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("RandomState " + label + ": " + t + " ms");
    }
}
