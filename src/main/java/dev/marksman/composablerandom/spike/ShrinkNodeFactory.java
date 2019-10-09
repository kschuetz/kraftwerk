package dev.marksman.composablerandom.spike;

import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import dev.marksman.shrink.Shrink;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.shrink.util.LazyCons.lazyCons;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShrinkNodeFactory<A> {
    private final Shrink<A> shrink;

    public ImmutableNonEmptyFiniteIterable<ShrinkNode<A>> apply(A value) {
        return lazyCons(build(value),
                () -> shrink.apply(value).fmap(this::build));
    }

    private ShrinkNode<A> build(A value) {
        return new ShrinkNode<A>() {
            @Override
            public A getValue() {
                return value;
            }

            @Override
            public ImmutableFiniteIterable<ShrinkNode<A>> shrinkThisValue() {
                return shrink.apply(value).fmap(ShrinkNodeFactory.this::build);
            }
        };
    }

    public static <A> ShrinkNodeFactory<A> shrinkNodeFactory(Shrink<A> shrink) {
        return new ShrinkNodeFactory<>(shrink);
    }

}
