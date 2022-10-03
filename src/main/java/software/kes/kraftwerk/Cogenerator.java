package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.adt.product.Product7;
import com.jnape.palatable.lambda.adt.product.Product8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Contravariant;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A strategy for applying a value to a {@link Seed} to create a new {@code Seed}. The dual to a {@link Generator}.
 * <p>
 * {@code Cogenerator}s are used to build {@code Generator}s of functions.  A {@code Cogenerator} is required for
 * each formal parameter of the function.
 * <p>
 * {@code Cogenerator}s must be pure, referentially transparent, and total.  In other words, they must return a {@code Seed}
 * for any input {@code Seed} and any value of type {@code A}, and it must return the same output {@code Seed} for subsequent
 * calls with the same inputs.
 *
 * @param <A> the input type
 */
public abstract class Cogenerator<A> implements Contravariant<A, Cogenerator<?>> {
    private Cogenerator() {
    }

    /**
     * Creates a {@code Cogenerator}.
     *
     * @param f   a function that is given and {@code A} and returns a {@link Long}; must be pure, referentially transparent, and total
     * @param <A> the input type
     * @return a {@code Cogenerator<A>}
     */
    public static <A> Cogenerator<A> cogenerator(Fn1<A, Long> f) {
        return new Cogenerator<A>() {
            @Override
            public Seed apply(Seed seed, A value) {
                return seed.perturb(f.apply(value));
            }
        };
    }

    /**
     * Creates a {@code Cogenerator}.
     *
     * @param f   a function that is given a {@link Seed} and an {@code A} that returns a new {@code Seed};
     *            must be pure, referentially transparent, and total
     * @param <A> the input type
     * @return a {@code Cogenerator<A>}
     */
    public static <A> Cogenerator<A> cogenerator(Fn2<Seed, A, Seed> f) {
        return new Cogenerator<A>() {
            @Override
            public Seed apply(Seed seed, A value) {
                return f.apply(seed, value);
            }
        };
    }

    /**
     * Creates a {@code Cogenerator} for {@link Integer}s.
     *
     * @return a {@code Cogenerator<Integer>}
     */
    public static Cogenerator<Integer> cogeneratorInt() {
        return cogenerator(n -> (long) n);
    }

    /**
     * Creates a {@code Cogenerator} for {@link Long}s.
     *
     * @return a {@code Cogenerator<Long>}
     */
    public static Cogenerator<Long> cogeneratorLong() {
        return cogenerator(id());
    }

    /**
     * Creates a {@code Cogenerator} for {@link Short}s.
     *
     * @return a {@code Cogenerator<Short>}
     */
    public static Cogenerator<Short> cogeneratorShort() {
        return cogenerator(n -> (long) n);
    }

    /**
     * Creates a {@code Cogenerator} for {@link Byte}s.
     *
     * @return a {@code Cogenerator<Byte>}
     */
    public static Cogenerator<Byte> cogeneratorByte() {
        return cogenerator(n -> (long) n);
    }

    /**
     * Creates a {@code Cogenerator} for {@link Boolean}s.
     *
     * @return a {@code Cogenerator<Boolean>}
     */
    public static Cogenerator<Boolean> cogeneratorBoolean() {
        return cogenerator(b -> b ? ~0L : 0L);
    }

    /**
     * Creates a {@code Cogenerator} for {@link Object}s.
     *
     * @return a {@code Cogenerator<Object>}
     */
    public static Cogenerator<Object> cogeneratorObject() {
        return cogenerator(obj -> (long) Objects.hashCode(obj));
    }

    /**
     * Creates a {@code Cogenerator} for {@link String}s.
     *
     * @return a {@code Cogenerator<String>}
     */
    public static Cogenerator<String> cogeneratorString() {
        return cogenerator(s -> (long) Objects.hashCode(s));
    }

