package benchmarks;

import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class InjectSpecialValuesBenchmark extends BenchmarkBase {
    private static final int ITERATIONS = 50_000_000;

    public static void main(String[] args) {
        Generator<Integer> baseGenerator = generateInt(IntRange.from(0).to(15));
        Generator<Integer> gen = baseGenerator.injectSpecialValues(Vector.of(998, 999));
        System.out.println(gen.run().take(100000).foldLeft((acc, n) -> n >= 998 ? acc + 1 : acc, 0));
        System.out.println("Base: " + benchmark(baseGenerator, ITERATIONS));
        System.out.println("Special values: " + benchmark(gen, ITERATIONS));
    }
}
