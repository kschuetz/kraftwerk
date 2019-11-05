package dev.marksman.kraftwerk.random;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Seed;
import dev.marksman.kraftwerk.StandardSeed;
import org.junit.jupiter.api.Test;
import testsupport.GeneratorPair;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static testsupport.GeneratorPair.newRandomGeneratorPair;

class BuildingBlocksTest {

    private static final int SEQUENCE_LENGTH = 32;

    @Test
    void nextInt() {
        testAgainstUtilRandom(Random::nextInt, BuildingBlocks::nextInt);
    }

    @Test
    void nextIntWithBound() {
        testAgainstUtilRandom(r -> r.nextInt(1), r -> BuildingBlocks.nextIntBounded(1, r));
        testAgainstUtilRandom(r -> r.nextInt(255), r -> BuildingBlocks.nextIntBounded(255, r));
        testAgainstUtilRandom(r -> r.nextInt(256), r -> BuildingBlocks.nextIntBounded(256, r));
        testAgainstUtilRandom(r -> r.nextInt(Integer.MAX_VALUE), r -> BuildingBlocks.nextIntBounded(Integer.MAX_VALUE, r));
    }

    @Test
    void nextIntWithInvalidBound() {
        GeneratorPair gp = newRandomGeneratorPair();
        assertThrows(IllegalArgumentException.class, () -> BuildingBlocks.nextIntBounded(0, gp.getSeed()));
        assertThrows(IllegalArgumentException.class, () -> BuildingBlocks.nextIntBounded(-1, gp.getSeed()));
    }

    @Test
    void nextDouble() {
        testAgainstUtilRandom(Random::nextDouble, BuildingBlocks::nextDouble);
    }

    @Test
    void nextFloat() {
        testAgainstUtilRandom(Random::nextFloat, BuildingBlocks::nextFloat);
    }

    @Test
    void nextLong() {
        testAgainstUtilRandom(Random::nextLong, BuildingBlocks::nextLong);
    }

    @Test
    void nextBoolean() {
        testAgainstUtilRandom(Random::nextBoolean, BuildingBlocks::nextBoolean);
    }

    @Test
    void nextGaussian() {
        testAgainstUtilRandom(Random::nextGaussian, BuildingBlocks::nextGaussian);
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
        gp = testAgainstUtilRandom(gp, 1, Random::nextInt, BuildingBlocks::nextInt);
        gp = testAgainstUtilRandom(gp, 1, r -> r.nextInt(10), r -> BuildingBlocks.nextIntBounded(10, r));
        gp = testAgainstUtilRandom(gp, 1, Random::nextDouble, BuildingBlocks::nextDouble);
        gp = testAgainstUtilRandom(gp, 1, Random::nextFloat, BuildingBlocks::nextFloat);
        gp = testAgainstUtilRandom(gp, 1, Random::nextLong, BuildingBlocks::nextLong);
        gp = testAgainstUtilRandom(gp, 1, Random::nextBoolean, BuildingBlocks::nextBoolean);
        testAgainstUtilRandom(gp, 1, Random::nextGaussian, BuildingBlocks::nextGaussian);
    }

    @Test
    void withCachedGaussian() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp = testAgainstUtilRandom(gp, 1, Random::nextGaussian, BuildingBlocks::nextGaussian);
        gp = testAgainstUtilRandom(gp, 1, Random::nextInt, BuildingBlocks::nextInt);
        gp = testAgainstUtilRandom(gp, 1, r -> r.nextInt(10), r -> BuildingBlocks.nextIntBounded(10, r));
        gp = testAgainstUtilRandom(gp, 1, Random::nextDouble, BuildingBlocks::nextDouble);
        gp = testAgainstUtilRandom(gp, 1, Random::nextFloat, BuildingBlocks::nextFloat);
        gp = testAgainstUtilRandom(gp, 1, Random::nextLong, BuildingBlocks::nextLong);
        gp = testAgainstUtilRandom(gp, 1, Random::nextBoolean, BuildingBlocks::nextBoolean);
        gp = testAgainstUtilRandom(gp, 1, Random::nextGaussian, BuildingBlocks::nextGaussian);
        testAgainstUtilRandom(gp, 1, Random::nextInt, BuildingBlocks::nextInt);
    }

    @Test
    void nextBytesWithCachedGaussian() {
        GeneratorPair gp = newRandomGeneratorPair();
        gp.getRandom().nextGaussian();
        testNextBytes(gp.updateSeed(r -> BuildingBlocks.nextGaussian(r)._1()), 4);
    }

    @Test
    void noMethodsMutate() {
        StandardSeed seed = StandardSeed.initStandardSeed();
        long seedValue = seed.getSeedValue();

        BuildingBlocks.nextInt(seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextIntBounded(10, seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextDouble(seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextFloat(seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextLong(seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextBoolean(seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextGaussian(seed);
        assertEquals(seedValue, seed.getSeedValue());

        BuildingBlocks.nextBytes(new byte[4], seed);
        assertEquals(seedValue, seed.getSeedValue());
    }

    private <A> void testAgainstUtilRandom(Fn1<Random, A> getNextExpected,
                                           Fn1<Seed, Product2<? extends Seed, A>> getNextResult) {
        GeneratorPair gp = newRandomGeneratorPair();
        testAgainstUtilRandom(gp, SEQUENCE_LENGTH, getNextExpected, getNextResult);
    }

    private <A> GeneratorPair testAgainstUtilRandom(GeneratorPair gp,
                                                    int times,
                                                    Fn1<Random, A> getNextExpected,
                                                    Fn1<Seed, Product2<? extends Seed, A>> getNextResult) {
        Seed current = gp.getSeed();
        Random random = gp.getRandom();
        for (int i = 0; i < times; i++) {
            A expected = getNextExpected.apply(random);
            Product2<? extends Seed, A> next = getNextResult.apply(current);
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
        BuildingBlocks.nextBytes(actual, gp.getSeed());

        assertArrayEquals(expected, actual, gp.info());
    }

}
