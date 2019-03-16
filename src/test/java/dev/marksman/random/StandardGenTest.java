package dev.marksman.random;

import com.jnape.palatable.lambda.adt.product.Product2;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardGenTest {

    private static final long INITIAL_SEED = 0x123456789abcdefL;
    private static final int SEQUENCE_LENGTH = 16;

    @Test
    void nextInt() {
        testAgainstUtilRandom(Random::nextInt, RandomGen::nextInt);
    }

    @Test
    void nextIntWithBound() {
        testAgainstUtilRandom(r -> r.nextInt(1), r -> r.nextInt(1));
        testAgainstUtilRandom(r -> r.nextInt(255), r -> r.nextInt(255));
        testAgainstUtilRandom(r -> r.nextInt(256), r -> r.nextInt(256));
        testAgainstUtilRandom(r -> r.nextInt(Integer.MAX_VALUE), r -> r.nextInt(Integer.MAX_VALUE));
    }

    @Test
    void nextDouble() {
        testAgainstUtilRandom(Random::nextDouble, RandomGen::nextDouble);
    }

    @Test
    void nextFloat() {
        testAgainstUtilRandom(Random::nextFloat, RandomGen::nextFloat);
    }

    @Test
    void nextLong() {
        testAgainstUtilRandom(Random::nextLong, RandomGen::nextLong);
    }

    @Test
    void nextBoolean() {
        testAgainstUtilRandom(Random::nextBoolean, RandomGen::nextBoolean);
    }

    @Test
    void nextGaussian() {
        testAgainstUtilRandom(Random::nextGaussian, RandomGen::nextGaussian);
    }

    @Test
    void nextBytes() {
        Random random = initRandom();
        RandomGen current = initStandardGen();

        byte[] expected = new byte[SEQUENCE_LENGTH];
        byte[] actual = new byte[SEQUENCE_LENGTH];

        random.nextBytes(expected);
        current.nextBytes(actual);
        
        assertArrayEquals(expected, actual);
    }

    private <A> void testAgainstUtilRandom(Function<Random, A> getNextExpected,
                                           Function<RandomGen, Product2<A, ? extends RandomGen>> getNextResult) {
        Random random = initRandom();
        RandomGen current = initStandardGen();

        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
            A expected = getNextExpected.apply(random);
            Product2<A, ? extends RandomGen> next = getNextResult.apply(current);
            A actual = next._1();
            current = next._2();

            assertEquals(expected, actual, "index " + i);
        }
    }

    private static Random initRandom() {
        Random result = new Random();
        result.setSeed(StandardGenTest.INITIAL_SEED);
        return result;
    }

    private static StandardGen initStandardGen() {
        return StandardGen.initStandardGen(INITIAL_SEED);
    }

    // TODO:
    // mixing types in sequences
    // int with bound 0
    // mixing gaussian / non-gaussian
    // immutability
    // associativity

}
