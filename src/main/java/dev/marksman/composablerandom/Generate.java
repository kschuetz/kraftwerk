package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
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
import static dev.marksman.composablerandom.domain.Choices.choices;
import static java.util.Arrays.asList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Generate<A> implements Monad<A, Generate> {

    private static final Generate<Boolean> GENERATE_BOOLEAN = generate(State::nextBoolean);
    private static final Generate<Double> GENERATE_DOUBLE = generate(State::nextDouble);
    private static final Generate<Float> GENERATE_FLOAT = generate(State::nextFloat);
    private static final Generate<Integer> GENERATE_INTEGER = generate(State::nextInt);
    private static final Generate<Long> GENERATE_LONG = generate(State::nextLong);
    private static final Generate<Double> GENERATE_GAUSSIAN = generate(State::nextGaussian);

    private static final Generate<Byte> GENERATE_BYTE = GENERATE_INTEGER.fmap(Integer::byteValue);
    private static final Generate<Short> GENERATE_SHORT = GENERATE_INTEGER.fmap(Integer::shortValue);

    private final Fn1<? super State, Result<? extends State, A>> run;

    /**
     * Produces a value, and a new <code>State</code>
     *
     * @param state The <code>State</code> to provide as input.  The same <code>State</code>
     *                  will always yield the same result.
     * @return A <code>Result</code> containing a new <code>State</code> and a generate value
     */
    public final Result<? extends State, A> run(State state) {
        return run.apply(state);
    }

    /**
     * Produces a value when given a <code>State</code>.
     * <p>
     * Equivalent to calling <code>run</code> and discarding the <code>State</code> from the output.
     *
     * @param state The <code>State</code> to provide as input.  The same <code>State</code>
     *                  will always yield the same result.
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
        return generate(run.fmap(a -> a.fmap(fn)));
    }

    @Override
    public final <B> Generate<B> flatMap(Function<? super A, ? extends Monad<B, Generate>> fn) {
        return generate(rg0 -> {
            Result<? extends State, A> x = run.apply(rg0);
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
        return generate(rg0 -> {
            State current = rg0;
            ArrayList<A> result = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                Result<? extends State, A> next = run(current);
                current = next._1();
                result.add(next._2());
            }
            return result(current, result);
        });
    }

    public static <A> Generate<A> generate(Fn1<? super State, Result<? extends State, A>> run) {
        return new Generate<>(run);
    }

    public static <A> Generate<A> generate(Function<? super State, Result<? extends State, A>> run) {
        return generate(run::apply);
    }

    public static <A> Generate<A> constant(A a) {
        return generate(rg -> result(rg, a));
    }

    public static Generate<Boolean> generateBoolean() {
        return GENERATE_BOOLEAN;
    }

    public static Generate<Double> generateDouble() {
        return GENERATE_DOUBLE;
    }

    public static Generate<Integer> generateInt() {
        return GENERATE_INTEGER;
    }

    public static Generate<Integer> generateInt(int bound) {
        return generate(s -> s.nextInt(bound));
    }

    public static Generate<Integer> generateInt(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        int n = bound - origin;
        int m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return generateInt(n).fmap(r -> origin + r);
        } else if ((n & m) == 0) {
            // power of two
            return generateInt().fmap(r -> (r & m) + origin);
        } else return generate(rg0 -> {
            Result<? extends State, Integer> rg1 = rg0.nextInt();
            State current = rg1._1();
            int r = rg1._2();
            for (int u = r >>> 1;
                 u + m - (r = u % n) < 0; ) {
                Result<? extends State, Integer> next = current.nextInt();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        });
    }

    public static Generate<Float> generateFloat() {
        return GENERATE_FLOAT;
    }

    public static Generate<Long> generateLong() {
        return GENERATE_LONG;
    }

    public static Generate<Long> generateLong(long bound) {
        if (bound <= Integer.MAX_VALUE) {
            return generateInt((int) bound).fmap(Integer::longValue);
        } else {
            return generateLong(0, bound);
        }
    }

    public static Generate<Long> generateLong(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = bound - origin;
        long m = n - 1;

        if ((n & m) == 0L) {
            // power of two
            return generateLong().fmap(r -> (r & m) + origin);
        } else return generate(rg0 -> {
            Result<? extends State, Long> rg1 = rg0.nextLong();
            State current = rg1._1();
            long r = rg1._2();
            for (long u = r >>> 1;
                 u + m - (r = u % n) < 0L; ) {
                Result<? extends State, Long> next = current.nextLong();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return result(current, r);
        });
    }

    public static Generate<Double> generateGaussian() {
        return GENERATE_GAUSSIAN;
    }

    public static Generate<Byte[]> generateBytes(int count) {
        return generate(s -> {
            byte[] buffer = new byte[count];
            Result<? extends State, Unit> next = s.nextBytes(buffer);
            Byte[] result = new Byte[count];
            int i = 0;
            for (byte b : buffer) {
                result[i++] = b;
            }
            return result(next._1(), result);
        });
    }

    public static Generate<Byte> generateByte() {
        return GENERATE_BYTE;
    }

    public static Generate<Short> generateShort() {
        return GENERATE_SHORT;
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
            Result<? extends State, A> result = run(current);
            current = result._1();
            return result._2();
        }
    }

}
