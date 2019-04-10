package dev.marksman.nonempty;

public interface NonEmptyCollection<A> extends NonEmptyIterable<A> {
    int size();

    ImmutableCollection<A> slice(int startIndex);
}
