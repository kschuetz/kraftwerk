package dev.marksman.composablerandom;

import java.util.Iterator;
import java.util.function.Function;

public interface DiscreteDomain<A> extends Iterable<A> {
    long getSize();

    A getValue(long index);

    static <A> DiscreteDomain<A> discreteDomain(long size, Function<Long, A> fn) {
        if (size <= 0) {
            throw new IllegalArgumentException("DiscreteDomain size must be positive");
        }
        return new DiscreteDomain<A>() {
            @Override
            public long getSize() {
                return size;
            }

            @Override
            public A getValue(long index) {
                return fn.apply(index);
            }
        };
    }

    @Override
    default Iterator<A> iterator() {
        return new DiscreteDomainIterator<>(this);
    }

}
