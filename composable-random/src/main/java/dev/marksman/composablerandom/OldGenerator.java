package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.composablerandom.legacy.State;
import dev.marksman.composablerandom.metadata.Metadata;
import dev.marksman.composablerandom.metadata.StandardMetadata;
import dev.marksman.composablerandom.tracing.Trace;
import lombok.Value;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.RunWithTrace.innerTrace;
import static dev.marksman.composablerandom.RunWithTrace.outerTrace;
import static dev.marksman.composablerandom.legacy.OldGeneratedStream.streamFrom;
import static dev.marksman.composablerandom.metadata.StandardMetadata.defaultMetadata;
import static dev.marksman.composablerandom.metadata.StandardMetadata.labeled;

@Value
public class OldGenerator<A> implements Monad<A, OldGenerator> {

    private static final StandardMetadata CONSTANT_METADATA = labeled("constant");

    private final Fn1<? super State, Result<State, A>> run;
    private final Metadata metadata;

    private OldGenerator(Metadata metadata, Fn1<? super State, Result<State, A>> run) {
        this.run = run;
        this.metadata = metadata;
    }

    private OldGenerator(Fn1<? super State, Result<State, A>> run) {
        this(defaultMetadata(), run);
    }

    /**
     * Produces a value, and a new <code>State</code>
     *
     * @param inputState The <code>State</code> to provide as input.  The same <code>State</code>
     *                   will always yield the same result.
     * @return A <code>Result</code> containing a new <code>State</code> and a generated value
     */
    public final Result<State, A> run(State inputState) {
        if (inputState.getContext().getTraceContext().isTracingEnabled()) {
            return innerTrace(run, metadata, inputState);
        } else {
            return run.apply(inputState);
        }
    }

    /**
     * Produces a value when given a <code>State</code>.
     * <p>
     * Equivalent to calling <code>run</code> and discarding the <code>State</code> from the output.
     *
     * @param state The <code>State</code> to provide as input.  The same <code>State</code>
     *              will always yield the same result.
     * @return A generated value
     */
    public final A getValue(State state) {
        return run(state)._2();
    }

    public final Iterable<A> infiniteStream(State initial) {
        return () -> streamFrom(this, initial);
    }

    @Override
    public final <B> OldGenerator<B> fmap(Function<? super A, ? extends B> fn) {
        return contextDependent(run.fmap(a -> a.fmap(fn)));
    }

    @Override
    public final <B> OldGenerator<B> flatMap(Function<? super A, ? extends Monad<B, OldGenerator>> fn) {
        return contextDependent(s0 -> {
            Result<State, A> x = run(s0);
            return ((OldGenerator<B>) fn.apply(x._2())).run(x._1());
        });
    }

    @Override
    public final <B> OldGenerator<B> pure(B b) {
        return constant(b);
    }

    public final OldGenerator<A> withLabel(String text) {
        return new OldGenerator<>(metadata.withLabel(text), run);
    }

    public final OldGenerator<A> withMetadata(Metadata metadata) {
        return new OldGenerator<>(metadata, run);
    }

    public final OldGenerator<Trace<A>> withTrace() {
        return outerTrace(this);
    }

    public static <A> OldGenerator<A> contextDependent(Fn1<? super State, Result<State, A>> run) {
        return new OldGenerator<>(run);
    }

    public static <A> OldGenerator<A> contextDependent(Metadata metadata, Fn1<? super State, Result<State, A>> run) {
        return new OldGenerator<>(metadata, run);
    }

    public static <A> OldGenerator<A> contextDependent(Function<? super State, Result<State, A>> run) {
        return new OldGenerator<>(run::apply);
    }

    public static <A> OldGenerator<A> contextDependent(Metadata metadata, Function<? super State, Result<State, A>> run) {
        return new OldGenerator<>(metadata, run::apply);
    }

    public static <A> OldGenerator<A> generator(Fn1<? super RandomState, Result<? extends RandomState, A>> run) {
        return contextDependent(state ->
                (Result<State, A>) run.apply(state.getRandomState())
                        .biMapL(state::withRandomState));
    }

    public static <A> OldGenerator<A> generator(Metadata metadata, Fn1<? super RandomState, Result<? extends RandomState, A>> run) {
        return contextDependent(metadata, state ->
                (Result<State, A>) run.apply(state.getRandomState())
                        .biMapL(state::withRandomState));
    }

    public static <A> OldGenerator<A> generator(Function<? super RandomState, Result<? extends RandomState, A>> run) {
        return generator(run::apply);
    }

    public static <A> OldGenerator<A> generator(Metadata metadata, Function<? super RandomState, Result<? extends RandomState, A>> run) {
        return generator(metadata, run::apply);
    }

    public static <A> OldGenerator<A> constant(A a) {
        return generator(CONSTANT_METADATA, rg -> result(rg, a));
    }

    // TODO: experimental
    public static <A> OldGenerator<A> lazy(Supplier<A> supplier) {
        AtomicReference<A> cache = new AtomicReference<>();
        return generator(rg -> {
            A result = cache.get();
            if (result == null) {
                result = supplier.get();
                if (!cache.compareAndSet(null, result)) {
                    result = cache.get();
                }
            }
            return result(rg, result);
        });
    }

}
