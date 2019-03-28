package dev.marksman.composablerandom.tracing;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.lens.Lens;
import dev.marksman.composablerandom.ContextLens;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.State;
import dev.marksman.composablerandom.StateLens;
import dev.marksman.composablerandom.metadata.Metadata;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static dev.marksman.composablerandom.tracing.TraceContext.tracing;

public class RunWithTrace {

    private static Lens.Simple<State, TraceContext> stateTraceContextLens =
            StateLens.context.andThen(ContextLens.traceContext);


    public static <A> Result<State, A> runWithTrace(Fn1<? super State, Result<State, A>> run,
                                                    Metadata metadata, State inputState) {

        TraceCollector parentCollector = view(stateTraceContextLens, inputState).getCollector();

        Result<State, A> result = run.apply(set(stateTraceContextLens, tracing(), inputState));
        State outputState = result.getNextState();

        Iterable<Trace<Object>> collectedTraces = reverse(view(stateTraceContextLens, outputState)
                .getCollector().getCollectedTraces());

        Trace<Object> traceResult = Trace.trace(result.getValue(), metadata, collectedTraces);
        State finalState = set(stateTraceContextLens, tracing(parentCollector.add(traceResult)), outputState);

        return result.withNextState(finalState);
    }

    // if tracing enabled:
    //  - save existing TraceCollector
    //  - create new TraceCollector, assign to context
    //  - call run.apply
    //  - pull TraceCollector off of context
    //  - using result, and TraceCollector, build Trace
    //  - add Trace to saved TraceCollector
    //  - restore saved TraceCollector to context

}
