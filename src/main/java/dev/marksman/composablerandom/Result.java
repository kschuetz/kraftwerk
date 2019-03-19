package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Functor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<R extends RandomGen, A> implements Product2<R, A>, Functor<A, Result> {
    private final R nextRandomGen;
    private final A value;

    @Override
    public R _1() {
        return nextRandomGen;
    }

    @Override
    public A _2() {
        return value;
    }

    @Override
    public <B> Result<R, B> fmap(Function<? super A, ? extends B> fn) {
        return result(nextRandomGen, fn.apply(value));
    }

    public static <R extends RandomGen, A> Result<R, A> result(R nextRandomGen, A value) {
        return new Result<>(nextRandomGen, value);
    }

    public static <R extends RandomGen, A> Result<R, A> result(Product2<R, A> p) {
        return new Result<>(p._1(), p._2());
    }

}
