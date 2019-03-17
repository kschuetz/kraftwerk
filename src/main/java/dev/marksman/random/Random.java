package dev.marksman.random;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Filter;
import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
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

    private final Function<RandomGen, Product2<A, ? extends RandomGen>> run;

    public final Product2<A, ? extends RandomGen> run(RandomGen randomGen) {
        return run.apply(randomGen);
    }

    @Override
    public final <B> Random<B> fmap(Function<? super A, ? extends B> fn) {
        return new Random<>(rg0 -> {
            Product2<A, ? extends RandomGen> x = run.apply(rg0);
            return product(fn.apply(x._1()), x._2());
        });
    }

    @Override
    public final <B> Random<B> flatMap(Function<? super A, ? extends Monad<B, Random>> fn) {
        return random(rg0 -> {
            Product2<A, ? extends RandomGen> x = run.apply(rg0);
            return ((Random<B>) fn.apply(x._1())).run.apply(x._2());
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

    public final Random<Maybe<A>> maybe(int nothingFrequency, int justFrequency) {
        if (nothingFrequency < 0) {
            throw new IllegalArgumentException("nothingFrequency must be non-negative");
        }
        if (justFrequency < 0) {
            throw new IllegalArgumentException("justFrequency must be non-negative");
        }
        int total = nothingFrequency + justFrequency;
        if (total == 0) {
            throw new IllegalArgumentException("sum of nothingFrequency and justFrequency must be > 0");
        }
        if (nothingFrequency == 0) {
            return this.fmap(Maybe::just);
        } else if (justFrequency == 0) {
            return constant(nothing());
        } else {
            return randomInt(total).flatMap(n -> {
                if (n < nothingFrequency) return constant(nothing());
                else return this.fmap(Maybe::just);
            });
        }
    }

    public final Random<Maybe<A>> maybe(int justFrequency) {
        return maybe(1, justFrequency);
    }

    public final Random<Maybe<A>> maybe() {
        return maybe(1, 9);
    }

    public static <A> Random<A> random(Function<RandomGen, Product2<A, ? extends RandomGen>> run) {
        return new Random<>(run);
    }

    public static <A> Random<A> constant(A a) {
        return random(rg -> product(a, rg));
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

    public static Random<Float> randomFloat() {
        return RANDOM_FLOAT;
    }

    public static Random<Integer> randomInt(int bound) {
        return random(s -> s.nextInt(bound));
    }

    public static Random<Long> randomLong() {
        return RANDOM_LONG;
    }

    public static Random<Double> randomGaussian() {
        return RANDOM_GAUSSIAN;
    }

    public static Random<Byte[]> randomBytes(int count) {
        return random(s -> {
            byte[] buffer = new byte[count];
            Product2<Unit, ? extends RandomGen> next = s.nextBytes(buffer);
            Byte[] result = new Byte[count];
            int i = 0;
            for (byte b : buffer) {
                result[i++] = b;
            }
            return product(result, next._2());
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
        int choices = 1 + more.length;
        if (choices == 1) {
            return constant(first);
        } else {
            return randomInt(choices).fmap(n -> {
                if (n == 0) return first;
                else return more[n - 1];
            });
        }
    }

    @SafeVarargs
    public static <A> Random<A> frequencies(Freq<A> first, Freq<A>... more) {
        // TODO:  frequencies
        Iterable<Freq<A>> fs = Filter.filter(f -> f.getWeight() > 0, cons(first, asList(more)));
        if(!fs.iterator().hasNext()) {
            throw new IllegalArgumentException("no items with positive weights");
        }
        return null;
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
