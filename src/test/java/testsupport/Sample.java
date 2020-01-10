package testsupport;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Seed;

import java.util.ArrayList;

public class Sample {
    private static final int SAMPLE_COUNT = 10000;

    public static <A> ArrayList<A> sample(Generator<A> gen) {
        return gen.run().take(SAMPLE_COUNT).toCollection(ArrayList::new);
    }

    public static <A> ArrayList<A> sample(Generator<A> gen, Seed initialState) {
        return gen.run(initialState).take(SAMPLE_COUNT).toCollection(ArrayList::new);
    }
}
