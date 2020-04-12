package dev.marksman.kraftwerk.constraints;

public interface Constraint<A> {
    boolean includes(A value);
}
