package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;

import static dev.marksman.composablerandom.Result.result;

public interface CompiledGenerator<A> extends Monad<A, CompiledGenerator<?>> {
    Result<? extends RandomState, A> run(RandomState input);

    default Fn1<RandomState, Result<? extends RandomState, A>> getRun() {
        return this::run;
    }

    @Override
    default <B> CompiledGenerator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return compiledGenerator(getRun().fmap(a -> a.fmap(fn)));
    }

    @Override
    default <B> CompiledGenerator<B> flatMap(Fn1<? super A, ? extends Monad<B, CompiledGenerator<?>>> fn) {
        return compiledGenerator(rs -> {
            Result<? extends RandomState, A> x = run(rs);
            return ((CompiledGenerator<B>) fn.apply(x._2())).run(x._1());
        });
    }

    @Override
    default <B> CompiledGenerator<B> pure(B b) {
        return compiledGenerator(rs -> result(rs, b));
    }

    static <A> CompiledGenerator<A> compiledGenerator(Fn1<RandomState, Result<? extends RandomState, A>> fn) {
        return new CompiledGenerator<A>() {
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
