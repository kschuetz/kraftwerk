package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;

public interface ShrinkNode<A> extends Functor<A, ShrinkNode<?>> {
    A getValue();

    ImmutableFiniteIterable<ShrinkNode<A>> shrinkThisValue();

    @Override
    default <B> ShrinkNode<B> fmap(Fn1<? super A, ? extends B> fn) {
        ShrinkNode<A> parent = this;
        return new ShrinkNode<B>() {
            @Override
            public B getValue() {
                return fn.apply(parent.getValue());
            }

            @Override
            public ImmutableFiniteIterable<ShrinkNode<B>> shrinkThisValue() {
                return parent.shrinkThisValue().fmap(node -> node.fmap(fn));
            }
        };
    }

    static <A> ShrinkNode<A> shrinkNode(A value) {
        return new ShrinkNode<A>() {
            @Override
            public A getValue() {
                return value;
            }

            @Override
            public ImmutableFiniteIterable<ShrinkNode<A>> shrinkThisValue() {
                return emptyImmutableIterable();
            }
        };
    }

}
