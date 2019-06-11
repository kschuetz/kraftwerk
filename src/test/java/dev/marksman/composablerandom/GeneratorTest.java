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
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Generator.*;
import static dev.marksman.composablerandom.StandardParameters.defaultParameters;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {

    private static final int SEQUENCE_LENGTH = 17;

    private static final Generator<Integer> gen1 = generateInt();
    private static final Generator<Double> gen2 = generateGaussian();
    private static final Generator<Integer> gen3 = generateIntExclusive(1, 10);
    private static final Generator<String> gen4 = frequency(FrequencyEntry.entryForValue(3, "foo"),
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
        assertTrue(all(eq(1), streamFrom(Generator.constant(1)).next(1000)));
    }

    private static <A> void testFunctorIdentity(Generator<A> generator1) {
        Generator<A> generator2 = generator1.fmap(id());
        testEquivalent(generator1, generator2);
    }

    private static <A> void testFunctorComposition(Generator<A> generator) {
        Fn1<A, Tuple2<A, A>> f = a -> tuple(a, a);
        Fn1<Tuple2<A, A>, Tuple3<A, A, A>> g = t -> t.cons(t._1());
        testEquivalent(generator.fmap(f).fmap(g), generator.fmap(f.fmap(g)));
    }

    private static <A> void testMonadLeftIdentity(A someValue, Generator<A> generator) {
        Fn1<A, Generator<A>> fn = Id.<A>id().fmap(generator::pure);

        Generator<A> generator1 = fn.apply(someValue);
        Generator<A> generator2 = generator.pure(someValue).flatMap(fn);

        testEquivalent(generator1, generator2);
    }

    private static <A> void testMonadRightIdentity(Generator<A> generator) {
        Generator<A> generator2 = generator.flatMap(generator::pure);
        testEquivalent(generator, generator2);
    }

    private static <A> void testEquivalent(Generator<A> generator1, Generator<A> generator2) {
        RandomState initial = initStandardGen();

        Result<RandomState, ArrayList<A>> result1 = run(generateArrayListOfN(SEQUENCE_LENGTH, generator1), initial);
        Result<RandomState, ArrayList<A>> result2 = run(generateArrayListOfN(SEQUENCE_LENGTH, generator2), initial);

        assertEquals(result1.getNextState(),
                result2.getNextState(), "outbound RandomGens don't match");
        assertEquals(result1.getValue(), result2.getValue(), "values don't match");
    }

    @SuppressWarnings("unchecked")
    private static <A> Result<RandomState, A> run(Generator<A> generator, RandomState input) {
        CompiledGenerator<A> compiled = defaultInterpreter().compile(defaultParameters(), generator);
        return (Result<RandomState, A>) compiled.run(input);
    }

}
