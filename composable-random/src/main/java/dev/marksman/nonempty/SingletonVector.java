package dev.marksman.nonempty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Iterator;
import java.util.function.Function;

import static dev.marksman.nonempty.Util.validateBounds;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingletonVector<A> implements NonEmptyVector<A> {
    private final A value;

    @Override
    public int size() {
        return 1;
    }

    @Override
    public A get(int index) {
        validateBounds(1, index);
        return value;
    }

    @Override
    public <B> SingletonVector<B> fmap(Function<? super A, ? extends B> fn) {
        return singletonVector(fn.apply(value));
    }

    @Override
    public A getHead() {
        return value;
    }

    @Override
    public Iterator<A> iterator() {
        return null;
    }

    public static <A> SingletonVector<A> singletonVector(A value) {
        return new SingletonVector<>(value);
    }
}
