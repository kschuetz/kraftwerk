package dev.marksman.discretedomain;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;

public abstract class SmallDomain<A> implements DiscreteDomain<A> {
    public static int MAX_SMALL_DOMAIN_SIZE = Short.MAX_VALUE + 1;

    @Override
    public Maybe<SmallDomain<A>> toSmallDomain() {
        return just(this);
    }

    public Maybe<SmallDomain<A>> intersect(SmallDomain<A> other) {
        return MemberList.intersection(this, other);
    }

    // TODO: difference

    public static <A> SmallDomain<A> smallDomain(int size, Function<Short, A> fn) {
        if (size <= 0) {
            throw new IllegalArgumentException("SmallDomain size must be positive");
        }
        if (size > MAX_SMALL_DOMAIN_SIZE) {
            throw new IllegalArgumentException("SmallDomain size cannot exceed " + MAX_SMALL_DOMAIN_SIZE);
        }
        return new SmallDomain<A>() {
            @Override
            public long getSize() {
                return size;
            }

            @Override
            public A getValue(long index) {
                return fn.apply((short) index);
            }
        };
    }
}
