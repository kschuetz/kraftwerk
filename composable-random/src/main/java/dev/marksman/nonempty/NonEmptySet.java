package dev.marksman.nonempty;

import static com.jnape.palatable.lambda.monoid.builtin.Concat.concat;

public interface NonEmptySet<A> extends NonEmptyIterable<A> {

    boolean contains(A item);

    NonEmptySet<A> add(A item);

    default NonEmptySet<A> addAll(Iterable<A> elements) {
        return nonEmptySet(getHead(), concat(this.getTail(), elements));
    }

    static <A> NonEmptySet<A> nonEmptySet(A first, Iterable<A> rest) {
        return ConcreteSets.nonEmptySet(first, rest);
    }

    static <A> NonEmptySet<A> nonEmptySet(NonEmptyIterable<A> elements) {
        return ConcreteSets.nonEmptySet(elements);
    }

}
