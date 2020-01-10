package dev.marksman.kraftwerk.spike;

import dev.marksman.kraftwerk.*;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamFrom {

    public static <A> Stream<A> streamFrom(Generator<A> gen, Parameters parameters, Seed initialSeed) {
        return streamFrom(gen.prepare(parameters), initialSeed);
    }

    private static <A> Stream<A> streamFrom(Generate<A> gen, Seed initialSeed) {
        Iterable<A> iterable = () -> GeneratedStream.streamFrom(gen, initialSeed);
        return StreamSupport.stream(iterable.spliterator(), false);

    }
}
