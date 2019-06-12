package dev.marksman.composablerandom.random;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.RandomState;
import org.junit.jupiter.api.Test;
import testsupport.GeneratorPair;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static testsupport.GeneratorPair.newRandomGeneratorPair;

class StandardGenTest {

    private static final int SEQUENCE_LENGTH = 32;

    @Test
    void nextInt() {
        testAgainstUtilRandom(Random::nextInt, RandomState::nextInt);
    }

    @Test
    void nextIntWithBound() {
        testAgainstUtilRandom(r -> r.nextInt(1), r -> r.nextIntBounded(1));
        testAgainstUtilRandom(r -> r.nextInt(255), r -> r.nextIntBounded(255));
        testAgainstUtilRandom(r -> r.nextInt(256), r -> r.nextIntBounded(256));
        testAgainstUtilRandom(r -> r.nextInt(Integer.MAX_VALUE), r -> r.nextIntBounded(Integer.MAX_VALUE));
    }

    @Test
    void nextIntWithInvalidBound() {
        GeneratorPair gp = newRandomGeneratorPair();
        assertThrows(IllegalArgumentException.class, () -> gp.getRandomState().nextIntBounded(0));
        assertThrows(IllegalArgumentException.class, () -> gp.getRandomState().nextIntBounded(-1));
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
        testNextBytes(newRandomGeneratorPair(), 1);
        testNextBytes(newRandomGeneratorPair(), 2);
        testNextBytes(newRandomGeneratorPair(), 3);
        testNextBytes(newRandomGeneratorPair(), 4);
        testNextBytes(newRandomGeneratorPair(), 5);
    }

    @Test
    void mixed() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp = testAgainstUtilRandom(gp, 1, Random::nextInt, RandomState::nextInt);
        gp = testAgainstUtilRandom(gp, 1, r -> r.nextInt(10), r -> r.nextIntBounded(10));
        gp = testAgainstUtilRandom(gp, 1, Random::nextDouble, RandomState::nextDouble);
        gp = testAgainstUtilRandom(gp, 1, Random::nextFloat, RandomState::nextFloat);
        gp = testAgainstUtilRandom(gp, 1, Random::nextLong, RandomState::nextLong);
        gp = testAgainstUtilRandom(gp, 1, Random::nextBoolean, RandomState::nextBoolean);
        testAgainstUtilRandom(gp, 1, Random::nextGaussian, RandomState::nextGaussian);
    }

    @Test
    void withCachedGaussian() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp = testAgainstUtilRandom(gp, 1, Random::nextGaussian, RandomState::nextGaussian);
        gp = testAgainstUtilRandom(gp, 1, Random::nextInt, RandomState::nextInt);
        gp = testAgainstUtilRandom(gp, 1, r -> r.nextInt(10), r -> r.nextIntBounded(10));
        gp = testAgainstUtilRandom(gp, 1, Random::nextDouble, RandomState::nextDouble);
        gp = testAgainstUtilRandom(gp, 1, Random::nextFloat, RandomState::nextFloat);
        gp = testAgainstUtilRandom(gp, 1, Random::nextLong, RandomState::nextLong);
        gp = testAgainstUtilRandom(gp, 1, Random::nextBoolean, RandomState::nextBoolean);
        gp = testAgainstUtilRandom(gp, 1, Random::nextGaussian, RandomState::nextGaussian);
        testAgainstUtilRandom(gp, 1, Random::nextInt, RandomState::nextInt);
    }

    @Test
    void nextBytesWithCachedGaussian() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp.getRandom().nextGaussian();
        testNextBytes(gp.updateRandomState(r -> r.nextGaussian()._1()), 4);
    }

    @Test
    void noMethodsMutate() {
        StandardGen randomGen = StandardGen.initStandardGen();
        long seed = randomGen.getSeedValue();

        randomGen.nextInt();
        assertEquals(seed, randomGen.getSeedValue());

        randomGen.nextIntBounded(10);
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

    private <A> void testAgainstUtilRandom(Fn1<Random, A> getNextExpected,
                                           Fn1<RandomState, Product2<? extends RandomState, A>> getNextResult) {
        GeneratorPair gp = newRandomGeneratorPair();
        testAgainstUtilRandom(gp, SEQUENCE_LENGTH, getNextExpected, getNextResult);
    }

    private <A> GeneratorPair testAgainstUtilRandom(GeneratorPair gp,
                                                    int times,
                                                    Fn1<Random, A> getNextExpected,
                                                    Fn1<RandomState, Product2<? extends RandomState, A>> getNextResult) {
        RandomState current = gp.getRandomState();
        Random random = gp.getRandom();
        for (int i = 0; i < times; i++) {
            A expected = getNextExpected.apply(random);
            Product2<? extends RandomState, A> next = getNextResult.apply(current);
            current = next._1();
            A actual = next._2();

            assertEquals(expected, actual, "index " + i + ", " + gp.info());
        }
        return gp.withRandomState(current);
    }

    private static void testNextBytes(GeneratorPair gp, int count) {
        byte[] expected = new byte[count];
        byte[] actual = new byte[count];

        gp.getRandom().nextBytes(expected);
        gp.getRandomState().nextBytes(actual);

        assertArrayEquals(expected, actual, gp.info());
    }

}
