package dev.marksman.composablerandom.benchmarks;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.builtin.Generators;
import dev.marksman.composablerandom.domain.Characters;

import static dev.marksman.composablerandom.builtin.Generators.*;
import static java.util.Arrays.asList;

public class BenchmarkSandbox {
    private static final int ITERATIONS = 5_000_000;

    private static void runTuples() {
        int iterations = 500_000;
        Generator<Tuple8<Integer, Float, Double, Long, Boolean, Byte, Short, Tuple2<String, Integer>>> g = Generators.tupled(generateInt(),
                Generators.generateFloat(),
                Generators.generateDouble(),
                generateLong(),
                Generators.generateBoolean(),
                Generators.generateByte(),
                Generators.generateShort(),
                Generators.tupled(Generators.chooseOneOfValues("foo", "bar", "baz"),
                        Generators.chooseOneFromCollection(asList(1, 2, 3, 4, 5, 6, 7, 8))));
        Runner.runMark2("tuple", iterations, g);

        Runner.run("tuple", iterations, g);
    }

    private static void sandbox1() {
        Runner.runMark2("int", ITERATIONS, generateInt());
        Runner.runMark2("float", ITERATIONS, Generators.generateFloat());
        Runner.runMark2("double", ITERATIONS, Generators.generateDouble());
        Runner.runMark2("long", ITERATIONS, generateLong());
        Runner.runMark2("boolean", ITERATIONS, Generators.generateBoolean());
        Runner.runMark2("byte", ITERATIONS, Generators.generateByte());
        Runner.runMark2("short", ITERATIONS, Generators.generateShort());
        Runner.runMark2("gaussian", ITERATIONS, Generators.generateGaussian());
        System.out.println("---");
        Runner.run("int", ITERATIONS, generateInt());
        Runner.run("float", ITERATIONS, Generators.generateFloat());
        Runner.run("double", ITERATIONS, Generators.generateDouble());
        Runner.run("long", ITERATIONS, generateLong());
        Runner.run("boolean", ITERATIONS, Generators.generateBoolean());
        Runner.run("byte", ITERATIONS, Generators.generateByte());
        Runner.run("short", ITERATIONS, Generators.generateShort());
        Runner.run("gaussian", ITERATIONS, Generators.generateGaussian());
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
        Runner.runMark2("int exclusive", ITERATIONS, Generators.generateIntExclusive(65537));
    }

    private static void sandbox3() {
        Generator<String> g = generateInt(1, 65537)
                .flatMap(i -> generateLong(i, i + 1000))
                .flatMap(l -> generateString(l.intValue() & 31,
                        generateStringFromCharacters(Characters.alphaLower())));

        Runner.runMark2("sandbox3", 100000, g);
        Runner.run("sandbox3", 100000, g);
    }

    public static void main(String[] args) {
        runTuples();
//        sandbox3();
    }
}
