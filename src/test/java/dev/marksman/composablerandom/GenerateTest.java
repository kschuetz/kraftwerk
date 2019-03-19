package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.composablerandom.FrequencyEntry.frequencyEntry;
import static dev.marksman.composablerandom.Generate.*;
import static dev.marksman.composablerandom.StandardGen.initStandardGen;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerateTest {

    private static int SEQUENCE_LENGTH = 17;

    private static final Generate<Integer> gen1 = generateInt();
    private static final Generate<Double> gen2 = generateGaussian();
    private static final Generate<Integer> gen3 = generateInt(1, 10);
    private static final Generate<String> gen4 = frequency(frequencyEntry(3, "foo"),
            frequencyEntry(7, "bar"));

    @Test
    void functorIdentity() {
        testFunctorIdentity(gen1);
        testFunctorIdentity(gen2);
        testFunctorIdentity(gen3);
        testFunctorIdentity(gen4);
    }

    @Test
    void functorComposition() {
        testFunctorComposition(gen1);
        testFunctorComposition(gen2);
        testFunctorComposition(gen3);
        testFunctorComposition(gen4);
    }

    @Test
    void monadLeftIdentity() {
        testMonadLeftIdentity(1, gen1);
        testMonadLeftIdentity(0.0, gen2);
        testMonadLeftIdentity(1, gen3);
        testMonadLeftIdentity("foo", gen4);
    }

    @Test
    void monadRightIdentity() {
        testMonadRightIdentity(gen1);
        testMonadRightIdentity(gen2);
        testMonadRightIdentity(gen3);
        testMonadRightIdentity(gen4);
    }

    private static <A> void testFunctorIdentity(Generate<A> generate1) {
        Generate<A> generate2 = generate1.fmap(id());
        testEquivalent(generate1, generate2);
    }

    private static <A> void testFunctorComposition(Generate<A> generate) {
        Fn1<A, Tuple2<A, A>> f = a -> tuple(a, a);
        Fn1<Tuple2<A, A>, Tuple3<A, A, A>> g = t -> t.cons(t._1());
        testEquivalent(generate.fmap(f).fmap(g), generate.fmap(f.andThen(g)));
    }

    private static <A> void testMonadLeftIdentity(A someValue, Generate<A> generate) {
        Fn1<A, Generate<A>> fn = Id.<A>id().andThen(generate::pure);

        Generate<A> generate1 = fn.apply(someValue);
        Generate<A> generate2 = generate.pure(someValue).flatMap(fn);

        testEquivalent(generate1, generate2);
    }

    private static <A> void testMonadRightIdentity(Generate<A> generate) {
        Generate<A> generate2 = generate.flatMap(generate::pure);
        testEquivalent(generate, generate2);
    }

    private static <A> void testEquivalent(Generate<A> generate1, Generate<A> generate2) {
        StandardGen initial = initStandardGen();

        Product2<? extends State, ArrayList<A>> result1 = generate1.listOfN(SEQUENCE_LENGTH).run(initial);
        Product2<? extends State, ArrayList<A>> result2 = generate2.listOfN(SEQUENCE_LENGTH).run(initial);

        assertEquals(result1._1(), result2._1(), "outbound RandomGens don't match");
        assertEquals(result1._2(), result2._2(), "values don't match");
    }
}
