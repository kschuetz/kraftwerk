package dev.marksman.nonempty;

import java.util.Collection;

import static dev.marksman.nonempty.NonEmptyCons.nonEmptyCons;

public interface NonEmptyList<A> extends NonEmptyCollection<A> {
    static <A> NonEmptyList<A> nonEmptyList(A head, Collection<A> tail) {
        return nonEmptyCons(head, tail);
    }
}