    /**
     * Converts a {@code Cogenerator<A>} to a {@code Cogenerator<Maybe<A>>}
     *
     * @param a   a {@code Cogenerator<A>}
     * @param <A> the input type
     * @return a {@code Cogenerator<Maybe<A>>}
     */
    public static <A> Cogenerator<Maybe<A>> maybe(Cogenerator<A> a) {
        return cogenerator((seed, value) ->
                value.match(__ -> seed,
                        x -> a.apply(seed, x)));
    }

    /**
     * Creates a {@code Cogenerator<Product2>>} by combining two {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @return a {@code Cogenerator<Product2<A, B>>}
     */
    public static <A, B> Cogenerator<Product2<A, B>> product(Cogenerator<A> a,
                                                             Cogenerator<B> b) {
        return cogenerator((seed, value) -> b.apply(
                a.apply(seed, value._1()),
                value._2()));
    }

    /**
     * Creates a {@code Cogenerator<Product3>>} by combining three {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param c   the third {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @param <C> the third input type
     * @return a {@code Cogenerator<Product3<A, B, C>>}
     */
    public static <A, B, C> Cogenerator<Product3<A, B, C>> product(Cogenerator<A> a,
                                                                   Cogenerator<B> b,
                                                                   Cogenerator<C> c) {
        return cogenerator((seed, value) -> c.apply(
                b.apply(
                        a.apply(seed, value._1()),
                        value._2()),
                value._3()));
    }

    /**
     * Creates a {@code Cogenerator<Product4>>} by combining four {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param c   the third {@code Cogenerator}
     * @param d   the fourth {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @param <C> the third input type
     * @param <D> the fourth input type
     * @return a {@code Cogenerator<Product4<A, B, C, D>>}
     */
    public static <A, B, C, D> Cogenerator<Product4<A, B, C, D>> product(Cogenerator<A> a,
                                                                         Cogenerator<B> b,
                                                                         Cogenerator<C> c,
                                                                         Cogenerator<D> d) {
        return cogenerator((seed, value) -> d.apply(
                c.apply(
                        b.apply(
                                a.apply(seed, value._1()),
                                value._2()),
                        value._3()),
                value._4()));
    }

    /**
     * Creates a {@code Cogenerator<Product5>>} by combining five {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param c   the third {@code Cogenerator}
     * @param d   the fourth {@code Cogenerator}
     * @param e   the fifth {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @param <C> the third input type
     * @param <D> the fourth input type
     * @param <E> the fifth input type
     * @return a {@code Cogenerator<Product5<A, B, C, D, E>>}
     */
    public static <A, B, C, D, E> Cogenerator<Product5<A, B, C, D, E>> product(Cogenerator<A> a,
                                                                               Cogenerator<B> b,
                                                                               Cogenerator<C> c,
                                                                               Cogenerator<D> d,
                                                                               Cogenerator<E> e) {
        return cogenerator((seed, value) -> e.apply(
                d.apply(
                        c.apply(
                                b.apply(
                                        a.apply(seed, value._1()),
                                        value._2()),
                                value._3()),
                        value._4()),
                value._5()));
    }

    /**
     * Creates a {@code Cogenerator<Product6>>} by combining six {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param c   the third {@code Cogenerator}
     * @param d   the fourth {@code Cogenerator}
     * @param e   the fifth {@code Cogenerator}
     * @param f   the sixth {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @param <C> the third input type
     * @param <D> the fourth input type
     * @param <E> the fifth input type
     * @param <F> the sixth input type
     * @return a {@code Cogenerator<Product6<A, B, C, D, E, F>>}
     */
    public static <A, B, C, D, E, F> Cogenerator<Product6<A, B, C, D, E, F>> product(Cogenerator<A> a,
                                                                                     Cogenerator<B> b,
                                                                                     Cogenerator<C> c,
                                                                                     Cogenerator<D> d,
                                                                                     Cogenerator<E> e,
                                                                                     Cogenerator<F> f) {
        return cogenerator((seed, value) -> f.apply(
                e.apply(
                        d.apply(
                                c.apply(
                                        b.apply(
                                                a.apply(seed, value._1()),
                                                value._2()),
                                        value._3()),
                                value._4()),
                        value._5()),
                value._6()));
    }

