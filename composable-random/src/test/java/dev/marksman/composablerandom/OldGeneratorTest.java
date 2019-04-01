package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import dev.marksman.composablerandom.legacy.State;
import dev.marksman.composablerandom.legacy.builtin.OldGenerators;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static dev.marksman.composablerandom.legacy.OldFrequencyEntry.entry;
import static dev.marksman.composablerandom.legacy.OldGeneratedStream.streamFrom;
import static dev.marksman.composablerandom.legacy.builtin.OldGenerators.*;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OldGeneratorTest {

    private static int SEQUENCE_LENGTH = 17;

    private static final OldGenerator<Integer> gen1 = OldGenerators.generateInt();
    private static final OldGenerator<Double> gen2 = generateGaussian();
    private static final OldGenerator<Integer> gen3 = OldGenerators.generateIntExclusive(1, 10);
    private static final OldGenerator<String> gen4 = frequency(entry(3, "foo"),
            entry(7, "bar"));

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

    @Test
    void generateConstant() {
        assertTrue(all(eq(1), streamFrom(OldGenerator.constant(1)).next(1000)));
    }

    private static <A> void testFunctorIdentity(OldGenerator<A> generator1) {
        OldGenerator<A> generator2 = generator1.fmap(id());
        testEquivalent(generator1, generator2);
    }

    private static <A> void testFunctorComposition(OldGenerator<A> generator) {
        Fn1<A, Tuple2<A, A>> f = a -> tuple(a, a);
        Fn1<Tuple2<A, A>, Tuple3<A, A, A>> g = t -> t.cons(t._1());
        testEquivalent(generator.fmap(f).fmap(g), generator.fmap(f.andThen(g)));
    }

    private static <A> void testMonadLeftIdentity(A someValue, OldGenerator<A> generator) {
        Fn1<A, OldGenerator<A>> fn = Id.<A>id().andThen(generator::pure);

        OldGenerator<A> generator1 = fn.apply(someValue);
        OldGenerator<A> generator2 = generator.pure(someValue).flatMap(fn);

        testEquivalent(generator1, generator2);
    }

    private static <A> void testMonadRightIdentity(OldGenerator<A> generator) {
        OldGenerator<A> generator2 = generator.flatMap(generator::pure);
        testEquivalent(generator, generator2);
    }

    private static <A> void testEquivalent(OldGenerator<A> generator1, OldGenerator<A> generator2) {
        State initial = State.state(initStandardGen());

        Result<State, ArrayList<A>> result1 = generateListOfN(SEQUENCE_LENGTH, generator1).run(initial);
        Result<State, ArrayList<A>> result2 = generateListOfN(SEQUENCE_LENGTH, generator2).run(initial);

        assertEquals(result1.getNextState().getRandomState(),
                result2.getNextState().getRandomState(), "outbound RandomGens don't match");
        assertEquals(result1.getValue(), result2.getValue(), "values don't match");
    }
}
