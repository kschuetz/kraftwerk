package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.Generator;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

public class Tuples {

    public static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> ra, Generator<B> rb) {
        return ra.flatMap(a -> rb.fmap(b -> tuple(a, b)));
    }

    public static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> ra, Generator<B> rb, Generator<C> rc) {
        return ra.flatMap(a -> tupled(rb, rc).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> ra, Generator<B> rb, Generator<C> rc, Generator<D> rd) {
        return ra.flatMap(a -> tupled(rb, rc, rd).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> ra, Generator<B> rb, Generator<C> rc,
                                                                          Generator<D> rd, Generator<E> re) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> ra, Generator<B> rb, Generator<C> rc,
                                                                                Generator<D> rd, Generator<E> re, Generator<F> rf) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> ra, Generator<B> rb, Generator<C> rc,
                                                                                      Generator<D> rd, Generator<E> re, Generator<F> rf,
                                                                                      Generator<G> rg) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf, rg).fmap(x -> x.cons(a)));
    }

    public static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> ra, Generator<B> rb, Generator<C> rc,
                                                                                            Generator<D> rd, Generator<E> re, Generator<F> rf,
                                                                                            Generator<G> rg, Generator<H> rh) {
        return ra.flatMap(a -> tupled(rb, rc, rd, re, rf, rg, rh).fmap(x -> x.cons(a)));
    }

}
