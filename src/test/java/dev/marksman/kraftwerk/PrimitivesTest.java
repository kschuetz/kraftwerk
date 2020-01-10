package dev.marksman.kraftwerk;

import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testsupport.Assert.assertForAll;
import static testsupport.Sample.sample;

class PrimitivesTest {

    @Test
    void testIntInclusiveInBounds() {
        assertForAll(Generators.generateInt(0, 0), eq(0));
        assertForAll(Generators.generateInt(-256, 256), n -> n >= -256 && n <= 256);
        assertForAll(Generators.generateInt(0, 1), n -> n == 0 || n == 1);
        assertForAll(Generators.generateInt(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(Generators.generateInt(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(Generators.generateInt(Integer.MIN_VALUE, 0), lte(0));
        assertForAll(Generators.generateInt(Integer.MIN_VALUE, -1), lt(0));
        assertForAll(Generators.generateInt(0, Integer.MAX_VALUE), gte(0));
        assertForAll(Generators.generateInt(0, Integer.MAX_VALUE), gte(0));
        assertForAll(Generators.generateInt().flatMap(lowerBound ->
                        Generators.generateInt(lowerBound, Integer.MAX_VALUE)
                                .flatMap(upperBound -> Generators.generateInt(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n <= upperBound))),
                id());

    }

    @Test
    void testIntExclusiveInBounds() {
        assertForAll(Generators.generateIntExclusive(Integer.MAX_VALUE), gte(0));
        assertForAll(Generators.generateIntExclusive(Integer.MAX_VALUE).flatMap(Generators::generateIntExclusive), gte(0));
        assertForAll(Generators.generateInt(Integer.MIN_VALUE, Integer.MAX_VALUE - 1)
                        .flatMap(lowerBound -> Generators.generateInt(lowerBound + 1, Integer.MAX_VALUE)
                                .flatMap(upperBound -> Generators.generateIntExclusive(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n < upperBound))),
                id());
    }

    @Test
    void testIntFullRange() {
        Seed initial = Seed.random();
        assertEquals(sample(Generators.generateInt(Integer.MIN_VALUE, Integer.MAX_VALUE), initial),
                sample(Generators.generateInt(), initial));
    }

    @Test
    void testLongInclusiveInBounds() {
        assertForAll(Generators.generateLong(0L, 0L), eq(0L));
        assertForAll(Generators.generateLong(-256, 256), n -> n >= -256 && n <= 256);
        assertForAll(Generators.generateLong(0, 1), n -> n == 0 || n == 1);
        assertForAll(Generators.generateLong(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(Generators.generateLong(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(Generators.generateLong(Long.MIN_VALUE, 0L), lte(0L));
        assertForAll(Generators.generateLong(Long.MIN_VALUE, -1), lt(0L));
        assertForAll(Generators.generateLong(0, Long.MAX_VALUE), gte(0L));
        assertForAll(Generators.generateLong(0, Long.MAX_VALUE), gte(0L));
        assertForAll(Generators.generateLong().flatMap(lowerBound ->
                        Generators.generateLong(lowerBound, Long.MAX_VALUE)
                                .flatMap(upperBound -> Generators.generateLong(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n <= upperBound))),
                id());

    }

    @Test
    void testLongExclusiveInBounds() {
        assertForAll(Generators.generateLongExclusive(Long.MAX_VALUE), gte(0L));
        assertForAll(Generators.generateLongExclusive(Long.MAX_VALUE).flatMap(Generators::generateLongExclusive), gte(0L));
        assertForAll(Generators.generateLong(Long.MIN_VALUE, Long.MAX_VALUE - 1)
                        .flatMap(lowerBound -> Generators.generateLong(lowerBound + 1, Long.MAX_VALUE)
                                .flatMap(upperBound -> Generators.generateLongExclusive(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n < upperBound))),
                id());
    }

    @Test
    void testLongFullRange() {
        Seed initial = Seed.random();
        assertEquals(sample(Generators.generateLong(Long.MIN_VALUE, Long.MAX_VALUE), initial),
                sample(Generators.generateLong(), initial));
    }

    @Test
    void testDoubleBetween0and1() {
        assertForAll(Generators.generateDouble(), n -> n >= 0.0d && n <= 1.0d);
    }

    @Test
    void testDoubleScaledInBounds() {
        assertForAll(Generators.generateDouble(999.0d), n -> n >= 0.0d && n <= 999.0d);
        assertForAll(Generators.generateDouble(-999.0d), n -> n >= -999.0d && n <= 0.0d);
    }

    @Test
    void testFloatBetween0and1() {
        assertForAll(Generators.generateFloat(), n -> n >= 0.0f && n <= 1.0f);
    }

    @Test
    void testFloatScaledInBounds() {
        assertForAll(Generators.generateFloat(999.0f), n -> n >= 0.0f && n <= 999.0f);
        assertForAll(Generators.generateFloat(-999.0f), n -> n >= -999.0f && n <= 0.0f);
    }

}
