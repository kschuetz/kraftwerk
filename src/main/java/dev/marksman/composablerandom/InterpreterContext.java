package dev.marksman.composablerandom;

public interface InterpreterContext {
    Parameters getParameters();

    <A> GeneratorImpl<A> recurse(Generator<A> gen);

    <A> GeneratorImpl<A> callNextHandler(Generator<A> gen);
}
