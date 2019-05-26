package benchmarks;

import dev.marksman.composablerandom.*;

import java.util.function.Function;

import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.TracingInterpreter.tracingInterpreter;
import static dev.marksman.composablerandom.legacy.DefaultInterpreterMark3.defaultInterpreter;
import static dev.marksman.composablerandom.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Runner {

    public static <A> void runMark3(String label, int iterations, Generator<A> generator) {
        CompiledGenerator<A> compiled = defaultInterpreter(defaultParameters()).compile(generator);

        RandomState current = initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends RandomState, A> result = compiled.run(current);
            current = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("mark 3 " + label + ": " + t + " ms");
    }

    public static <A> void run(String label, int iterations, Generator<A> generator) {

        Interpreter overrides = Interpreter.<A>interpreter((InterpreterContext ctx, Generator<A> gen) -> {
            if (gen instanceof Generator.NextInt) {
                //noinspection unchecked
                return (CompiledGenerator<A>) constantImpl(1);
            }
            return ctx.callNextHandler(gen);
        });

        Interpreter interpreter = DefaultInterpreter.defaultInterpreter()
                .overrideWith(overrides);
        CompiledGenerator<A> compiled = interpreter.compile(defaultParameters(), generator);
        RandomState current = initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends RandomState, A> result = compiled.run(current);
            current = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println(label + ": " + t + " ms");
    }

    public static <A> void runTraced(String label, int iterations, Generator<A> generator) {
        CompiledGenerator<Trace<A>> compile = tracingInterpreter().compile(generator);
        GeneratedStream<Trace<A>> stream = GeneratedStream.streamFrom(compile, initStandardGen());

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            stream.next();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("traced " + label + ": " + t + " ms");
    }

    public static <A> void runRandomState(String label, int iterations, Function<RandomState, Result<? extends RandomState, A>> fn) {
        RandomState currentState = initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends RandomState, A> result = fn.apply(currentState);
            currentState = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("RandomState " + label + ": " + t + " ms");
    }
}
