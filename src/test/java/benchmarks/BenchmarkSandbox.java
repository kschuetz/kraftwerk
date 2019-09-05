package benchmarks;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.domain.Characters;

import static dev.marksman.composablerandom.Generator.*;
import static java.util.Arrays.asList;

public class BenchmarkSandbox {
    private static final int ITERATIONS = 5_000_000;

    private static void runTuples() {
        int iterations = 500_000;
        Generator<Tuple8<Integer, Float, Double, Long, Boolean, Byte, Short, Tuple2<String, Integer>>> g = tupled(generateInt(),
                generateFloat(),
                generateDouble(),
                generateLong(),
                generateBoolean(),
                generateByte(),
                generateShort(),
                tupled(chooseOneOfValues("foo", "bar", "baz"),
                        chooseOneFromCollection(asList(1, 2, 3, 4, 5, 6, 7, 8))));

        Runner.runMark3("tuple", iterations, g);
        Runner.run("tuple", iterations, g);
        Runner.runTraced("tuple", iterations, g);
    }

    private static void sandbox1() {
        Runner.runMark3("int", ITERATIONS, generateInt());
        Runner.runMark3("float", ITERATIONS, generateFloat());
        Runner.runMark3("double", ITERATIONS, generateDouble());
        Runner.runMark3("long", ITERATIONS, generateLong());
        Runner.runMark3("boolean", ITERATIONS, generateBoolean());
        Runner.runMark3("byte", ITERATIONS, generateByte());
        Runner.runMark3("short", ITERATIONS, generateShort());
        Runner.runMark3("gaussian", ITERATIONS, generateGaussian());
        System.out.println("---");
        Runner.runRandomState("nextInt", ITERATIONS, RandomState::nextInt);
        Runner.runRandomState("nextFloat", ITERATIONS, RandomState::nextFloat);
        Runner.runRandomState("nextDouble", ITERATIONS, RandomState::nextDouble);
        Runner.runRandomState("nextLong", ITERATIONS, RandomState::nextLong);
        Runner.runRandomState("nextBoolean", ITERATIONS, RandomState::nextBoolean);
        Runner.runRandomState("nextGaussian", ITERATIONS, RandomState::nextGaussian);
    }

    private static void sandbox2() {
//        Runner.run("int", ITERATIONS, generateInt());
//        Runner.run("boolean", ITERATIONS, generateBoolean());
//        Runner.run("double", ITERATIONS, generateDouble());
//        Runner.run("long", ITERATIONS, generateLong());
//        Runner.run("float", ITERATIONS, generateFloat());
    }

    private static void sandbox3() {
        Generator<String> g = generateInt(1, 65537)
                .flatMap(i -> generateLong(i, i + 1000))
                .flatMap(l -> generateString(l.intValue() & 31,
                        generateStringFromCharacters(Characters.alphaLower())));

        Runner.runMark3("sandbox3", 100000, g);
        Runner.run("sandbox3", 100000, g);
        Runner.runTraced("sandbox3", 100000, g);
    }

    public static void main(String[] args) {
        runTuples();
        sandbox3();
    }
}
