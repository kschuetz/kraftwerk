package software.kes.kraftwerk;

import com.jnape.palatable.lambda.functions.builtin.fn2.Into3;
import org.junit.jupiter.api.Test;
import software.kes.enhancediterables.FiniteIterable;
import software.kes.kraftwerk.constraints.IntRange;

import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateTuple;
import static testsupport.Assert.assertForAll;

class ReservoirSampleTest {
    @Test
    void resultsInRange() {
        Generator<Integer> generateN = generateInt(IntRange.from(0).to(10));
        Generator<Integer> generateK = generateInt(IntRange.from(0).to(10));

        assertForAll(generateTuple(generateN, generateK)
                        .flatMap(t -> ReservoirSample.reservoirSample(t._1(), t._2())
                                .fmap(result -> tuple(t._1(), t._2(), result))),
                Into3.into3((n, k, result) -> {
                    int actualK = Math.min(n, k);
                    return containsKElements(actualK, result)
                            && allElementsInRange(n, result)
                            && containsNoRepeats(result);
                }));
    }

    private static <A> boolean containsKElements(int k, FiniteIterable<A> xs) {
        return xs.size() == k;
    }

    private static <A> boolean containsNoRepeats(FiniteIterable<A> xs) {
        return xs.toCollection(HashSet::new).size() == xs.size();
    }

    private static <A> boolean allElementsInRange(int n, FiniteIterable<Integer> xs) {
        return all(x -> x >= 0 && x < n, xs);
    }
}
