package dev.marksman.nonempty;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.iteration.ImmutableIterator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class MappedCollectionWrapper<Source, Elem> implements ImmutableCollection<Elem> {
    private final ImmutableCollection<Source> source;
    private final Fn1<Source, Elem> mapFn;

    @Override
    public <C> MappedCollectionWrapper<Source, C> fmap(Function<? super Elem, ? extends C> fn) {
        return new MappedCollectionWrapper<>(source, mapFn.andThen(fn));
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        Iterator<Elem> it = iterator();
        if (o == null) {
            while (it.hasNext())
                if (it.next() == null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }
        return false;
    }

    @Override
    public Iterator<Elem> iterator() {
        Iterator<Source> internalIterator = source.iterator();
        return new ImmutableIterator<Elem>() {
            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public Elem next() {
                return mapFn.apply(internalIterator.next());
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] toArray() {
        Object[] result = source.toArray();
        for (int i = 0; i < result.length; i++) {
            result[i] = mapFn.apply((Source) result[i]);
        }
        return result;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        ArrayList<Elem> bs = toCollection(ArrayList::new, this);
        return bs.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        // TODO: hashCode
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MappedCollectionWrapper) {
            Iterator<Elem> myIter = iterator();
            Iterator<?> otherIter = ((MappedCollectionWrapper<?, ?>) obj).iterator();
            boolean result = true;
            while (result && myIter.hasNext() && otherIter.hasNext()) {
                result = myIter.next().equals(otherIter.next());
            }
            return result;
        }
        return false;
    }

    static <Source, Elem> MappedCollectionWrapper<Source, Elem> mappedCollectionWrapper(ImmutableCollection<Source> source,
                                                                                        Fn1<Source, Elem> mapFn) {
        return new MappedCollectionWrapper<>(source, mapFn);
    }
}
