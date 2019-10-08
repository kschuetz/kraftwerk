package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;

public interface GeneratorState<A> {
    Result<? extends Seed, A> run(Seed input);

    default Fn1<Seed, Result<? extends Seed, A>> getRun() {
        return this::run;
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
