package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iteration.InfiniteIterator;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.composablerandom.builtin.Generators;
import dev.marksman.composablerandom.metadata.Metadata;
import dev.marksman.composablerandom.metadata.StandardMetadata;
import lombok.Value;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.builtin.Generators.tupled;
import static dev.marksman.composablerandom.metadata.StandardMetadata.defaultMetadata;
import static dev.marksman.composablerandom.metadata.StandardMetadata.labeled;

@Value
public class Generator<A> implements Monad<A, Generator> {

    private static final StandardMetadata CONSTANT_METADATA = labeled("constant");

    private final Fn1<? super State, Result<State, A>> run;
    private final Metadata metadata;

    private Generator(Metadata metadata, Fn1<? super State, Result<State, A>> run) {
        this.run = run;
        this.metadata = metadata;
    }

    private Generator(Fn1<? super State, Result<State, A>> run) {
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
        return run.apply(inputState);
    }

    // if tracing enabled:
    //  - save existing TraceBuilder
    //  - create new TraceBuilder, assign to context
    //  - call run.apply
    //  - pull TraceBuilder off of context
    //  - using result, and TraceBuilder, build Trace
    //  - add Trace to saved TraceBuilder
    //  - restore saved TraceBuilder to context

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
    public final <B> Generator<B> fmap(Function<? super A, ? extends B> fn) {
        return contextDependent(run.fmap(a -> a.fmap(fn)));
    }

    @Override
    public final <B> Generator<B> flatMap(Function<? super A, ? extends Monad<B, Generator>> fn) {
        return contextDependent(s0 -> {
            Result<State, A> x = run(s0);
            return ((Generator<B>) fn.apply(x._2())).run(x._1());
        });
    }

    @Override
    public final <B> Generator<B> pure(B b) {
        return constant(b);
    }

    public final Generator<Tuple2<A, A>> pair() {
        return tupled(this, this);
    }

    public final Generator<Tuple3<A, A, A>> triple() {
        return tupled(this, this, this);
    }

    public final Generator<Maybe<A>> maybe(int nothingWeight, int justWeight) {
        return Generators.generateMaybe(nothingWeight, justWeight, this);
    }

    public final Generator<Maybe<A>> maybe(int justWeight) {
        return Generators.generateMaybe(justWeight, this);
    }

    public final Generator<Maybe<A>> maybe() {
        return Generators.generateMaybe(this);
    }

    public final Generator<Maybe<A>> just() {
        return Generators.generateJust(this);
    }

    public final Generator<ArrayList<A>> listOfN(int n) {
        return Generators.generateListOfN(n, this);
    }

    public final Generator<A> withLabel(String text) {
        return new Generator<>(metadata.withLabel(text), run);
    }

    public final Generator<A> withMetadata(Metadata metadata) {
        return new Generator<>(metadata, run);
    }

    public final Generator<A> modifyMetadata(Function<Metadata, Metadata> fn) {
        return new Generator<>(fn.apply(metadata), run);
    }

    public static <A> Generator<A> contextDependent(Fn1<? super State, Result<State, A>> run) {
        return new Generator<>(run);
    }

    public static <A> Generator<A> contextDependent(Metadata metadata, Fn1<? super State, Result<State, A>> run) {
        return new Generator<>(metadata, run);
    }

    public static <A> Generator<A> contextDependent(Function<? super State, Result<State, A>> run) {
        return new Generator<>(run::apply);
    }

    public static <A> Generator<A> contextDependent(Metadata metadata, Function<? super State, Result<State, A>> run) {
        return new Generator<>(metadata, run::apply);
    }

    public static <A> Generator<A> generator(Fn1<? super RandomState, Result<? extends RandomState, A>> run) {
        return contextDependent(state ->
                (Result<State, A>) run.apply(state.getRandomState())
                        .biMapL(state::withRandomState));
    }

    public static <A> Generator<A> generator(Metadata metadata, Fn1<? super RandomState, Result<? extends RandomState, A>> run) {
        return contextDependent(metadata, state ->
                (Result<State, A>) run.apply(state.getRandomState())
                        .biMapL(state::withRandomState));
    }

    public static <A> Generator<A> generator(Function<? super RandomState, Result<? extends RandomState, A>> run) {
        return generator(run::apply);
    }

    public static <A> Generator<A> generator(Metadata metadata, Function<? super RandomState, Result<? extends RandomState, A>> run) {
        return generator(metadata, run::apply);
    }

    public static <A> Generator<A> constant(A a) {
        return generator(CONSTANT_METADATA, rg -> result(rg, a));
    }

    // TODO: experimental
    public static <A> Generator<A> lazy(Supplier<A> supplier) {
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

    private class ValuesIterator extends InfiniteIterator<A> {
        private State current;

        public ValuesIterator(State initial) {
            this.current = initial;
        }

        @Override
        public A next() {
            Result<State, A> result = run(current);
            current = result._1();
            return result._2();
        }
    }

}
