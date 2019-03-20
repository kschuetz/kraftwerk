package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.product.Product2;
import dev.marksman.composablerandom.random.StandardGen;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StandardGenTest {

    private static final long INITIAL_SEED = 0x123456789abcdefL;
    private static final int SEQUENCE_LENGTH = 16;

    @Test
    void nextInt() {
        testAgainstUtilRandom(Random::nextInt, RandomState::nextInt);
    }

    @Test
    void nextIntWithBound() {
        testAgainstUtilRandom(r -> r.nextInt(1), r -> r.nextInt(1));
        testAgainstUtilRandom(r -> r.nextInt(255), r -> r.nextInt(255));
        testAgainstUtilRandom(r -> r.nextInt(256), r -> r.nextInt(256));
        testAgainstUtilRandom(r -> r.nextInt(Integer.MAX_VALUE), r -> r.nextInt(Integer.MAX_VALUE));
    }

    @Test
    void nextIntWithInvalidBound() {
        RandomState randomState = initStandardGen();
        assertThrows(IllegalArgumentException.class, () -> randomState.nextInt(0));
        assertThrows(IllegalArgumentException.class, () -> randomState.nextInt(-1));
    }

    @Test
    void nextDouble() {
        testAgainstUtilRandom(Random::nextDouble, RandomState::nextDouble);
    }

    @Test
    void nextFloat() {
        testAgainstUtilRandom(Random::nextFloat, RandomState::nextFloat);
    }

    @Test
    void nextLong() {
        testAgainstUtilRandom(Random::nextLong, RandomState::nextLong);
    }

    @Test
    void nextBoolean() {
        testAgainstUtilRandom(Random::nextBoolean, RandomState::nextBoolean);
    }

    @Test
    void nextGaussian() {
        testAgainstUtilRandom(Random::nextGaussian, RandomState::nextGaussian);
    }

    @Test
    void nextBytes() {
        testNextBytes(initRandom(), initStandardGen(), 1);
        testNextBytes(initRandom(), initStandardGen(), 2);
        testNextBytes(initRandom(), initStandardGen(), 3);
        testNextBytes(initRandom(), initStandardGen(), 4);
        testNextBytes(initRandom(), initStandardGen(), 5);
    }

    @Test
    void mixed() {
        Random random = initRandom();
        RandomState rg = initStandardGen();
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextInt, RandomState::nextInt);
        rg = testAgainstUtilRandom(random, rg, 1, r -> r.nextInt(10), r -> r.nextInt(10));
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextDouble, RandomState::nextDouble);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextFloat, RandomState::nextFloat);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextLong, RandomState::nextLong);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextBoolean, RandomState::nextBoolean);
        testAgainstUtilRandom(random, rg, 1, Random::nextGaussian, RandomState::nextGaussian);
    }

    @Test
    void withCachedGaussian() {
        Random random = initRandom();
        RandomState rg = initStandardGen();
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextGaussian, RandomState::nextGaussian);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextInt, RandomState::nextInt);
        rg = testAgainstUtilRandom(random, rg, 1, r -> r.nextInt(10), r -> r.nextInt(10));
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextDouble, RandomState::nextDouble);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextFloat, RandomState::nextFloat);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextLong, RandomState::nextLong);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextBoolean, RandomState::nextBoolean);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextGaussian, RandomState::nextGaussian);
        testAgainstUtilRandom(random, rg, 1, Random::nextInt, RandomState::nextInt);
    }

    @Test
    void nextBytesWithCachedGaussian() {
        Random random = initRandom();
        random.nextGaussian();
        testNextBytes(random, initStandardGen().nextGaussian()._1(), 4);
    }

    @Test
    void noMethodsMutate() {
        StandardGen randomGen = initStandardGen();
        long seed = randomGen.getSeedValue();

        randomGen.nextInt();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextInt(10);
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextDouble();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextFloat();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextLong();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextBoolean();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextGaussian();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextBytes(new byte[4]);
        assertEquals(seed, randomGen.getSeedValue());
    }

    private <A> void testAgainstUtilRandom(Function<Random, A> getNextExpected,
                                           Function<RandomState, Product2<? extends RandomState, A>> getNextResult) {
        testAgainstUtilRandom(initRandom(), initStandardGen(), SEQUENCE_LENGTH, getNextExpected, getNextResult);
    }

    private <A> RandomState testAgainstUtilRandom(Random random,
                                                  RandomState randomState,
                                                  int times,
                                                  Function<Random, A> getNextExpected,
                                                  Function<RandomState, Product2<? extends RandomState, A>> getNextResult) {
        RandomState current = randomState;
        for (int i = 0; i < times; i++) {
            A expected = getNextExpected.apply(random);
            Product2<? extends RandomState, A> next = getNextResult.apply(current);
            current = next._1();
            A actual = next._2();

            assertEquals(expected, actual, "index " + i);
        }
        return current;
    }

    private static void testNextBytes(Random random, RandomState randomState, int count) {
        byte[] expected = new byte[count];
        byte[] actual = new byte[count];

        random.nextBytes(expected);
        randomState.nextBytes(actual);

        assertArrayEquals(expected, actual);
    }

    private static Random initRandom() {
        Random result = new Random();
        result.setSeed(StandardGenTest.INITIAL_SEED);
        return result;
    }

    private static StandardGen initStandardGen() {
        return StandardGen.initStandardGen(INITIAL_SEED);
    }

}
