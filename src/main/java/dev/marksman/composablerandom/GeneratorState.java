package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.Monad;

import static dev.marksman.composablerandom.Result.result;

public interface GeneratorState<A> extends Monad<A, GeneratorState<?>> {
    Result<? extends Seed, A> run(Seed input);

    default Fn1<Seed, Result<? extends Seed, A>> getRun() {
        return this::run;
    }

    @Override
    default <B> GeneratorState<B> fmap(Fn1<? super A, ? extends B> fn) {
        return generator(getRun().fmap(a -> a.fmap(fn)));
    }

    @Override
    default <B> GeneratorState<B> flatMap(Fn1<? super A, ? extends Monad<B, GeneratorState<?>>> fn) {
        return generator(rs -> {
            Result<? extends Seed, A> x = run(rs);
            return ((GeneratorState<B>) fn.apply(x._2())).run(x._1());
        });
    }

    @Override
    default <B> GeneratorState<B> pure(B b) {
        return generator(rs -> result(rs, b));
    }

    static <A> GeneratorState<A> generator(Fn1<Seed, Result<? extends Seed, A>> fn) {
        return new GeneratorState<A>() {
            @Override
            public Result<? extends Seed, A> run(Seed input) {
                return fn.apply(input);
            }

            @Override
            public Fn1<Seed, Result<? extends Seed, A>> getRun() {
                return fn;
            }
        };
    }

}
