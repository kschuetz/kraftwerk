package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.Generate;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

public class Tuples {

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

}
