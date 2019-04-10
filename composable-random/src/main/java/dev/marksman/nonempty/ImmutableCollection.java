package dev.marksman.nonempty;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.Collection;

import static dev.marksman.nonempty.CollectionWrapper.collectionWrapper;

public interface ImmutableCollection<A> extends Collection<A>, Functor<A, ImmutableCollection<?>> {
    @Override
    default boolean add(A a) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean addAll(Collection<? extends A> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void clear() {
        throw new UnsupportedOperationException();
    }

    static <A> ImmutableCollection<A> immutableCollection(Collection<A> coll) {
        return collectionWrapper(coll);
    }
}
