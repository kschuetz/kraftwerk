package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.product.*;
import com.jnape.palatable.lambda.functions.*;

import static dev.marksman.kraftwerk.Generator.tap;

class Functions {

    static <R> Generator<Fn0<R>> generateFn0(Generator<R> result) {
        return tap(result, (output, rs) -> () -> output.apply(rs).getValue());
    }

    static <A, R> Generator<Fn1<A, R>> generateFn1(Cogenerator<A> param1,
                                                   Generator<R> result) {
        return tap(result, (output, rs) -> a -> output.apply(param1.apply(rs, a)).getValue());
    }

    static <A, B, R> Generator<Fn2<A, B, R>> generateFn2(Cogenerator<A> param1,
                                                         Cogenerator<B> param2,
                                                         Generator<R> result) {
        Cogenerator<Product2<A, B>> params = Cogenerator.product(param1, param2);
        return Generator.tap(result,
                (output, rs) -> (a, b) -> output.apply(params.apply(rs, Product2.product(a, b))).getValue());
    }

    static <A, B, C, R> Generator<Fn3<A, B, C, R>> generateFn3(Cogenerator<A> param1,
                                                               Cogenerator<B> param2,
                                                               Cogenerator<C> param3,
                                                               Generator<R> result) {
        Cogenerator<Product3<A, B, C>> params = Cogenerator.product(param1, param2, param3);
        return Generator.tap(result,
                (output, rs) -> (a, b, c) -> output.apply(params.apply(rs, Product3.product(a, b, c))).getValue());
    }

    static <A, B, C, D, R> Generator<Fn4<A, B, C, D, R>> generateFn4(Cogenerator<A> param1,
                                                                     Cogenerator<B> param2,
                                                                     Cogenerator<C> param3,
                                                                     Cogenerator<D> param4,
                                                                     Generator<R> result) {
        Cogenerator<Product4<A, B, C, D>> params = Cogenerator.product(param1, param2, param3, param4);
        return Generator.tap(result,
                (output, rs) -> (a, b, c, d) -> output.apply(params.apply(rs, Product4.product(a, b, c, d))).getValue());
    }

    static <A, B, C, D, E, R> Generator<Fn5<A, B, C, D, E, R>> generateFn5(Cogenerator<A> param1,
                                                                           Cogenerator<B> param2,
                                                                           Cogenerator<C> param3,
                                                                           Cogenerator<D> param4,
                                                                           Cogenerator<E> param5,
                                                                           Generator<R> result) {
        Cogenerator<Product5<A, B, C, D, E>> params = Cogenerator.product(param1, param2, param3, param4, param5);
        return Generator.tap(result,
                (output, rs) -> (a, b, c, d, e) -> output.apply(params.apply(rs, Product5.product(a, b, c, d, e))).getValue());
    }

    static <A, B, C, D, E, F, R> Generator<Fn6<A, B, C, D, E, F, R>> generateFn6(Cogenerator<A> param1,
                                                                                 Cogenerator<B> param2,
                                                                                 Cogenerator<C> param3,
                                                                                 Cogenerator<D> param4,
                                                                                 Cogenerator<E> param5,
                                                                                 Cogenerator<F> param6,
                                                                                 Generator<R> result) {
        Cogenerator<Product6<A, B, C, D, E, F>> params = Cogenerator.product(param1, param2, param3, param4, param5, param6);
        return Generator.tap(result,
                (output, rs) -> (a, b, c, d, e, f) ->
                        output.apply(params.apply(rs, Product6.product(a, b, c, d, e, f))).getValue());
    }

    static <A, B, C, D, E, F, G, R> Generator<Fn7<A, B, C, D, E, F, G, R>> generateFn7(Cogenerator<A> param1,
                                                                                       Cogenerator<B> param2,
                                                                                       Cogenerator<C> param3,
                                                                                       Cogenerator<D> param4,
                                                                                       Cogenerator<E> param5,
                                                                                       Cogenerator<F> param6,
                                                                                       Cogenerator<G> param7,
                                                                                       Generator<R> result) {
        Cogenerator<Product7<A, B, C, D, E, F, G>> params = Cogenerator.product(param1, param2, param3, param4, param5,
                param6, param7);
        return Generator.tap(result,
                (output, rs) -> (a, b, c, d, e, f, g) ->
                        output.apply(params.apply(rs, Product7.product(a, b, c, d, e, f, g))).getValue());
    }

    static <A, B, C, D, E, F, G, H, R> Generator<Fn8<A, B, C, D, E, F, G, H, R>> generateFn8(Cogenerator<A> param1,
                                                                                             Cogenerator<B> param2,
                                                                                             Cogenerator<C> param3,
                                                                                             Cogenerator<D> param4,
                                                                                             Cogenerator<E> param5,
                                                                                             Cogenerator<F> param6,
                                                                                             Cogenerator<G> param7,
                                                                                             Cogenerator<H> param8,
                                                                                             Generator<R> result) {
        Cogenerator<Product8<A, B, C, D, E, F, G, H>> params = Cogenerator.product(param1, param2, param3, param4, param5,
                param6, param7, param8);
        return Generator.tap(result,
                (output, rs) -> (a, b, c, d, e, f, g, h) ->
                        output.apply(params.apply(rs, Product8.product(a, b, c, d, e, f, g, h))).getValue());
    }

}
