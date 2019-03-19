package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.product.Product2;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StandardGenTest {

    private static final long INITIAL_SEED = 0x123456789abcdefL;
    private static final int SEQUENCE_LENGTH = 16;

    @Test
    void nextInt() {
        testAgainstUtilRandom(Random::nextInt, EntropySource::nextInt);
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
        EntropySource entropySource = initStandardGen();
        assertThrows(IllegalArgumentException.class, () -> entropySource.nextInt(0));
        assertThrows(IllegalArgumentException.class, () -> entropySource.nextInt(-1));
    }

    @Test
    void nextDouble() {
        testAgainstUtilRandom(Random::nextDouble, EntropySource::nextDouble);
    }

    @Test
    void nextFloat() {
        testAgainstUtilRandom(Random::nextFloat, EntropySource::nextFloat);
    }

    @Test
    void nextLong() {
        testAgainstUtilRandom(Random::nextLong, EntropySource::nextLong);
    }

    @Test
    void nextBoolean() {
        testAgainstUtilRandom(Random::nextBoolean, EntropySource::nextBoolean);
    }

    @Test
    void nextGaussian() {
        testAgainstUtilRandom(Random::nextGaussian, EntropySource::nextGaussian);
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
        EntropySource rg = initStandardGen();
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextInt, EntropySource::nextInt);
        rg = testAgainstUtilRandom(random, rg, 1, r -> r.nextInt(10), r -> r.nextInt(10));
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextDouble, EntropySource::nextDouble);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextFloat, EntropySource::nextFloat);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextLong, EntropySource::nextLong);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextBoolean, EntropySource::nextBoolean);
        testAgainstUtilRandom(random, rg, 1, Random::nextGaussian, EntropySource::nextGaussian);
    }

    @Test
    void withCachedGaussian() {
        Random random = initRandom();
        EntropySource rg = initStandardGen();
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextGaussian, EntropySource::nextGaussian);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextInt, EntropySource::nextInt);
        rg = testAgainstUtilRandom(random, rg, 1, r -> r.nextInt(10), r -> r.nextInt(10));
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextDouble, EntropySource::nextDouble);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextFloat, EntropySource::nextFloat);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextLong, EntropySource::nextLong);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextBoolean, EntropySource::nextBoolean);
        rg = testAgainstUtilRandom(random, rg, 1, Random::nextGaussian, EntropySource::nextGaussian);
        testAgainstUtilRandom(random, rg, 1, Random::nextInt, EntropySource::nextInt);
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
                                           Function<EntropySource, Product2<? extends EntropySource, A>> getNextResult) {
        testAgainstUtilRandom(initRandom(), initStandardGen(), SEQUENCE_LENGTH, getNextExpected, getNextResult);
    }

    private <A> EntropySource testAgainstUtilRandom(Random random,
                                                    EntropySource entropySource,
                                                    int times,
                                                    Function<Random, A> getNextExpected,
                                                    Function<EntropySource, Product2<? extends EntropySource, A>> getNextResult) {
        EntropySource current = entropySource;
        for (int i = 0; i < times; i++) {
            A expected = getNextExpected.apply(random);
            Product2<? extends EntropySource, A> next = getNextResult.apply(current);
            current = next._1();
            A actual = next._2();

            assertEquals(expected, actual, "index " + i);
        }
        return current;
    }

    private static void testNextBytes(Random random, EntropySource entropySource, int count) {
        byte[] expected = new byte[count];
        byte[] actual = new byte[count];

        random.nextBytes(expected);
        entropySource.nextBytes(actual);

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
