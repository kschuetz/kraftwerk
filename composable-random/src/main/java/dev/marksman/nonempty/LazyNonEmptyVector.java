package dev.marksman.nonempty;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

import static dev.marksman.nonempty.Util.validateBounds;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class LazyNonEmptyVector<A> implements NonEmptyVector<A> {
    private final int size;
    private final Fn1<Integer, ? extends A> fn;

    @Override
    public int size() {
        return size;
    }

    @Override
    public A get(int index) {
        validateBounds(size, index);
        return fn.apply(index);
    }

    @Override
    public <B> LazyNonEmptyVector<B> fmap(Function<? super A, ? extends B> fn) {
        return new LazyNonEmptyVector<>(size, this.fn.andThen(fn));
    }

    @Override
    public A getHead() {
        return fn.apply(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<A> iterator() {
        return (LazySliceIterator<A>) LazySliceIterator.lazySliceIterator(fn, 0, size);
    }
}
