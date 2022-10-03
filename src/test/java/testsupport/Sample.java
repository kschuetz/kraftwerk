package testsupport;

import software.kes.kraftwerk.Generator;
import software.kes.kraftwerk.Seed;

import java.util.ArrayList;

public class Sample {
    private static final int SAMPLE_COUNT = 2000;

    public static <A> ArrayList<A> sample(Generator<A> gen) {
        return sample(SAMPLE_COUNT, gen);
    }

    public static <A> ArrayList<A> sample(Generator<A> gen, Seed initialState) {
        return sample(SAMPLE_COUNT, gen, initialState);
    }

    public static <A> ArrayList<A> sample(int sampleCount, Generator<A> gen) {
        return gen.run().take(sampleCount).toCollection(ArrayList::new);
    }

    public static <A> ArrayList<A> sample(int sampleCount, Generator<A> gen, Seed initialState) {
        return gen.run(initialState).take(sampleCount).toCollection(ArrayList::new);
    }
}
