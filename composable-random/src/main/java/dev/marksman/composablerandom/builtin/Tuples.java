package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.Generator;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

class Tuples {

    static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> ga, Generator<B> gb) {
        return ga.flatMap(a -> gb.fmap(b -> tuple(a, b)));
    }

    static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc) {
        return ga.flatMap(a -> tupled(gb, gc).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc, Generator<D> gd) {
        return ga.flatMap(a -> tupled(gb, gc, gd).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                   Generator<D> gd, Generator<E> ge) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                         Generator<D> gd, Generator<E> ge, Generator<F> gf) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge, gf).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                               Generator<D> gd, Generator<E> ge, Generator<F> gf,
                                                                               Generator<G> gg) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge, gf, gg).fmap(x -> x.cons(a)));
    }

    static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                                     Generator<D> gd, Generator<E> ge, Generator<F> gf,
                                                                                     Generator<G> gg, Generator<H> gh) {
        return ga.flatMap(a -> tupled(gb, gc, gd, ge, gf, gg, gh).fmap(x -> x.cons(a)));
    }

}

