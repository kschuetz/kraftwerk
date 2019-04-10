package dev.marksman.nonempty;

public interface NonEmptyIterable<A> extends Iterable<A> {
    A getHead();

    Iterable<A> getTail();
}
