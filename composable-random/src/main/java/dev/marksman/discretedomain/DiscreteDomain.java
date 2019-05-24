package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Iterator;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.discretedomain.CrossJoin.crossJoin;
import static dev.marksman.discretedomain.Mapped.mapped;

/**
 * The purpose of a `DiscreteDomain` is to represent a potentially large, finite, non-empty collection of values,
 * without necessarily being backed by a concrete collection type.
 *
 * <p>
 * A `DiscreteDomain` provides the following guarantees:
 * - Always contains at least one element
 * - Both `getSize()` and `getValue()` execute in O(1)
 * - The iteration order for a given instance will always be consistent
 * <p>
 * A `DiscreteDomain` does NOT guarantee:
 * - Any particular order of iteration
 * - That there will be no duplicate elements
 * <p>
 * A `SmallDomain` is a special case of a `DiscreteDomain` of size <= 32768.
 * <p>
 * A `SmallDomain` comes with additional capabilities, such as intersecting with other `SmallDomain`s.
 *
 * @param <A>
 */
public interface DiscreteDomain<A> extends Iterable<A>, Functor<A, DiscreteDomain<?>> {
    /**
     * The size of the domain.  Guaranteed to be > 0 and execute in O(1).
     *
     * @return
     */
    long getSize();

    /**
     * Gets the value at index.  Guaranteed to execute in O(1).
     * @param index A number between 0 and getSize(), exclusive.
     * @return the value at the domain's index
     * @throws java.util.NoSuchElementException if `index` &lt; 0 or &gt;= getSize()
     */
    A getValue(long index);

    @Override
    default <B> DiscreteDomain<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mapped(this, fn);
    }

    /**
     * Returns a new `DiscreteDomain` containing the cartesian product of elements
     * in both source domains.
     * @param other
     * @param <B>
     * @return
     */
    default <B> DiscreteDomain<Tuple2<A, B>> cross(DiscreteDomain<B> other) {
        return crossJoin(this, other);
    }

    /**
     * If uniting with another `DiscreteDomain` results in a new domain that can
     * fit in a `SmallDomain`, returns the resultant `SmallDomain` in a `Maybe.just`.
     * <p>
     * Does not mutate either source domain.
     *
     * @param other the other domain.
     * @return
     */
    default Maybe<SmallDomain<A>> union(DiscreteDomain<A> other) {
        return MemberList.union(this, other);
    }

    /**
     * Converts to a `SmallDomain` if possible.
     *
     * @return
     */
    default Maybe<SmallDomain<A>> toSmallDomain() {
        if (getSize() <= SmallDomain.MAX_SMALL_DOMAIN_SIZE) {
            return just(SmallDomain.smallDomain((short) getSize(), this::getValue));
        } else {
            return nothing();
        }
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
