package dev.marksman.composablerandom.random;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.composablerandom.LegacySeed;
import org.junit.jupiter.api.Test;
import testsupport.GeneratorPair;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static testsupport.GeneratorPair.newRandomGeneratorPair;

class StandardGenTest {

    private static final int SEQUENCE_LENGTH = 32;

    @Test
    void nextInt() {
        testAgainstUtilRandom(Random::nextInt, LegacySeed::nextInt);
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
        assertThrows(IllegalArgumentException.class, () -> gp.getSeed().nextIntBounded(0));
        assertThrows(IllegalArgumentException.class, () -> gp.getSeed().nextIntBounded(-1));
    }

    @Test
    void nextDouble() {
        testAgainstUtilRandom(Random::nextDouble, LegacySeed::nextDouble);
    }

    @Test
    void nextFloat() {
        testAgainstUtilRandom(Random::nextFloat, LegacySeed::nextFloat);
    }

    @Test
    void nextLong() {
        testAgainstUtilRandom(Random::nextLong, LegacySeed::nextLong);
    }

    @Test
    void nextBoolean() {
        testAgainstUtilRandom(Random::nextBoolean, LegacySeed::nextBoolean);
    }

    @Test
    void nextGaussian() {
        testAgainstUtilRandom(Random::nextGaussian, LegacySeed::nextGaussian);
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
        gp = testAgainstUtilRandom(gp, 1, Random::nextInt, LegacySeed::nextInt);
        gp = testAgainstUtilRandom(gp, 1, r -> r.nextInt(10), r -> r.nextIntBounded(10));
        gp = testAgainstUtilRandom(gp, 1, Random::nextDouble, LegacySeed::nextDouble);
        gp = testAgainstUtilRandom(gp, 1, Random::nextFloat, LegacySeed::nextFloat);
        gp = testAgainstUtilRandom(gp, 1, Random::nextLong, LegacySeed::nextLong);
        gp = testAgainstUtilRandom(gp, 1, Random::nextBoolean, LegacySeed::nextBoolean);
        testAgainstUtilRandom(gp, 1, Random::nextGaussian, LegacySeed::nextGaussian);
    }

    @Test
    void withCachedGaussian() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp = testAgainstUtilRandom(gp, 1, Random::nextGaussian, LegacySeed::nextGaussian);
        gp = testAgainstUtilRandom(gp, 1, Random::nextInt, LegacySeed::nextInt);
        gp = testAgainstUtilRandom(gp, 1, r -> r.nextInt(10), r -> r.nextIntBounded(10));
        gp = testAgainstUtilRandom(gp, 1, Random::nextDouble, LegacySeed::nextDouble);
        gp = testAgainstUtilRandom(gp, 1, Random::nextFloat, LegacySeed::nextFloat);
        gp = testAgainstUtilRandom(gp, 1, Random::nextLong, LegacySeed::nextLong);
        gp = testAgainstUtilRandom(gp, 1, Random::nextBoolean, LegacySeed::nextBoolean);
        gp = testAgainstUtilRandom(gp, 1, Random::nextGaussian, LegacySeed::nextGaussian);
        testAgainstUtilRandom(gp, 1, Random::nextInt, LegacySeed::nextInt);
    }

    @Test
    void nextBytesWithCachedGaussian() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp.getRandom().nextGaussian();
        testNextBytes(gp.updateSeed(r -> r.nextGaussian()._1()), 4);
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
                                           Fn1<LegacySeed, Product2<? extends LegacySeed, A>> getNextResult) {
        GeneratorPair gp = newRandomGeneratorPair();
        testAgainstUtilRandom(gp, SEQUENCE_LENGTH, getNextExpected, getNextResult);
    }

    private <A> GeneratorPair testAgainstUtilRandom(GeneratorPair gp,
                                                    int times,
                                                    Fn1<Random, A> getNextExpected,
                                                    Fn1<LegacySeed, Product2<? extends LegacySeed, A>> getNextResult) {
        LegacySeed current = gp.getSeed();
        Random random = gp.getRandom();
        for (int i = 0; i < times; i++) {
            A expected = getNextExpected.apply(random);
            Product2<? extends LegacySeed, A> next = getNextResult.apply(current);
            current = next._1();
            A actual = next._2();

            assertEquals(expected, actual, "index " + i + ", " + gp.info());
        }
        return gp.withSeed(current);
    }

    private static void testNextBytes(GeneratorPair gp, int count) {
        byte[] expected = new byte[count];
        byte[] actual = new byte[count];

        gp.getRandom().nextBytes(expected);
        gp.getSeed().nextBytes(actual);

        assertArrayEquals(expected, actual, gp.info());
    }

}
