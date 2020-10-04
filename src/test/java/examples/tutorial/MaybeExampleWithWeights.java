package examples.tutorial;

import dev.marksman.kraftwerk.weights.MaybeWeights;

import static dev.marksman.kraftwerk.Generators.generateString;

public class MaybeExampleWithWeights {
    public static void main(String[] args) {
        generateString().maybe(MaybeWeights.justs(3).toNothings(1))
                .run()
                .take(10)
                .forEach(System.out::println);

        //sample output:
        //Just 9oo*=4+f!OCW
        //Just ^oSbZU
        //Just K)[B3:x]ob^8\s
        //Nothing
        //Nothing
        //Just  [cMM
        //Nothing
        //Just L
        //Just :_{MS
        //Just O`0>Q0
    }
}
