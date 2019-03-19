package dev.marksman.composablerandom;

import java.util.function.Function;

public interface Domain<A> {
    long getSize();

    A getValue(long index);

    static <A> Domain<A> domain(long size, Function<Long, A> fn) {
        if(size <= 0) {
            throw new IllegalArgumentException("Domain size must be positive");
        }
        return new Domain<A>() {
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
}
