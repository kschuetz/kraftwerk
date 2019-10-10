package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;
import static dev.marksman.shrink.util.LazyCons.lazyCons;

public interface ShrinkTree<A> extends Functor<A, ShrinkTree<?>> {
    A getValue();

    Maybe<ShrinkTree<A>> getLeft();

    Maybe<ShrinkTree<A>> getRight();

    @Override
    default <B> ShrinkTree<B> fmap(Fn1<? super A, ? extends B> fn) {
        ShrinkTree<A> self = this;
        return new ShrinkTree<B>() {
            @Override
            public B getValue() {
                return fn.apply(self.getValue());
            }

            @Override
            public Maybe<ShrinkTree<B>> getLeft() {
                return self.getLeft().fmap(st -> st.fmap(fn));
            }

            @Override
            public Maybe<ShrinkTree<B>> getRight() {
                return self.getRight().fmap(st -> st.fmap(fn));
            }
        };
    }

    default ImmutableFiniteIterable<A> getHappyPath() {
        return getRight()
                .match(__ -> emptyImmutableIterable(),
                        st -> lazyCons(st.getValue(), st::getHappyPath));
    }

    static <A> ShrinkTree<A> noShrink(A value) {
        return new ShrinkTree<A>() {
            @Override
            public A getValue() {
                return value;
            }

            @Override
            public Maybe<ShrinkTree<A>> getLeft() {
                return nothing();
            }

            @Override
            public Maybe<ShrinkTree<A>> getRight() {
                return nothing();
            }
        };
    }
}
