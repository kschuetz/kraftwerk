package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;

import static dev.marksman.composablerandom.Result.result;

public interface Generator<A> extends Monad<A, Generator<?>> {
    Result<? extends RandomState, A> run(RandomState input);

    default Fn1<RandomState, Result<? extends RandomState, A>> getRun() {
        return this::run;
    }

    @Override
    default <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return generator(getRun().fmap(a -> a.fmap(fn)));
    }

    @Override
    default <B> Generator<B> flatMap(Fn1<? super A, ? extends Monad<B, Generator<?>>> fn) {
        return generator(rs -> {
            Result<? extends RandomState, A> x = run(rs);
            return ((Generator<B>) fn.apply(x._2())).run(x._1());
        });
    }

    @Override
    default <B> Generator<B> pure(B b) {
        return generator(rs -> result(rs, b));
    }

    static <A> Generator<A> generator(Fn1<RandomState, Result<? extends RandomState, A>> fn) {
        return new Generator<A>() {
            @Override
            public Result<? extends RandomState, A> run(RandomState input) {
                return fn.apply(input);
            }

            @Override
            public Fn1<RandomState, Result<? extends RandomState, A>> getRun() {
                return fn;
            }
        };
    }

}