    /**
     * Creates a {@code Cogenerator<Product7>>} by combining seven {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param c   the third {@code Cogenerator}
     * @param d   the fourth {@code Cogenerator}
     * @param e   the fifth {@code Cogenerator}
     * @param f   the sixth {@code Cogenerator}
     * @param g   the seventh {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @param <C> the third input type
     * @param <D> the fourth input type
     * @param <E> the fifth input type
     * @param <F> the sixth input type
     * @param <G> the seventh input type
     * @return a {@code Cogenerator<Product7<A, B, C, D, E, F, G>>}
     */
    public static <A, B, C, D, E, F, G> Cogenerator<Product7<A, B, C, D, E, F, G>> product(Cogenerator<A> a,
                                                                                           Cogenerator<B> b,
                                                                                           Cogenerator<C> c,
                                                                                           Cogenerator<D> d,
                                                                                           Cogenerator<E> e,
                                                                                           Cogenerator<F> f,
                                                                                           Cogenerator<G> g) {
        return cogenerator((seed, value) -> g.apply(
                f.apply(
                        e.apply(
                                d.apply(
                                        c.apply(
                                                b.apply(
                                                        a.apply(seed, value._1()),
                                                        value._2()),
                                                value._3()),
                                        value._4()),
                                value._5()),
                        value._6()),
                value._7()));
    }

    /**
     * Creates a {@code Cogenerator<Product8>>} by combining eight {@code Cogenerator}s.
     *
     * @param a   the first {@code Cogenerator}
     * @param b   the second {@code Cogenerator}
     * @param c   the third {@code Cogenerator}
     * @param d   the fourth {@code Cogenerator}
     * @param e   the fifth {@code Cogenerator}
     * @param f   the sixth {@code Cogenerator}
     * @param g   the seventh {@code Cogenerator}
     * @param h   the eighth {@code Cogenerator}
     * @param <A> the first input type
     * @param <B> the second input type
     * @param <C> the third input type
     * @param <D> the fourth input type
     * @param <E> the fifth input type
     * @param <F> the sixth input type
     * @param <G> the seventh input type
     * @param <H> the eighth input type
     * @return a {@code Cogenerator<Product8<A, B, C, D, E, F, G, H>>}
     */
    public static <A, B, C, D, E, F, G, H> Cogenerator<Product8<A, B, C, D, E, F, G, H>> product(Cogenerator<A> a,
                                                                                                 Cogenerator<B> b,
                                                                                                 Cogenerator<C> c,
                                                                                                 Cogenerator<D> d,
                                                                                                 Cogenerator<E> e,
                                                                                                 Cogenerator<F> f,
                                                                                                 Cogenerator<G> g,
                                                                                                 Cogenerator<H> h) {
        return cogenerator((seed, value) -> h.apply(
                g.apply(
                        f.apply(
                                e.apply(
                                        d.apply(
                                                c.apply(
                                                        b.apply(
                                                                a.apply(seed, value._1()),
                                                                value._2()),
                                                        value._3()),
                                                value._4()),
                                        value._5()),
                                value._6()),
                        value._7()),
                value._8()));
    }

    /**
     * The implementation of the {@code Cogenerator}.  Converts a {@link Seed} and a value of type {@code A} to
     * a new {@code Seed}
     *
     * @param seed  the input {@code Seed}
     * @param value the value that will be used to perturb the input {@code Seed}
     * @return a {@code Seed}
     */
    public abstract Seed apply(Seed seed, A value);

    /**
     * Converts this {@code Cogenerator} to a {@code Cogenerator} of a possibly different type by mapping the
     * input through a function.
     *
     * @param fn  the mapping function
     * @param <B> the new input type
     * @return a {@code Cogenerator<B>}
     */
    public final <B> Cogenerator<B> contraMap(Fn1<? super B, ? extends A> fn) {
        return new Cogenerator<B>() {
            @Override
            public Seed apply(Seed seed, B value) {
                return Cogenerator.this.apply(seed, fn.apply(value));
            }
        };
    }
}
