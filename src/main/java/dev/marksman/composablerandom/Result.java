package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.product.Product2;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.product.Product2.product;

class Result {

    static <A, B, R> Product2<R, B> mapResult(Function<A, ? extends B> fn, Product2<R, ? extends A> p) {
        return product(p._1(), fn.apply(p._2()));
    }

}
