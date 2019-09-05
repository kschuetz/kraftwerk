package dev.marksman.composablerandom;

public interface InterpreterContext {
    Parameters getParameters();

    <A> GeneratorState<A> recurse(Generator<A> gen);

    <A> GeneratorState<A> callNextHandler(Generator<A> gen);
}
