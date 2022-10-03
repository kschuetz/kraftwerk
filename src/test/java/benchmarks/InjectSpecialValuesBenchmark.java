package benchmarks;

import software.kes.collectionviews.Vector;
import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.constraints.IntRange;

import static software.kes.kraftwerk.Generators.generateInt;

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
