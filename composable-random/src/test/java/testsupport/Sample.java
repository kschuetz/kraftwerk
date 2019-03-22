package testsupport;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.State;

import java.util.ArrayList;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;

public class Sample {
    private static final int SAMPLE_COUNT = 10000;

    public static <A> ArrayList<A> sample(Generator<A> generator) {
        return streamFrom(generator).next(SAMPLE_COUNT);
    }

    public static <A> ArrayList<A> sample(Generator<A> generator, State initialState) {
        return streamFrom(generator, initialState).next(SAMPLE_COUNT);
    }
}
