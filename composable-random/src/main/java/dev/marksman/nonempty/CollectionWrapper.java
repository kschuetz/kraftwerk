package dev.marksman.nonempty;

import com.jnape.palatable.lambda.iteration.ImmutableIterator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class CollectionWrapper<A> implements ImmutableCollection<A> {
    private final ArrayList<A> internal;

    @SuppressWarnings("unchecked")
    @Override
    public <B> ImmutableCollection<B> fmap(Function<? super A, ? extends B> fn) {
        return (ImmutableCollection<B>) MappedCollectionWrapper.mappedCollectionWrapper(this, fn::apply);
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internal.contains(o);
    }

    @Override
    public Iterator<A> iterator() {
        Iterator<A> internalIterator = internal.iterator();
        return new ImmutableIterator<A>() {
            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public A next() {
                return internalIterator.next();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return internal.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return internal.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return internal.containsAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        // TODO: handle slices
        if (this == o) return true;
        if (o instanceof CollectionWrapper<?>) {
            CollectionWrapper<A> other = (CollectionWrapper<A>) o;
            return internal.equals(other.internal);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    static <A> CollectionWrapper<A> collectionWrapper(Collection<A> collection) {
        if (collection instanceof CollectionWrapper<?>) {
            return (CollectionWrapper<A>) collection;
        } else {
            return null;
        }
    }
}
