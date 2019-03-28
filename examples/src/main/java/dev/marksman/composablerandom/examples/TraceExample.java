package dev.marksman.composablerandom.examples;

import com.jnape.palatable.lambda.lens.Lens;
import dev.marksman.composablerandom.ContextLens;
import dev.marksman.composablerandom.State;
import dev.marksman.composablerandom.StateLens;
import dev.marksman.composablerandom.tracing.TraceContext;

public class TraceExample {

    private static Lens.Simple<State, TraceContext> stateTraceContextLens =
            StateLens.context.andThen(ContextLens.traceContext);


    public static void main(String[] args) {

    }
}
