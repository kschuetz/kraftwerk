package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.product.*;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Contravariant;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

public abstract class Cogenerator<A> implements Contravariant<A, Cogenerator<?>> {

    private Cogenerator() {

    }

    public abstract RandomState apply(RandomState randomState, A value);

    public final <B> Cogenerator<B> contraMap(Fn1<? super B, ? extends A> fn) {
        return new Cogenerator<B>() {
            @Override
            public RandomState apply(RandomState randomState, B value) {
                return Cogenerator.this.apply(randomState, fn.apply(value));
            }
        };
    }

    public static <A> Cogenerator<A> cogenerator(Fn1<A, Long> f) {
        return new Cogenerator<A>() {
            @Override
            public RandomState apply(RandomState randomState, A value) {
                return randomState.perturb(f.apply(value));
            }
        };
    }

    public static <A> Cogenerator<A> cogenerator(Fn2<RandomState, A, RandomState> f) {
        return new Cogenerator<A>() {
            @Override
            public RandomState apply(RandomState randomState, A value) {
                return f.apply(randomState, value);
            }
        };
    }

    public static Cogenerator<Integer> cogeneratorInt() {
        return cogenerator(n -> (long) n);
    }

    public static Cogenerator<Long> cogeneratorLong() {
        return cogenerator(id());
    }

    public static Cogenerator<Short> cogeneratorShort() {
        return cogenerator(n -> (long) n);
    }

    public static Cogenerator<Byte> cogeneratorByte() {
        return cogenerator(n -> (long) n);
    }

    public static Cogenerator<Boolean> cogeneratorBoolean() {
        return cogenerator(b -> b ? ~0L : 0L);
    }

    public static Cogenerator<Object> cogeneratorObject() {
        return cogenerator(obj -> obj == null ? 0L : obj.hashCode());
    }

    public static <A> Cogenerator<Maybe<A>> maybe(Cogenerator<A> a) {
        return cogenerator((randomState, value) ->
                value.match(__ -> randomState,
                        x -> a.apply(randomState, x)));
    }

    public static <A, B> Cogenerator<Product2<A, B>> product(Cogenerator<A> a,
                                                             Cogenerator<B> b) {
        return cogenerator((randomState, value) -> b.apply(
                a.apply(randomState, value._1()),
                value._2()));
    }

    public static <A, B, C> Cogenerator<Product3<A, B, C>> product(Cogenerator<A> a,
                                                                   Cogenerator<B> b,
                                                                   Cogenerator<C> c) {
        return cogenerator((randomState, value) -> c.apply(
                b.apply(
                        a.apply(randomState, value._1()),
                        value._2()),
                value._3()));
    }

    public static <A, B, C, D> Cogenerator<Product4<A, B, C, D>> product(Cogenerator<A> a,
                                                                         Cogenerator<B> b,
                                                                         Cogenerator<C> c,
                                                                         Cogenerator<D> d) {
        return cogenerator((randomState, value) -> d.apply(
                c.apply(
                        b.apply(
                                a.apply(randomState, value._1()),
                                value._2()),
                        value._3()),
                value._4()));
    }

    public static <A, B, C, D, E> Cogenerator<Product5<A, B, C, D, E>> product(Cogenerator<A> a,
                                                                               Cogenerator<B> b,
                                                                               Cogenerator<C> c,
                                                                               Cogenerator<D> d,
                                                                               Cogenerator<E> e) {
        return cogenerator((randomState, value) -> e.apply(
                d.apply(
                        c.apply(
                                b.apply(
                                        a.apply(randomState, value._1()),
                                        value._2()),
                                value._3()),
                        value._4()),
                value._5()));
    }

    public static <A, B, C, D, E, F> Cogenerator<Product6<A, B, C, D, E, F>> product(Cogenerator<A> a,
                                                                                     Cogenerator<B> b,
                                                                                     Cogenerator<C> c,
                                                                                     Cogenerator<D> d,
                                                                                     Cogenerator<E> e,
                                                                                     Cogenerator<F> f) {
        return cogenerator((randomState, value) -> f.apply(
                e.apply(
                        d.apply(
                                c.apply(
                                        b.apply(
                                                a.apply(randomState, value._1()),
                                                value._2()),
                                        value._3()),
                                value._4()),
                        value._5()),
                value._6()));
    }

    public static <A, B, C, D, E, F, G> Cogenerator<Product7<A, B, C, D, E, F, G>> product(Cogenerator<A> a,
                                                                                           Cogenerator<B> b,
                                                                                           Cogenerator<C> c,
                                                                                           Cogenerator<D> d,
                                                                                           Cogenerator<E> e,
                                                                                           Cogenerator<F> f,
                                                                                           Cogenerator<G> g) {
        return cogenerator((randomState, value) -> g.apply(
                f.apply(
                        e.apply(
                                d.apply(
                                        c.apply(
                                                b.apply(
                                                        a.apply(randomState, value._1()),
                                                        value._2()),
                                                value._3()),
                                        value._4()),
                                value._5()),
                        value._6()),
                value._7()));
    }

    public static <A, B, C, D, E, F, G, H> Cogenerator<Product8<A, B, C, D, E, F, G, H>> product(Cogenerator<A> a,
                                                                                                 Cogenerator<B> b,
                                                                                                 Cogenerator<C> c,
                                                                                                 Cogenerator<D> d,
                                                                                                 Cogenerator<E> e,
                                                                                                 Cogenerator<F> f,
                                                                                                 Cogenerator<G> g,
                                                                                                 Cogenerator<H> h) {
        return cogenerator((randomState, value) -> h.apply(
                g.apply(
                        f.apply(
                                e.apply(
                                        d.apply(
                                                c.apply(
                                                        b.apply(
                                                                a.apply(randomState, value._1()),
                                                                value._2()),
                                                        value._3()),
                                                value._4()),
                                        value._5()),
                                value._6()),
                        value._7()),
                value._8()));
    }

}
