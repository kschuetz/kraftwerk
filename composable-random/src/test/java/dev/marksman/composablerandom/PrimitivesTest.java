package dev.marksman.composablerandom;

import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static dev.marksman.composablerandom.Generator.*;
import static dev.marksman.composablerandom.Initialize.randomInitialRandomState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testsupport.Assert.assertForAll;
import static testsupport.Sample.sample;

class PrimitivesTest {

    @Test
    void testIntInclusiveInBounds() {
        assertForAll(generateInt(0, 0), eq(0));
        assertForAll(generateInt(-256, 256), n -> n >= -256 && n <= 256);
        assertForAll(generateInt(0, 1), n -> n == 0 || n == 1);
        assertForAll(generateInt(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(generateInt(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(generateInt(Integer.MIN_VALUE, 0), lte(0));
        assertForAll(generateInt(Integer.MIN_VALUE, -1), lt(0));
        assertForAll(generateInt(0, Integer.MAX_VALUE), gte(0));
        assertForAll(generateInt(0, Integer.MAX_VALUE), gte(0));
        assertForAll(generateInt().flatMap(lowerBound ->
                        generateInt(lowerBound, Integer.MAX_VALUE)
                                .flatMap(upperBound -> generateInt(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n <= upperBound))),
                id());

    }

    @Test
    void testIntExclusiveInBounds() {
        assertForAll(generateIntExclusive(Integer.MAX_VALUE), gte(0));
        assertForAll(generateIntExclusive(Integer.MAX_VALUE).flatMap(Generator::generateIntExclusive), gte(0));
        assertForAll(generateInt(Integer.MIN_VALUE, Integer.MAX_VALUE - 1)
                        .flatMap(lowerBound -> generateInt(lowerBound + 1, Integer.MAX_VALUE)
                                .flatMap(upperBound -> generateIntExclusive(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n < upperBound))),
                id());
    }

    @Test
    void testIntFullRange() {
        RandomState initial = randomInitialRandomState();
        assertEquals(sample(generateInt(Integer.MIN_VALUE, Integer.MAX_VALUE), initial),
                sample(generateInt(), initial));
    }

    @Test
    void testLongInclusiveInBounds() {
        assertForAll(generateLong(0L, 0L), eq(0L));
        assertForAll(generateLong(-256, 256), n -> n >= -256 && n <= 256);
        assertForAll(generateLong(0, 1), n -> n == 0 || n == 1);
        assertForAll(generateLong(-1, 1), n -> n >= -1 && n <= 1);
        assertForAll(generateLong(-1, 1), n -> n >= -1 && n <= 1);
        // TODO: infinite loop in next test case
//        assertForAll(generateLong(Long.MIN_VALUE, 0L), lte(0L));
//        assertForAll(generateLong(Long.MIN_VALUE, -1), lt(0L));
//        assertForAll(generateLong(0, Long.MAX_VALUE), gte(0L));
//        assertForAll(generateLong(0, Long.MAX_VALUE), gte(0L));
//        assertForAll(generateLong().flatMap(lowerBound ->
//                        generateLong(lowerBound, Long.MAX_VALUE)
//                                .flatMap(upperBound -> generateLong(lowerBound, upperBound)
//                                        .fmap(n -> n >= lowerBound && n <= upperBound))),
//                id());

    }

    @Test
    void testLongExclusiveInBounds() {
        assertForAll(generateLongExclusive(Long.MAX_VALUE), gte(0L));
        assertForAll(generateLongExclusive(Long.MAX_VALUE).flatMap(Generator::generateLongExclusive), gte(0L));
        assertForAll(generateLong(Long.MIN_VALUE, Long.MAX_VALUE - 1)
                        .flatMap(lowerBound -> generateLong(lowerBound + 1, Long.MAX_VALUE)
                                .flatMap(upperBound -> generateLongExclusive(lowerBound, upperBound)
                                        .fmap(n -> n >= lowerBound && n < upperBound))),
                id());
    }

    @Test
    void testLongFullRange() {
        RandomState initial = randomInitialRandomState();
        assertEquals(sample(generateLong(Long.MIN_VALUE, Long.MAX_VALUE), initial),
                sample(generateLong(), initial));
    }

    @Test
    void testDoubleBetween0and1() {
        assertForAll(generateDouble(), n -> n >= 0.0d && n <= 1.0d);
    }

    @Test
    void testDoubleScaledInBounds() {
        assertForAll(generateDouble(999.0d), n -> n >= 0.0d && n <= 999.0d);
        assertForAll(generateDouble(-999.0d), n -> n >= -999.0d && n <= 0.0d);
    }

    @Test
    void testFloatBetween0and1() {
        assertForAll(generateFloat(), n -> n >= 0.0f && n <= 1.0f);
    }

    @Test
    void testFloatScaledInBounds() {
        assertForAll(generateFloat(999.0f), n -> n >= 0.0f && n <= 999.0f);
        assertForAll(generateFloat(-999.0f), n -> n >= -999.0f && n <= 0.0f);
    }

}
