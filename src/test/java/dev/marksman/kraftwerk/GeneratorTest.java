package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static dev.marksman.kraftwerk.StandardGeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.kraftwerk.core.StandardSeed.initStandardSeed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {

    private static final int SEQUENCE_LENGTH = 17;

    private static final Generator<Integer> gen1 = Generators.generateInt();
    private static final Generator<Double> gen2 = Generators.generateGaussian();
    private static final Generator<Integer> gen3 = Generators.generateIntExclusive(1, 10);
    private static final Generator<String> gen4 = Generators.frequency(FrequencyEntry.entryForValue(3, "foo"),
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
        assertTrue(all(eq(1), Generators.constant(1).run().take(1000)));
    }

    @Disabled
    @Test
    void stackSafeFlatMap() {
        int LARGE_NUMBER = 10_000;
        int max = 2 * LARGE_NUMBER;
        Generator<Integer> g = Generators.generateInt(0, max);
        for (int i = 0; i < LARGE_NUMBER; i++) {
            g = g.flatMap(n -> Generators.generateInt(n, max));
        }
        g.run().iterator().next();
    }

    @Test
    void stackSafeFmap() {
        int LARGE_NUMBER = 10_000;
        int max = 2 * LARGE_NUMBER;
        Generator<Integer> g = Generators.generateInt(0, max);
        for (int i = 0; i < LARGE_NUMBER; i++) {
            g = g.fmap(n -> n + 1);
        }
        g.run().iterator().next();
    }

    private static <A> void testFunctorIdentity(Generator<A> gen) {
        Generator<A> generator2 = gen.fmap(id());
        testEquivalent(gen, generator2);
    }

    private static <A> void testFunctorComposition(Generator<A> gen) {
        Fn1<A, Tuple2<A, A>> f = a -> tuple(a, a);
        Fn1<Tuple2<A, A>, Tuple3<A, A, A>> g = t -> t.cons(t._1());
        testEquivalent(gen.fmap(f).fmap(g), gen.fmap(f.fmap(g)));
    }

    private static <A> void testMonadLeftIdentity(A someValue, Generator<A> gen) {
        Fn1<A, Generator<A>> fn = Id.<A>id().fmap(gen::pure);

        Generator<A> generator1 = fn.apply(someValue);
        Generator<A> generator2 = gen.pure(someValue).flatMap(fn);

        testEquivalent(generator1, generator2);
    }

    private static <A> void testMonadRightIdentity(Generator<A> gen) {
        Generator<A> generator2 = gen.flatMap(gen::pure);
        testEquivalent(gen, generator2);
    }

    private static <A> void testEquivalent(Generator<A> gen1, Generator<A> gen2) {
        Seed initial = initStandardSeed();

        Result<Seed, ArrayList<A>> result1 = run(Generators.generateArrayListOfN(SEQUENCE_LENGTH, gen1), initial);
        Result<Seed, ArrayList<A>> result2 = run(Generators.generateArrayListOfN(SEQUENCE_LENGTH, gen2), initial);

        assertEquals(result1.getNextState(),
                result2.getNextState(), "outbound RandomGens don't match");
        assertEquals(result1.getValue(), result2.getValue(), "values don't match");
    }

    @SuppressWarnings("unchecked")
    private static <A> Result<Seed, A> run(Generator<A> gen, Seed input) {
        return (Result<Seed, A>) gen.prepare(defaultGeneratorParameters()).apply(input);
    }

}
