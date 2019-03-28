package dev.marksman.composablerandom.tracing;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Function;

import static dev.marksman.composablerandom.tracing.TraceCollector.traceCollector;

public abstract class TraceContext implements CoProduct2<TraceContext.NoTrace,
        TraceContext.Tracing, TraceContext> {

    public abstract boolean isTracingEnabled();

    public abstract TraceCollector getCollector();

    public static final class NoTrace extends TraceContext {
        private static final NoTrace INSTANCE = new NoTrace();

        @Override
        public boolean isTracingEnabled() {
            return false;
        }

        @Override
        public TraceCollector getCollector() {
            return traceCollector();
        }

        @Override
        public <R> R match(Function<? super NoTrace, ? extends R> aFn, Function<? super Tracing, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor
    public static final class Tracing extends TraceContext {
        private static final Tracing DEFAULT = new Tracing(traceCollector());

        private final TraceCollector collector;

        @Override
        public boolean isTracingEnabled() {
            return true;
        }

        public Tracing add(Trace<Object> trace) {
            return tracing(collector.add(trace));
        }

        @Override
        public <R> R match(Function<? super NoTrace, ? extends R> aFn, Function<? super Tracing, ? extends R> bFn) {
            return bFn.apply(this);
        }

    }

    public static NoTrace noTrace() {
        return NoTrace.INSTANCE;
    }

    public static Tracing tracing(TraceCollector collector) {
        return new Tracing(collector);
    }

    public static Tracing tracing() {
        return Tracing.DEFAULT;
    }
}
