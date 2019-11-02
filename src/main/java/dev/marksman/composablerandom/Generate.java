package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;

@FunctionalInterface
public interface Generate<A> extends Fn1<Seed, Result<? extends Seed, A>> {
}
