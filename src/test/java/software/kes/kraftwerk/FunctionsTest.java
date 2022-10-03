package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import org.junit.jupiter.api.Test;
import software.kes.enhancediterables.ImmutableFiniteIterable;
import software.kes.kraftwerk.constraints.IntRange;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static software.kes.kraftwerk.Generators.generateFn1;
import static software.kes.kraftwerk.Generators.generateFn2;
import static software.kes.kraftwerk.Generators.generateFn3;
import static software.kes.kraftwerk.Generators.generateFn4;
import static software.kes.kraftwerk.Generators.generateFn5;
import static software.kes.kraftwerk.Generators.generateFn6;
import static software.kes.kraftwerk.Generators.generateFn7;
import static software.kes.kraftwerk.Generators.generateFn8;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateIntRange;
import static software.kes.kraftwerk.Generators.generateTuple;
import static testsupport.Assert.assertForAll;

class FunctionsTest {
    private static final int NUM_CALLS = 10;

    @Test
    void fn1Generator() {
        Generator<Tuple2<Fn1<Integer, Integer>, IntRange>> generateTestCase = generateIntRange()
                .flatMap(range ->
                        generateFn1(Cogenerator.cogeneratorInt(), generateInt(range))
                                .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn1<Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Integer> args = generateInt().run().take(NUM_CALLS);

            // Test that all results are in range, and that the function is referentially transparent
            return all(x -> range.includes(f.apply(x)), args)
                    && all(x -> f.apply(x).equals(f.apply(x)), args);
        });
    }

    @Test
    void fn2Generator() {
        Generator<Tuple2<Fn2<Integer, Integer, Integer>, IntRange>> generateTestCase = generateIntRange()
                .flatMap(range ->
                        generateFn2(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), generateInt(range))
                                .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn2<Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple2<Integer, Integer>> args = generateInt().pair()
                    .run().take(NUM_CALLS);

            Fn1<Tuple2<Integer, Integer>, Integer> call = x -> f.apply(x._1(), x._2());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }

    @Test
    void fn3Generator() {
        Generator<Tuple2<Fn3<Integer, Integer, Integer, Integer>, IntRange>> generateTestCase = generateIntRange()
                .flatMap(range ->
                        generateFn3(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), generateInt(range))
                                .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn3<Integer, Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple3<Integer, Integer, Integer>> args = generateInt().triple()
                    .run().take(NUM_CALLS);

            Fn1<Tuple3<Integer, Integer, Integer>, Integer> call = x -> f.apply(x._1(), x._2(), x._3());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }

    @Test
    void fn4Generator() {
        Generator<Tuple2<Fn4<Integer, Integer, Integer, Integer, Integer>, IntRange>> generateTestCase = generateIntRange()
                .flatMap(range ->
                        generateFn4(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), generateInt(range))
                                .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn4<Integer, Integer, Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple4<Integer, Integer, Integer, Integer>> args =
                    generateTuple(generateInt(), generateInt(), generateInt(), generateInt())
                            .run().take(NUM_CALLS);

            Fn1<Tuple4<Integer, Integer, Integer, Integer>, Integer> call = x -> f.apply(x._1(), x._2(), x._3(), x._4());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }

    @Test
    void fn5Generator() {
        Generator<Tuple2<Fn5<Integer, Integer, Integer, Integer, Integer, Integer>, IntRange>> generateTestCase = generateIntRange()
                .flatMap(range ->
                        generateFn5(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(),
                                generateInt(range))
                                .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn5<Integer, Integer, Integer, Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple5<Integer, Integer, Integer, Integer, Integer>> args =
                    generateTuple(generateInt(), generateInt(), generateInt(), generateInt(), generateInt())
                            .run().take(NUM_CALLS);

            Fn1<Tuple5<Integer, Integer, Integer, Integer, Integer>, Integer> call = x -> f.apply(x._1(), x._2(), x._3(), x._4(), x._5());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }

    @Test
    void fn6Generator() {
        Generator<Tuple2<Fn6<Integer, Integer, Integer, Integer, Integer, Integer, Integer>, IntRange>> generateTestCase = generateIntRange()
                .flatMap(range ->
                        generateFn6(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(),
                                Cogenerator.cogeneratorInt(), generateInt(range))
                                .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>> args =
                    generateTuple(generateInt(), generateInt(), generateInt(), generateInt(), generateInt(), generateInt())
                            .run().take(NUM_CALLS);

            Fn1<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>, Integer> call =
                    x -> f.apply(x._1(), x._2(), x._3(), x._4(), x._5(), x._6());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }

    @Test
    void fn7Generator() {
        Generator<Tuple2<Fn7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, IntRange>> generateTestCase =
                generateIntRange()
                        .flatMap(range ->
                                generateFn7(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(),
                                        Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), generateInt(range))
                                        .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>> args =
                    generateTuple(generateInt(), generateInt(), generateInt(), generateInt(), generateInt(), generateInt(),
                            generateInt())
                            .run().take(NUM_CALLS);

            Fn1<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> call =
                    x -> f.apply(x._1(), x._2(), x._3(), x._4(), x._5(), x._6(), x._7());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }

    @Test
    void fn8Generator() {
        Generator<Tuple2<Fn8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, IntRange>> generateTestCase =
                generateIntRange()
                        .flatMap(range ->
                                generateFn8(Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(),
                                        Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), Cogenerator.cogeneratorInt(), generateInt(range))
                                        .fmap(fnGenerator -> tuple(fnGenerator, range)));

        assertForAll(generateTestCase, t -> {
            Fn8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = t._1();
            IntRange range = t._2();
            ImmutableFiniteIterable<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> args =
                    generateTuple(generateInt(), generateInt(), generateInt(), generateInt(), generateInt(), generateInt(),
                            generateInt(), generateInt())
                            .run().take(NUM_CALLS);

            Fn1<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>, Integer> call =
                    x -> f.apply(x._1(), x._2(), x._3(), x._4(), x._5(), x._6(), x._7(), x._8());

            return all(x -> range.includes(call.apply(x)), args)
                    && all(x -> call.apply(x)
                    .equals(call.apply(x)), args);
        });
    }
}
