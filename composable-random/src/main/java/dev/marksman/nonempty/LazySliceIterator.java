package dev.marksman.nonempty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class LazySliceIterator<A> implements Iterator<A> {
    private final Function<Integer, ? extends A> supplier;
    private int offset;
    private int size;
    private int index;

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public A next() {
        A result = supplier.apply(offset + index);
        index += 1;
        return result;
    }

    static <A> LazySliceIterator<A> lazySliceIterator(Function<Integer, A> supplier, int offset, int size) {
        return new LazySliceIterator<>(supplier, offset, size, 0);
    }
}
