package dev.marksman.composablerandom.tracing;

import dev.marksman.composablerandom.metadata.Metadata;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static java.util.Collections.emptyList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Trace<A> {
    private final A result;
    private final Metadata metadata;
    private final Iterable<Trace<?>> children;

    public static <A> Trace<A> trace(A result, Metadata metadata, Iterable<Trace<?>> children) {
        return new Trace<>(result, metadata, children);
    }

    public static <A> Trace<A> trace(A result, Metadata metadata) {
        return new Trace<>(result, metadata, emptyList());
    }
}
