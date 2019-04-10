package dev.marksman.nonempty;

import com.jnape.palatable.lambda.monoid.builtin.Concat;

import java.util.Iterator;
import java.util.function.Function;

import static dev.marksman.nonempty.Util.validateBounds;

class ConcatVectors<A> implements NonEmptyVector<A> {
    private final NonEmptyVector<A> first;
    private final NonEmptyVector<A> second;
    private final int size1;
    private final int size;

    private ConcatVectors(NonEmptyVector<A> first, NonEmptyVector<A> second) {
        this.first = first;
        this.second = second;
        this.size1 = first.size();
        this.size = size1 + second.size();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public A get(int index) {
        validateBounds(size, index);
        if (index < size1) return first.get(index);
        else return second.get(index - size1);
    }

    @Override
    public NonEmptyVector<A> concat(Iterable<A> other) {
        return null;
    }

    @Override
    public <B> ConcatVectors<B> fmap(Function<? super A, ? extends B> fn) {
        return concatVectors(first.fmap(fn), second.fmap(fn));
    }

    @Override
    public A getHead() {
        return first.getHead();
    }

    @Override
    public Iterator<A> iterator() {
        return Concat.concat(first, second).iterator();
    }

    static <A> ConcatVectors<A> concatVectors(NonEmptyVector<A> first, NonEmptyVector<A> second) {
        return new ConcatVectors<>(first, second);
    }
}
