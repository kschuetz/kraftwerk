package dev.marksman.nonempty;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;

public interface NonEmptyVector<A> extends NonEmptyCollection<A>, Functor<A, NonEmptyVector<?>> {
    int size();

    A get(int index);

    default NonEmptyVector<A> concat(Iterable<A> other) {
        if (other instanceof NonEmptyVector<?>) {
            return ConcatVectors.concatVectors(this, (NonEmptyVector<A>) other);
        } else {
            return null;
        }
    }

    @Override
    default <B> NonEmptyVector<B> fmap(Function<? super A, ? extends B> fn) {
        return null;
    }

    @Override
    default Iterable<A> getTail() {
        return drop(1, this);
    }

    static <A> NonEmptyVector<A> nonEmptyVector(int size, Function<Integer, A> fn) {
        return null;
    }
}
