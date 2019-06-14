package dev.marksman.composablerandom;

import dev.marksman.composablerandom.random.StandardGen;
import dev.marksman.enhancediterables.ImmutableIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

import static dev.marksman.composablerandom.Generator.tap;
import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

class Infinite {

    static <A> Generator<ImmutableNonEmptyIterable<A>> generateInfiniteIterable(Generator<A> generator) {
        return tap(generator,
                (inner, rs) -> {
                    StandardGen initialState = initStandardGen(rs.nextLong().getValue());
                    ImmutableIterable<A> iterable = () -> GeneratedStream.streamFrom(inner, initialState);
                    return iterable.toNonEmpty().orElseThrow(AssertionError::new);
                });
    }

}
