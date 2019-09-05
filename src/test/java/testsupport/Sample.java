package testsupport;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;

import java.util.ArrayList;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;

public class Sample {
    private static final int SAMPLE_COUNT = 10000;

    public static <A> ArrayList<A> sample(Generator<A> gen) {
        return streamFrom(gen).next(SAMPLE_COUNT);
    }

    public static <A> ArrayList<A> sample(Generator<A> gen, RandomState initialState) {
        return streamFrom(gen, initialState).next(SAMPLE_COUNT);
    }
}
