package examples;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn2;
import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.kraftwerk.constraints.IntRange;

import static software.kes.kraftwerk.Cogenerator.cogeneratorInt;
import static software.kes.kraftwerk.Cogenerator.cogeneratorString;
import static software.kes.kraftwerk.Generators.generateFn2;
import static software.kes.kraftwerk.Generators.generateInt;

public class FunctionExample {

    private static final ImmutableNonEmptyVector<Tuple2<Integer, String>> testParams =
            Vector.of(1, 2, 3)
                    .cross(Vector.of("foo", "bar", "baz"));


    public static void main(String[] args) {
        generateFn2(cogeneratorInt(),
                cogeneratorString(),
                generateInt(IntRange.from(0).until(99)))
                .run()
                .take(10)
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
