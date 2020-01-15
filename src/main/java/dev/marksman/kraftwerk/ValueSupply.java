package dev.marksman.kraftwerk;

import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;

import java.util.stream.Stream;

import static dev.marksman.kraftwerk.DefaultValueSupply.defaultValueSupply;

public interface ValueSupply<A> extends ImmutableNonEmptyIterable<A> {
    Stream stream();

    static <A> ValueSupply<A> valueSupply(Generate<A> gen, Seed initialState) {
        return defaultValueSupply(gen, initialState);
    }
}
