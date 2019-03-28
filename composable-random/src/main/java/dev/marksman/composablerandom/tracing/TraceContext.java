package dev.marksman.composablerandom.tracing;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;

import java.util.function.Function;

public abstract class TraceContext implements CoProduct2<TraceContext.NoTrace,
        TraceContext.Tracing, TraceContext> {

    public abstract boolean isTracingEnabled();

    public static class NoTrace extends TraceContext {
        private static final NoTrace INSTANCE = new NoTrace();

        @Override
        public boolean isTracingEnabled() {
            return false;
        }

        @Override
        public <R> R match(Function<? super NoTrace, ? extends R> aFn, Function<? super Tracing, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    public static class Tracing extends TraceContext {

        @Override
        public boolean isTracingEnabled() {
            return true;
        }

        @Override
        public <R> R match(Function<? super NoTrace, ? extends R> aFn, Function<? super Tracing, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static NoTrace noTrace() {
        return NoTrace.INSTANCE;
    }
}
