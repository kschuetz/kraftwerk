package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.product.Product2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.composablerandom.StandardGen.initStandardGen;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomTest {

    @Test
    void functorIdentity() {
        testFunctorIdentity(Random.randomInt());
        testFunctorIdentity(Random.randomGaussian());
    }
    
    private static <A> void testFunctorIdentity(Random<A> random1) {
        Random<A> random2 = random1.fmap(id());
        testEquivalent(random1, random2, 16);
    }

    private static <A> void testEquivalent(Random<A> random1, Random<A> random2, int sequenceLength) {
        StandardGen initial = initStandardGen();
        Product2<? extends RandomGen, ArrayList<A>> result1 = random1.times(sequenceLength).run(initial);
        Product2<? extends RandomGen, ArrayList<A>> result2 = random2.times(sequenceLength).run(initial);

        assertEquals(result1._1(), result2._1(), "outbound RandomGens don't match");
        assertEquals(result1._2(), result2._2(), "values don't match");
    }
}