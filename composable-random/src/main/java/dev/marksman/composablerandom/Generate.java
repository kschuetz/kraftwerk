package dev.marksman.composablerandom;

public interface Generate<A> {
    Result<? extends RandomState, A> generate(RandomState input);
}
