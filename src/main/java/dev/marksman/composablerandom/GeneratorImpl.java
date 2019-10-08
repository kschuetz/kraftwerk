package dev.marksman.composablerandom;

public interface GeneratorImpl<A> {
    Result<? extends Seed, A> run(Seed input);
}
