package dev.marksman.composablerandom.benchmarks;

import dev.marksman.composablerandom.GeneratedStream;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.legacy.OldInterpreter;
import dev.marksman.composablerandom.random.StandardGen;

import java.util.function.Function;

import static dev.marksman.composablerandom.legacy.OldInterpreter.defaultInterpreter;

public class Runner {

    public static <A> void runMark2(String label, int iterations, Generator<A> generator) {
        OldInterpreter interpreter = defaultInterpreter();
        RandomState currentState = StandardGen.initStandardGen();

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<RandomState, A> result = interpreter.execute(currentState, generator);
            currentState = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("mk2 " + label + ": " + t + " ms");
    }

    public static <A> void run(String label, int iterations, Generator<A> generator) {
        GeneratedStream<A> stream = GeneratedStream.streamFrom(generator);

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            stream.next();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println(label + ": " + t + " ms");
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
