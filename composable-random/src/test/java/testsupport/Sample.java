package testsupport;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;

import java.util.ArrayList;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;

public class Sample {
    private static final int SAMPLE_COUNT = 10000;

    public static <A> ArrayList<A> sample(Generator<A> generator) {
        return streamFrom(generator).next(SAMPLE_COUNT);
    }

    public static <A> ArrayList<A> sample(Generator<A> generator, RandomState initialState) {
        return streamFrom(generator, initialState).next(SAMPLE_COUNT);
    }
}
