package dev.marksman.composablerandom;

public interface GeneratorImpl<A> {
    Result<? extends LegacySeed, A> run(LegacySeed input);
}
