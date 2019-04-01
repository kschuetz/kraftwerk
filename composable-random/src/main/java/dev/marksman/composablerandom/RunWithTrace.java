package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.lens.Lens;
import dev.marksman.composablerandom.legacy.State;
import dev.marksman.composablerandom.legacy.StateLens;
import dev.marksman.composablerandom.metadata.Metadata;
import dev.marksman.composablerandom.tracing.Trace;
import dev.marksman.composablerandom.tracing.TraceCollector;
import dev.marksman.composablerandom.tracing.TraceContext;

import java.util.ArrayList;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static dev.marksman.composablerandom.OldGenerator.contextDependent;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.tracing.TraceContext.tracing;

class RunWithTrace {

    private static Lens.Simple<State, TraceContext> stateTraceContextLens =
            StateLens.context.andThen(ContextLens.traceContext);

    static <A> OldGenerator<Trace<A>> outerTrace(OldGenerator<A> g) {
        return contextDependent((State s0) -> {
            TraceContext previous = view(stateTraceContextLens, s0);
            State newInput = set(stateTraceContextLens, tracing(), s0);
            Result<State, Trace<A>> stateTraceResult = traceImpl(g.getRun(), g.getMetadata(), newInput);
            return (Result<State, Trace<A>>) stateTraceResult.biMapL(set(stateTraceContextLens, previous));
        });
    }

    static <A> Result<State, A> innerTrace(Fn1<? super State, Result<State, A>> run,
                                           Metadata metadata, State inputState) {
        TraceCollector parentCollector = view(stateTraceContextLens, inputState).getCollector();

        Result<State, Trace<A>> tracedOutput = traceImpl(run, metadata, inputState);

        Trace<A> trace = tracedOutput.getValue();
        TraceCollector added = parentCollector.add(trace);
        State finalState = set(stateTraceContextLens, tracing(added), tracedOutput.getNextState());

        return result(finalState, tracedOutput.getValue().getResult());
    }

    private static <A> Result<State, Trace<A>> traceImpl(Fn1<? super State, Result<State, A>> run,
                                                         Metadata metadata, State inputState) {
        Result<State, A> result = run.apply(set(stateTraceContextLens, tracing(), inputState));
        State outputState = result.getNextState();

        Iterable<Trace<?>> collectedTraces = toCollection(ArrayList::new, reverse(view(stateTraceContextLens, outputState)
                .getCollector().getCollectedTraces()));

        Trace<A> traceResult = Trace.trace(result.getValue(), metadata, collectedTraces);
        return result(outputState, traceResult);
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
