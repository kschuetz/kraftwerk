package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static java.util.Collections.emptyList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Trace<A> {
    private final A result;
    private final Generate<A> source;
    private final Iterable<Trace<?>> children;

    public static <A> Trace<A> trace(A result, Generate<A> gen, Iterable<Trace<?>> children) {
        return new Trace<>(result, gen, children);
    }

    public static <A> Trace<A> trace(A result, Generate<A> gen) {
        return new Trace<>(result, gen, emptyList());
    }
}
