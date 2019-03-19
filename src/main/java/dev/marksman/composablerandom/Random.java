package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Filter;
import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.composablerandom.Result.mapResult;
import static dev.marksman.composablerandom.domain.Choices.choices;
import static java.util.Arrays.asList;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Random<A> implements Monad<A, Random> {

    private static final Random<Boolean> RANDOM_BOOLEAN = random(RandomGen::nextBoolean);
    private static final Random<Double> RANDOM_DOUBLE = random(RandomGen::nextDouble);
    private static final Random<Float> RANDOM_FLOAT = random(RandomGen::nextFloat);
    private static final Random<Integer> RANDOM_INTEGER = random(RandomGen::nextInt);
    private static final Random<Long> RANDOM_LONG = random(RandomGen::nextLong);
    private static final Random<Double> RANDOM_GAUSSIAN = random(RandomGen::nextGaussian);

    private static final Random<Byte> RANDOM_BYTE = RANDOM_INTEGER.fmap(Integer::byteValue);
    private static final Random<Short> RANDOM_SHORT = RANDOM_INTEGER.fmap(Integer::shortValue);

    private final Fn1<? super RandomGen, Product2<? extends RandomGen, A>> run;

    public final Product2<? extends RandomGen, A> run(RandomGen randomGen) {
        return run.apply(randomGen);
    }

    @Override
    public final <B> Random<B> fmap(Function<? super A, ? extends B> fn) {
        return random(run.fmap(a -> mapResult(fn, a)));
    }

    @Override
    public final <B> Random<B> flatMap(Function<? super A, ? extends Monad<B, Random>> fn) {
        return random(rg0 -> {
            Product2<? extends RandomGen, A> x = run.apply(rg0);
            return ((Random<B>) fn.apply(x._2())).run.apply(x._1());
        });
    }

    @Override
    public final <B> Monad<B, Random> pure(B b) {
        return constant(b);
    }

    public final Random<Tuple2<A, A>> pair() {
        return tupled(this, this);
    }

    public final Random<Tuple3<A, A, A>> triple() {
        return tupled(this, this, this);
    }

    public final Random<Maybe<A>> maybe(int justFrequency) {
        if (justFrequency < 0) {
            throw new IllegalArgumentException("justFrequency must be non-negative");
        } else if (justFrequency == 0) {
            return constant(nothing());
        }
        Random<Maybe<A>> just = fmap(Maybe::just);
        Random<Maybe<A>> nothing = constant(nothing());
        return randomInt(1 + justFrequency)
                .flatMap(n -> n == 0 ? nothing : just);
    }

    public final Random<Maybe<A>> maybe() {
        return maybe(9);
    }

    public static <A> Random<A> random(Fn1<? super RandomGen, Product2<? extends RandomGen, A>> run) {
        return new Random<>(run);
    }

    public static <A> Random<A> random(Function<? super RandomGen, Product2<? extends RandomGen, A>> run) {
        return random(run::apply);
    }

    public static <A> Random<A> constant(A a) {
        return random(rg -> product(rg, a));
    }

    public static Random<Boolean> randomBoolean() {
        return RANDOM_BOOLEAN;
    }

    public static Random<Double> randomDouble() {
        return RANDOM_DOUBLE;
    }

    public static Random<Integer> randomInt() {
        return RANDOM_INTEGER;
    }

    public static Random<Integer> randomInt(int bound) {
        return random(s -> s.nextInt(bound));
    }

    public static Random<Integer> randomInt(int origin, int bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        int n = bound - origin;
        int m = n - 1;
        if (n < Integer.MAX_VALUE) {
            return randomInt(n).fmap(r -> origin + r);
        } else if ((n & m) == 0) {
            // power of two
            return randomInt().fmap(r -> (r & m) + origin);
        } else return random(rg0 -> {
            Product2<? extends RandomGen, Integer> rg1 = rg0.nextInt();
            RandomGen current = rg1._1();
            int r = rg1._2();
            for (int u = r >>> 1;
                 u + m - (r = u % n) < 0; ) {
                Product2<? extends RandomGen, Integer> next = current.nextInt();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return product(current, r);
        });
    }

    public static Random<Float> randomFloat() {
        return RANDOM_FLOAT;
    }

    public static Random<Long> randomLong() {
        return RANDOM_LONG;
    }

    public static Random<Long> randomLong(long bound) {
        if (bound <= Integer.MAX_VALUE) {
            return randomInt((int) bound).fmap(Integer::longValue);
        } else {
            return randomLong(0, bound);
        }
    }

    public static Random<Long> randomLong(long origin, long bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("bound must be greater than origin");
        }
        long n = bound - origin;
        long m = n - 1;

        if ((n & m) == 0L) {
            // power of two
            return randomLong().fmap(r -> (r & m) + origin);
        } else return random(rg0 -> {
            Product2<? extends RandomGen, Long> rg1 = rg0.nextLong();
            RandomGen current = rg1._1();
            long r = rg1._2();
            for (long u = r >>> 1;
                 u + m - (r = u % n) < 0L; ) {
                Product2<? extends RandomGen, Long> next = current.nextLong();
                u = next._2() >>> 1;
                current = next._1();
            }
            r += origin;

            return product(current, r);
        });
    }

    public static Random<Double> randomGaussian() {
        return RANDOM_GAUSSIAN;
    }

    public static Random<Byte[]> randomBytes(int count) {
        return random(s -> {
            byte[] buffer = new byte[count];
            Product2<? extends RandomGen, Unit> next = s.nextBytes(buffer);
            Byte[] result = new Byte[count];
            int i = 0;
            for (byte b : buffer) {
                result[i++] = b;
            }
            return product(next._1(), result);
        });
    }

    public static Random<Byte> randomByte() {
        return RANDOM_BYTE;
    }

    public static Random<Short> randomShort() {
        return RANDOM_SHORT;
    }

    @SafeVarargs
    public static <A> Random<A> oneOf(A first, A... more) {
        ArrayList<A> choices = new ArrayList<>();
        choices.add(first);
        choices.addAll(asList(more));
        return chooseFrom(choices);
    }

    public static <A> Random<A> chooseFrom(Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException("chooseFrom requires at least one choice");
        }
        return fromDomain(choices(items));
    }

    public static <A> Random<A> fromDomain(Domain<A> domain) {
        long size = domain.getSize();
        if (size == 1) {
            return constant(domain.getValue(1));
        } else {
            return randomLong(size).fmap(domain::getValue);
        }
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <A> Random<A> frequency(Freq<? extends A> first, Freq<? extends A>... more) {
        Iterable<Freq<? extends A>> fs = Filter.filter(f -> f.getWeight() > 0, cons(first, asList(more)));
        if (!fs.iterator().hasNext()) {
            throw new IllegalArgumentException("no items with positive weights");
        }
        long total = 0L;
        TreeMap<Long, Random<? extends A>> tree = new TreeMap<>();
        for (Freq<? extends A> f : fs) {
            total += f.getWeight();
            tree.put(total, f.getRandom());
        }

        return (Random<A>) randomLong(total)
                .flatMap(n -> tree.ceilingEntry(1 + n).getValue());
    }

    public static <A, B> Random<Tuple2<A, B>> tupled(Random<A> ra, Random<B> rb) {
        return ra.flatMap(a -> rb.fmap(b -> tuple(a, b)));
    }

    public static <A, B, C> Random<Tuple3<A, B, C>> tupled(Random<A> ra, Random<B> rb, Random<C> rc) {
        return ra.flatMap(a -> tupled(rb, rc).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D> Random<Tuple4<A, B, C, D>> tupled(Random<A> ra, Random<B> rb, Random<C> rc, Random<D> rd) {
        return ra.flatMap(a -> tupled(rb, rc, rd).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E> Random<Tuple5<A, B, C, D, E>> tupled(Random<A> ra, Random<B> rb, Random<C> rc,
                                                                       Random<D> rd, Random<E> re) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F> Random<Tuple6<A, B, C, D, E, F>> tupled(Random<A> ra, Random<B> rb, Random<C> rc,
                                                                             Random<D> rd, Random<E> re, Random<F> rf) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F, G> Random<Tuple7<A, B, C, D, E, F, G>> tupled(Random<A> ra, Random<B> rb, Random<C> rc,
                                                                                   Random<D> rd, Random<E> re, Random<F> rf,
                                                                                   Random<G> rg) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf, rg).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F, G, H> Random<Tuple8<A, B, C, D, E, F, G, H>> tupled(Random<A> ra, Random<B> rb, Random<C> rc,
                                                                                         Random<D> rd, Random<E> re, Random<F> rf,
                                                                                         Random<G> rg, Random<H> rh) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf, rg, rh).fmap(x -> x.cons(a)));
    }

}
