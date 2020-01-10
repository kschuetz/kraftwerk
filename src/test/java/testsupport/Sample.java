package testsupport;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Seed;

import java.util.ArrayList;

import static dev.marksman.kraftwerk.StandardParameters.defaultParameters;
import static dev.marksman.kraftwerk.ValueSupplyIterator.streamFrom;

public class Sample {
    private static final int SAMPLE_COUNT = 10000;

    public static <A> ArrayList<A> sample(Generator<A> gen) {
        return streamFrom(gen).next(SAMPLE_COUNT);
    }

    public static <A> ArrayList<A> sample(Generator<A> gen, Seed initialState) {
        return streamFrom(gen.prepare(defaultParameters()), initialState).next(SAMPLE_COUNT);
    }
}
