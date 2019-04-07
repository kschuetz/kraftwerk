package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.Generator;

class Tuples {

    static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> ga, Generator<B> gb) {
        return Generator.tupled(ga, gb);
    }

    static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc) {
        return Generator.tupled(ga, gb, gc);
    }

    static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc, Generator<D> gd) {
        return Generator.tupled(ga, gb, gc, gd);
    }

    static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                   Generator<D> gd, Generator<E> ge) {
        return Generator.tupled(ga, gb, gc, gd, ge);
    }

    static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                         Generator<D> gd, Generator<E> ge, Generator<F> gf) {
        return Generator.tupled(ga, gb, gc, gd, ge, gf);
    }

    static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                               Generator<D> gd, Generator<E> ge, Generator<F> gf,
                                                                               Generator<G> gg) {
        return Generator.tupled(ga, gb, gc, gd, ge, gf, gg);
    }

    static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                                     Generator<D> gd, Generator<E> ge, Generator<F> gf,
                                                                                     Generator<G> gg, Generator<H> gh) {
        return Generator.tupled(ga, gb, gc, gd, ge, gf, gg, gh);
    }

}

