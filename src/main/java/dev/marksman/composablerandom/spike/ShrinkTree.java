package dev.marksman.composablerandom.spike;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.enhancediterables.ImmutableIterable.emptyImmutableIterable;
import static dev.marksman.shrink.util.LazyCons.lazyCons;

public interface ShrinkTree<A> {
    A getValue();

    default Maybe<ShrinkTree<A>> getLeft() {
        return nothing();
    }

    default Maybe<ShrinkTree<A>> getRight() {
        return nothing();
    }

    default ImmutableFiniteIterable<A> getHappyPath() {
        return getRight()
                .match(__ -> emptyImmutableIterable(),
                        st -> lazyCons(st.getValue(), st::getHappyPath));
    }

}
