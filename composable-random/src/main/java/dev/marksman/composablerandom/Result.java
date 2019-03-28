package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<S, A> implements Product2<S, A>, Functor<A, Result>, Bifunctor<S, A, Result> {
    private final S nextState;
    private final A value;

    @Override
    public S _1() {
        return nextState;
    }

    @Override
    public A _2() {
        return value;
    }

    public Result<S, A> withNextState(S newNextState) {
        return result(newNextState, value);
    }

    public Result<S, A> withValue(A newValue) {
        return result(nextState, newValue);
    }

    @Override
    public <B> Result<S, B> fmap(Function<? super A, ? extends B> fn) {
        return result(nextState, fn.apply(value));
    }

    @Override
    public <C, D> Result<C, D> biMap(Function<? super S, ? extends C> lFn, Function<? super A, ? extends D> rFn) {
        return result(lFn.apply(nextState), rFn.apply(value));
    }

    public static <S, A> Result<S, A> result(S nextState, A value) {
        return new Result<>(nextState, value);
    }

    public static <S, A> Result<S, A> result(Product2<S, A> p) {
        return new Result<>(p._1(), p._2());
    }

}
