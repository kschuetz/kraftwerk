package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.functions.Fn1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Mapped<A, B> implements DiscreteDomain<B> {
    private final DiscreteDomain<A> source;
    private final Fn1<? super A, ? extends B> fn;

    @Override
    public long getSize() {
        return source.getSize();
    }

    @Override
    public B getValue(long index) {
        return fn.apply(source.getValue(index));
    }

    static <A, B> Mapped<A, B> mapped(DiscreteDomain<A> source, Fn1<? super A, ? extends B> fn) {
        return new Mapped<>(source, fn);
    }
}
