package benchmarks;

import software.kes.kraftwerk.Generator;

public abstract class BenchmarkBase {
    public static final int DEFAULT_ITERATIONS = 5_000_000;

    protected static <A> long benchmark(Generator<A> gen, int iterations) {
        long t0 = System.currentTimeMillis();
        gen.run().drop(iterations);
        long t1 = System.currentTimeMillis();
        return t1 - t0;
    }
}
