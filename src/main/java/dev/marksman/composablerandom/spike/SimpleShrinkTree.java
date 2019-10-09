package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.shrink.Shrink;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleShrinkTree<A> implements ShrinkTree<A> {
    private final Shrink<A> shrink;
    private final A value;
    private final ImmutableFiniteIterable<A> rightValues;

    @Override
    public A getValue() {
        return value;
    }

    @Override
    public Maybe<ShrinkTree<A>> getRight() {
        return rightValues.toNonEmpty()
                .fmap(xs -> new SimpleShrinkTree<A>(shrink, xs.head(), xs.tail()));
    }

    @Override
    public Maybe<ShrinkTree<A>> getLeft() {
        return shrink.apply(value).toNonEmpty()
                .fmap(xs -> new SimpleShrinkTree<A>(shrink, xs.head(), xs.tail()));
    }

    @Override
    public ImmutableFiniteIterable<A> getHappyPath() {
        return rightValues;
    }

    public static <A> SimpleShrinkTree<A> simpleShrinkTree(Shrink<A> shrink, A initialValue) {
        return new SimpleShrinkTree<>(shrink, initialValue, shrink.apply(initialValue));
    }

}
