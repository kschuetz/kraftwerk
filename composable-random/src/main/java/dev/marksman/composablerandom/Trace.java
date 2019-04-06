package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static java.util.Collections.emptyList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Trace<A> {
    private final A result;
    private final Generator<A> generator;
    private final Iterable<Trace<?>> children;

    public static <A> Trace<A> trace(A result, Generator<A> generator, Iterable<Trace<?>> children) {
        return new Trace<>(result, generator, children);
    }

    public static <A> Trace<A> trace(A result, Generator<A> generator) {
        return new Trace<>(result, generator, emptyList());
    }
}
