package benchmarks;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.*;

import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.TracingInterpreter.tracingInterpreter;
import static dev.marksman.composablerandom.legacy.DefaultInterpreterMark3.defaultInterpreter;
import static dev.marksman.composablerandom.primitives.ConstantImpl.constantImpl;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Runner {

    public static <A> void runMark3(String label, int iterations, Generator<A> gen) {
        GeneratorImpl<A> compiled = defaultInterpreter(defaultParameters()).compile(gen);

        Seed current = initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends Seed, A> result = compiled.run(current);
            current = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("mark 3 " + label + ": " + t + " ms");
    }

    public static <A> void run(String label, int iterations, Generator<A> gen) {

        Interpreter overrides = Interpreter.<A>interpreter((InterpreterContext ctx, Generator<A> g) -> {
            if (g instanceof Generator.NextInt) {
                //noinspection unchecked
                return (GeneratorImpl<A>) constantImpl(1);
            }
            return ctx.callNextHandler(g);
        });

        Interpreter interpreter = DefaultInterpreter.defaultInterpreter()
                .overrideWith(overrides);
        GeneratorImpl<A> compiled = interpreter.compile(defaultParameters(), gen);
        Seed current = initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends Seed, A> result = compiled.run(current);
            current = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println(label + ": " + t + " ms");
    }

    public static <A> void runTraced(String label, int iterations, Generator<A> gen) {
        GeneratorImpl<Trace<A>> compile = tracingInterpreter().compile(gen);
        GeneratedStream<Trace<A>> stream = GeneratedStream.streamFrom(compile, initStandardGen());

        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            stream.next();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("traced " + label + ": " + t + " ms");
    }

    public static <A> void runSeed(String label, int iterations, Fn1<Seed, Result<? extends Seed, A>> fn) {
        Seed currentState = initStandardGen();
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Result<? extends Seed, A> result = fn.apply(currentState);
            currentState = result.getNextState();
        }
        long t = System.currentTimeMillis() - t0;

        System.out.println("Seed " + label + ": " + t + " ms");
    }
}
