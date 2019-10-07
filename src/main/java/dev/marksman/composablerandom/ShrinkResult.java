package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShrinkResult<A> implements Functor<A, ShrinkResult<?>> {
    private final A value;

    private final ImmutableIterable<A> shrinkValues;

    @Override
    public <B> ShrinkResult<B> fmap(Fn1<? super A, ? extends B> fn) {
        return shrinkResult(fn.apply(value), shrinkValues.fmap(fn));
    }

    public static <A> ShrinkResult<A> shrinkResult(A value, ImmutableIterable<A> shrinkValues) {
        return new ShrinkResult<>(value, shrinkValues);
    }

    public static <A> ShrinkResult<A> shrinkResult(A value) {
        return new ShrinkResult<>(value, Vector.empty());
    }
}
