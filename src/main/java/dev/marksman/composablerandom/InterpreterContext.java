package dev.marksman.composablerandom;

public interface InterpreterContext {
    Parameters getParameters();

    <A> CompiledGenerator<A> recurse(Generator<A> generator);

    <A> CompiledGenerator<A> callNextHandler(Generator<A> generator);
}
