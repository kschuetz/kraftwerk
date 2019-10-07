package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;

public interface GeneratorState<A> {
    Result<? extends Seed, A> run(Seed input);

    default Result<? extends Seed, ShrinkResult<A>> runShrink(Seed input) {
        return this.getRun().apply(input).fmap(ShrinkResult::shrinkResult);
    }

    default Fn1<Seed, Result<? extends Seed, A>> getRun() {
        return this::run;
    }

    default Fn1<Seed, Result<? extends Seed, ShrinkResult<A>>> getRunShrink() {
        return this::runShrink;
    }

    static <A> GeneratorState<A> generator(Fn1<Seed, Result<? extends Seed, A>> fn) {
        Fn1<Seed, Result<? extends Seed, ShrinkResult<A>>> shrinkFn = fn.fmap(x -> x.fmap(ShrinkResult::shrinkResult));
        return new GeneratorState<A>() {
            @Override
            public Result<? extends Seed, A> run(Seed input) {
                return fn.apply(input);
            }

            @Override
            public Result<? extends Seed, ShrinkResult<A>> runShrink(Seed input) {
                return null;
            }

            @Override
            public Fn1<Seed, Result<? extends Seed, A>> getRun() {
                return fn;
            }

            @Override
            public Fn1<Seed, Result<? extends Seed, ShrinkResult<A>>> getRunShrink() {
                return shrinkFn;
            }
        };
    }

}
