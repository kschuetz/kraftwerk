package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Filter;
import com.jnape.palatable.lambda.iteration.InfiniteIterator;
import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.composablerandom.Result.result;
import static dev.marksman.composablerandom.builtin.Primitives.generateInt;
import static dev.marksman.composablerandom.builtin.Primitives.generateLong;
import static dev.marksman.composablerandom.domain.Choices.choices;
import static java.util.Arrays.asList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Generate<A> implements Monad<A, Generate> {

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
     *                    will always yield the same result.
     * @return A generate value
     */
    public final A getValue(State state) {
        return run(state)._2();
    }

    public final Iterable<A> infiniteStream(State initial) {
        return () -> new ValuesIterator(initial);
    }

    @Override
    public final <B> Generate<B> fmap(Function<? super A, ? extends B> fn) {
        return generateS(run.fmap(a -> a.fmap(fn)));
    }

    @Override
    public final <B> Generate<B> flatMap(Function<? super A, ? extends Monad<B, Generate>> fn) {
        return generateS(rg0 -> {
            Result<State, A> x = run.apply(rg0);
            return ((Generate<B>) fn.apply(x._2())).run.apply(x._1());
        });
    }

    @Override
    public final <B> Generate<B> pure(B b) {
        return constant(b);
    }

    public final Generate<Tuple2<A, A>> pair() {
        return tupled(this, this);
    }

    public final Generate<Tuple3<A, A, A>> triple() {
        return tupled(this, this, this);
    }

    public final Generate<Maybe<A>> maybe(int justFrequency) {
        if (justFrequency < 0) {
            throw new IllegalArgumentException("justFrequency must be >= 0");
        } else if (justFrequency == 0) {
            return constant(nothing());
        }
        Generate<Maybe<A>> just = fmap(Maybe::just);
        Generate<Maybe<A>> nothing = constant(nothing());
        return generateInt(1 + justFrequency)
                .flatMap(n -> n == 0 ? nothing : just);
    }

    public final Generate<Maybe<A>> maybe() {
        return maybe(9);
    }

    public final Generate<ArrayList<A>> listOfN(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return generateS(s0 -> {
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

    public static <A> Generate<A> generateS(Fn1<? super State, Result<State, A>> run) {
        return new Generate<>(run);
    }

    public static <A> Generate<A> generate(Fn1<? super RandomState, Result<? extends RandomState, A>> run) {
        return generateS(state ->
                (Result<State, A>) run.apply(state.getRandomState())
                        .biMapL(state::withRandomState));
    }

    public static <A> Generate<A> generate(Function<? super RandomState, Result<? extends RandomState, A>> run) {
        return generate(run::apply);
    }

    public static <A> Generate<A> constant(A a) {
        return generate(rg -> result(rg, a));
    }

    @SafeVarargs
    public static <A> Generate<A> oneOf(A first, A... more) {
        ArrayList<A> choices = new ArrayList<>();
        choices.add(first);
        choices.addAll(asList(more));
        return chooseFrom(choices);
    }

    public static <A> Generate<A> chooseFrom(Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException("chooseFrom requires at least one choice");
        }
        return fromDomain(choices(items));
    }

    public static <A> Generate<A> fromDomain(Domain<A> domain) {
        long size = domain.getSize();
        if (size == 1) {
            return constant(domain.getValue(1));
        } else {
            return generateLong(size).fmap(domain::getValue);
        }
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <A> Generate<A> frequency(FrequencyEntry<? extends A> first, FrequencyEntry<? extends A>... more) {
        Iterable<FrequencyEntry<? extends A>> fs = Filter.filter(f -> f.getWeight() > 0, cons(first, asList(more)));
        if (!fs.iterator().hasNext()) {
            throw new IllegalArgumentException("no items with positive weights");
        }
        long total = 0L;
        TreeMap<Long, Generate<? extends A>> tree = new TreeMap<>();
        for (FrequencyEntry<? extends A> f : fs) {
            total += f.getWeight();
            tree.put(total, f.getGenerate());
        }

        return (Generate<A>) generateLong(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

    public static <A, B> Generate<Tuple2<A, B>> tupled(Generate<A> ra, Generate<B> rb) {
        return ra.flatMap(a -> rb.fmap(b -> tuple(a, b)));
    }

    public static <A, B, C> Generate<Tuple3<A, B, C>> tupled(Generate<A> ra, Generate<B> rb, Generate<C> rc) {
        return ra.flatMap(a -> tupled(rb, rc).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D> Generate<Tuple4<A, B, C, D>> tupled(Generate<A> ra, Generate<B> rb, Generate<C> rc, Generate<D> rd) {
        return ra.flatMap(a -> tupled(rb, rc, rd).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E> Generate<Tuple5<A, B, C, D, E>> tupled(Generate<A> ra, Generate<B> rb, Generate<C> rc,
                                                                         Generate<D> rd, Generate<E> re) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F> Generate<Tuple6<A, B, C, D, E, F>> tupled(Generate<A> ra, Generate<B> rb, Generate<C> rc,
                                                                               Generate<D> rd, Generate<E> re, Generate<F> rf) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F, G> Generate<Tuple7<A, B, C, D, E, F, G>> tupled(Generate<A> ra, Generate<B> rb, Generate<C> rc,
                                                                                     Generate<D> rd, Generate<E> re, Generate<F> rf,
                                                                                     Generate<G> rg) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf, rg).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F, G, H> Generate<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generate<A> ra, Generate<B> rb, Generate<C> rc,
                                                                                           Generate<D> rd, Generate<E> re, Generate<F> rf,
                                                                                           Generate<G> rg, Generate<H> rh) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf, rg, rh).fmap(x -> x.cons(a)));
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
