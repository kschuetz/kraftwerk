package dev.marksman.composablerandom;

import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static dev.marksman.composablerandom.Generator.generateInt;
import static dev.marksman.composablerandom.Generator.generateIntExclusive;
import static dev.marksman.composablerandom.Initialize.randomInitialRandomState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testsupport.Sample.sample;

class PrimitivesTest {

    @Test
    void testIntRange() {
        assertTrue(all(eq(0), sample(generateInt(0, 0))));
        assertTrue(all(n -> n >= -256 && n <= 256, sample(generateInt(-256, 256))));
        assertTrue(all(n -> n == 0 || n == 1, sample(generateInt(0, 1))));
        assertTrue(all(n -> n >= -1 && n <= 1, sample(generateInt(-1, 1))));
        assertTrue(all(n -> n >= -1 && n <= 1, sample(generateInt(-1, 1))));
        assertTrue(all(lte(0), sample(generateInt(Integer.MIN_VALUE, 0))));
        assertTrue(all(lt(0), sample(generateInt(Integer.MIN_VALUE, -1))));
        assertTrue(all(gte(0), sample(generateInt(0, Integer.MAX_VALUE))));
        assertTrue(all(gte(0), sample(generateInt(0, Integer.MAX_VALUE))));
    }

    @Test
    void testIntExclusive() {
        assertTrue(all(gte(0), sample(generateIntExclusive(Integer.MAX_VALUE))));
        assertTrue(all(gte(0), sample(generateIntExclusive(Integer.MAX_VALUE)
                .flatMap(Generator::generateIntExclusive))));
    }

    @Test
    void testIntFullRange() {
        RandomState initial = randomInitialRandomState();
        assertEquals(sample(generateInt(Integer.MIN_VALUE, Integer.MAX_VALUE), initial),
                sample(generateInt(), initial));
    }

}
