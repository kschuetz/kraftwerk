package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn2;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.Vector;

import static dev.marksman.kraftwerk.Cogenerator.cogeneratorInt;
import static dev.marksman.kraftwerk.Cogenerator.cogeneratorString;
import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static dev.marksman.kraftwerk.Generator.generateFn2;
import static dev.marksman.kraftwerk.Generator.generateIntExclusive;

public class FunctionExample {

    private static final ImmutableNonEmptyVector<Tuple2<Integer, String>> testParams =
            Vector.of(1, 2, 3)
                    .cross(Vector.of("foo", "bar", "baz"));


    public static void main(String[] args) {
        streamFrom(generateFn2(cogeneratorInt(),
                cogeneratorString(),
                generateIntExclusive(0, 99)))
                .next(10)
                .forEach(FunctionExample::runFunction);
    }

    private static void runFunction(Fn2<Integer, String, Integer> f) {
        testParams.forEach(t -> {
            Integer result = f.apply(t._1(), t._2());
            System.out.println("f(" + t._1() + ", \"" + t._2() + "\") = " + result);

        });
        System.out.println("------");
    }
}
