package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.OldGenerator;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

class Tuples {

    static <A, B> OldGenerator<Tuple2<A, B>> tupled(OldGenerator<A> ga, OldGenerator<B> gb) {
        return ga.flatMap(a -> gb.fmap(b -> tuple(a, b)));
    }

    static <A, B, C> OldGenerator<Tuple3<A, B, C>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc) {
        return ga.flatMap(a -> tupled(gb, gc).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D> OldGenerator<Tuple4<A, B, C, D>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc, OldGenerator<D> gd) {
        return ga.flatMap(a -> tupled(gb, gc, gd).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E> OldGenerator<Tuple5<A, B, C, D, E>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                      OldGenerator<D> gd, OldGenerator<E> ge) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E, F> OldGenerator<Tuple6<A, B, C, D, E, F>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                            OldGenerator<D> gd, OldGenerator<E> ge, OldGenerator<F> gf) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge, gf).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E, F, G> OldGenerator<Tuple7<A, B, C, D, E, F, G>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                                  OldGenerator<D> gd, OldGenerator<E> ge, OldGenerator<F> gf,
                                                                                  OldGenerator<G> gg) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge, gf, gg).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E, F, G, H> OldGenerator<Tuple8<A, B, C, D, E, F, G, H>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                                        OldGenerator<D> gd, OldGenerator<E> ge, OldGenerator<F> gf,
                                                                                        OldGenerator<G> gg, OldGenerator<H> gh) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge, gf, gg, gh).fmap(x -> x.cons(a)));
    }

}
