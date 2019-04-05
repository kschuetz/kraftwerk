package dev.marksman.composablerandom.benchmarks;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.builtin.Generators;
import dev.marksman.composablerandom.legacy.builtin.OldGenerators;

import static java.util.Arrays.asList;

public class BenchmarkSandbox {
    private static final int ITERATIONS = 5_000_000;

    private static void runTuples() {
        int iterations = 500_000;
        Runner.runOld("tuple", iterations, OldGenerators.tupled(OldGenerators.generateInt(),
                OldGenerators.generateFloat(),
                OldGenerators.generateDouble(),
                OldGenerators.generateLong(),
                OldGenerators.generateBoolean(),
                OldGenerators.generateByte(),
                OldGenerators.generateShort(),
                OldGenerators.tupled(OldGenerators.chooseOneOf("foo", "bar", "baz"),
                        OldGenerators.chooseOneFrom(asList(1, 2, 3, 4, 5, 6, 7, 8)))));

        Generator<Tuple8<Integer, Float, Double, Long, Boolean, Byte, Short, Tuple2<String, Integer>>> g = Generators.tupled(Generators.generateInt(),
                Generators.generateFloat(),
                Generators.generateDouble(),
                Generators.generateLong(),
                Generators.generateBoolean(),
                Generators.generateByte(),
                Generators.generateShort(),
                Generators.tupled(Generators.chooseOneOfValues("foo", "bar", "baz"),
                        Generators.chooseOneFromCollection(asList(1, 2, 3, 4, 5, 6, 7, 8))));
        Runner.run("tuple", iterations, g);

        Runner.runAlternate("tuple", iterations, g);
    }

    private static void sandbox1() {
        Runner.runOld("int", ITERATIONS, OldGenerators.generateInt());
        Runner.runOld("float", ITERATIONS, OldGenerators.generateFloat());
        Runner.runOld("double", ITERATIONS, OldGenerators.generateDouble());
        Runner.runOld("long", ITERATIONS, OldGenerators.generateLong());
        Runner.runOld("boolean", ITERATIONS, OldGenerators.generateBoolean());
        Runner.runOld("byte", ITERATIONS, OldGenerators.generateByte());
        Runner.runOld("short", ITERATIONS, OldGenerators.generateShort());
        Runner.runOld("gaussian", ITERATIONS, OldGenerators.generateGaussian());
        System.out.println("---");
        Runner.run("int", ITERATIONS, Generators.generateInt());
        Runner.run("float", ITERATIONS, Generators.generateFloat());
        Runner.run("double", ITERATIONS, Generators.generateDouble());
        Runner.run("long", ITERATIONS, Generators.generateLong());
        Runner.run("boolean", ITERATIONS, Generators.generateBoolean());
        Runner.run("byte", ITERATIONS, Generators.generateByte());
        Runner.run("short", ITERATIONS, Generators.generateShort());
        Runner.run("gaussian", ITERATIONS, Generators.generateGaussian());
        System.out.println("---");
        Runner.runAlternate("int", ITERATIONS, Generators.generateInt());
        Runner.runAlternate("float", ITERATIONS, Generators.generateFloat());
        Runner.runAlternate("double", ITERATIONS, Generators.generateDouble());
        Runner.runAlternate("long", ITERATIONS, Generators.generateLong());
        Runner.runAlternate("boolean", ITERATIONS, Generators.generateBoolean());
        Runner.runAlternate("byte", ITERATIONS, Generators.generateByte());
        Runner.runAlternate("short", ITERATIONS, Generators.generateShort());
        Runner.runAlternate("gaussian", ITERATIONS, Generators.generateGaussian());
        System.out.println("---");
        Runner.runRandomState("nextInt", ITERATIONS, RandomState::nextInt);
        Runner.runRandomState("nextFloat", ITERATIONS, RandomState::nextFloat);
        Runner.runRandomState("nextDouble", ITERATIONS, RandomState::nextDouble);
        Runner.runRandomState("nextLong", ITERATIONS, RandomState::nextLong);
        Runner.runRandomState("nextBoolean", ITERATIONS, RandomState::nextBoolean);
        Runner.runRandomState("nextGaussian", ITERATIONS, RandomState::nextGaussian);
    }

    private static void sandbox2() {
//        Runner.run("int", ITERATIONS, Generators.generateInt());
//        Runner.run("boolean", ITERATIONS, Generators.generateBoolean());
//        Runner.run("double", ITERATIONS, Generators.generateDouble());
//        Runner.run("long", ITERATIONS, Generators.generateLong());
//        Runner.run("float", ITERATIONS, Generators.generateFloat());
        Runner.run("int exclusive", ITERATIONS, Generators.generateIntExclusive(65537));
    }

    public static void main(String[] args) {
//        runTuples();
        sandbox1();
    }
}
