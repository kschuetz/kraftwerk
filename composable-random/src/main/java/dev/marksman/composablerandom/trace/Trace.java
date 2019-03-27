package dev.marksman.composablerandom.trace;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Trace<A> {
    private final A value;
    private final Maybe<String> label;
    private final Iterable<Trace<Object>> children;
}
