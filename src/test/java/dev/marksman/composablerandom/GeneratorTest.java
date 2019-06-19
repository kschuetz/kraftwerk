package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static dev.marksman.composablerandom.DefaultInterpreter.defaultInterpreter;
import static dev.marksman.composablerandom.Generate.*;
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {

    private static final int SEQUENCE_LENGTH = 17;

    private static final Generate<Integer> gen1 = generateInt();
    private static final Generate<Double> gen2 = generateGaussian();
    private static final Generate<Integer> gen3 = generateIntExclusive(1, 10);
    private static final Generate<String> gen4 = frequency(FrequencyEntry.entryForValue(3, "foo"),
            FrequencyEntry.entryForValue(7, "bar"));

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
        assertTrue(all(eq(1), streamFrom(Generate.constant(1)).next(1000)));
    }

    private static <A> void testFunctorIdentity(Generate<A> gen) {
        Generate<A> generator2 = gen.fmap(id());
        testEquivalent(gen, generator2);
    }

    private static <A> void testFunctorComposition(Generate<A> gen) {
        Fn1<A, Tuple2<A, A>> f = a -> tuple(a, a);
        Fn1<Tuple2<A, A>, Tuple3<A, A, A>> g = t -> t.cons(t._1());
        testEquivalent(gen.fmap(f).fmap(g), gen.fmap(f.fmap(g)));
    }

    private static <A> void testMonadLeftIdentity(A someValue, Generate<A> gen) {
        Fn1<A, Generate<A>> fn = Id.<A>id().fmap(gen::pure);

        Generate<A> generator1 = fn.apply(someValue);
        Generate<A> generator2 = gen.pure(someValue).flatMap(fn);

        testEquivalent(generator1, generator2);
    }

    private static <A> void testMonadRightIdentity(Generate<A> gen) {
        Generate<A> generator2 = gen.flatMap(gen::pure);
        testEquivalent(gen, generator2);
    }

    private static <A> void testEquivalent(Generate<A> gen1, Generate<A> gen2) {
        RandomState initial = initStandardGen();

        Result<RandomState, ArrayList<A>> result1 = run(generateArrayListOfN(SEQUENCE_LENGTH, gen1), initial);
        Result<RandomState, ArrayList<A>> result2 = run(generateArrayListOfN(SEQUENCE_LENGTH, gen2), initial);

        assertEquals(result1.getNextState(),
                result2.getNextState(), "outbound RandomGens don't match");
        assertEquals(result1.getValue(), result2.getValue(), "values don't match");
    }

    @SuppressWarnings("unchecked")
    private static <A> Result<RandomState, A> run(Generate<A> gen, RandomState input) {
        Generator<A> compiled = defaultInterpreter().compile(defaultParameters(), gen);
        return (Result<RandomState, A>) compiled.run(input);
    }

}
