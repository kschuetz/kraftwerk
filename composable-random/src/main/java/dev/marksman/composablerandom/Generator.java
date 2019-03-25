package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iteration.InfiniteIterator;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.composablerandom.builtin.Generators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.builtin.Generators.tupled;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Generator<A> implements Monad<A, Generator> {

    private final Fn1<? super State, Result<State, A>> run;

    /**
     * Produces a value, and a new <code>RandomState</code>
     *
     * @param randomState The <code>RandomState</code> to provide as input.  The same <code>RandomState</code>
     *                    will always yield the same result.
     * @return A <code>Result</code> containing a new <code>RandomState</code> and a generate value
     */
    public final Result<State, A> run(State randomState) {
        return run.apply(randomState);
    }

    /**
     * Produces a value when given a <code>RandomState</code>.
     * <p>
     * Equivalent to calling <code>run</code> and discarding the <code>RandomState</code> from the output.
     *
     * @param state The <code>RandomState</code> to provide as input.  The same <code>RandomState</code>
     *              will always yield the same result.
     * @return A generate value
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
            Result<State, A> x = run.apply(s0);
            return ((Generator<B>) fn.apply(x._2())).run.apply(x._1());
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

    public final Generator<Maybe<A>> maybe(int justFrequency) {
        return Generators.generateMaybe(justFrequency, this);
    }

    public final Generator<Maybe<A>> maybe() {
        return Generators.generateMaybe(this);
    }

    public final Generator<Maybe<A>> just() {
        return Generators.generateJust(this);
    }

    public final Generator<ArrayList<A>> listOfN(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return contextDependent(s0 -> {
            State current = s0;
            ArrayList<A> result = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                Result<State, A> next = run(current);
                current = next._1();
                result.add(next._2());
            }
            return result(current, result);
        });
    }

    public static <A> Generator<A> contextDependent(Fn1<? super State, Result<State, A>> run) {
        return new Generator<>(run);
    }

    public static <A> Generator<A> contextDependent(Function<? super State, Result<State, A>> run) {
        return new Generator<>(run::apply);
    }

    public static <A> Generator<A> generator(Fn1<? super RandomState, Result<? extends RandomState, A>> run) {
        return contextDependent(state ->
                (Result<State, A>) run.apply(state.getRandomState())
                        .biMapL(state::withRandomState));
    }

    public static <A> Generator<A> generator(Function<? super RandomState, Result<? extends RandomState, A>> run) {
        return generator(run::apply);
    }

    public static <A> Generator<A> constant(A a) {
        return generator(rg -> result(rg, a));
    }

    public static <A> Generator<A> lazy(Supplier<A> supplier) {
        return generator(rg -> result(rg, supplier.get()));
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
