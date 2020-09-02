package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * A pure and referentially transparent function that converts a {@link Seed} to a {@link Result}.
 * The same {@code Seed} will always yield the same {@code Result}, and all {@code Seed}s
 * are guaranteed to return a result.
 *
 * @param <A> the output type
 */
@FunctionalInterface
public interface GenerateFn<A> extends Fn1<Seed, Result<? extends Seed, A>> {
}
