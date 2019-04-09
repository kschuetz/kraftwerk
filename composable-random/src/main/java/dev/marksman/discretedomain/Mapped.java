package dev.marksman.discretedomain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Mapped<A, B> implements DiscreteDomain<B> {
    private final DiscreteDomain<A> source;
    private final Function<? super A, ? extends B> fn;

    @Override
    public long getSize() {
        return source.getSize();
    }

    @Override
    public B getValue(long index) {
        return fn.apply(source.getValue(index));
    }

    static <A, B> Mapped<A, B> mapped(DiscreteDomain<A> source, Function<? super A, ? extends B> fn) {
        return new Mapped<>(source, fn);
    }
}
