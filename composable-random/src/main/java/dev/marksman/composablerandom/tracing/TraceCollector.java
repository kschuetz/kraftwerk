package dev.marksman.composablerandom.tracing;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Collections.emptyList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TraceCollector {
    private static final TraceCollector EMPTY = traceCollector(emptyList());

    private final Iterable<Trace<?>> collectedTraces;

    public TraceCollector add(Trace<?> trace) {
        return traceCollector(cons(trace, collectedTraces));
    }

    public static TraceCollector traceCollector(Iterable<Trace<?>> collectedTraces) {
        return new TraceCollector(collectedTraces);
    }

    public static TraceCollector traceCollector() {
        return EMPTY;
    }
}
