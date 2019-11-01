package dev.marksman.composablerandom;

import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

class Infinite {

    // TODO: generateInfiniteIterable
    static <A> Generator<ImmutableNonEmptyIterable<A>> generateInfiniteIterable(Generator<A> gen) {
//        return legacyTap(gen,
//                (inner, rs) -> {
//                    StandardGen initialState = initStandardGen(rs.nextLong().getValue());
//                    ImmutableIterable<A> iterable = () -> GeneratedStream.streamFrom(inner, initialState);
//                    return iterable.toNonEmpty().orElseThrow(AssertionError::new);
//                });
        return null;
    }

}
