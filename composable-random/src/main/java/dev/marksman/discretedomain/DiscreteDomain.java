package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Iterator;
import java.util.function.Function;

import static dev.marksman.discretedomain.CrossJoin.crossJoin;
import static dev.marksman.discretedomain.Mapped.mapped;

public interface DiscreteDomain<A> extends Iterable<A>, Functor<A, DiscreteDomain<?>> {
    long getSize();

    A getValue(long index);

    @Override
    default <B> DiscreteDomain<B> fmap(Function<? super A, ? extends B> fn) {
        return mapped(this, fn);
    }

    default <B> DiscreteDomain<Tuple2<A, B>> cross(DiscreteDomain<B> other) {
        return crossJoin(this, other);
    }

    @Override
    default Iterator<A> iterator() {
        return new DiscreteDomainIterator<>(this);
    }

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

}
