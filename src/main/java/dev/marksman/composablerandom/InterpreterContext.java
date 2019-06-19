package dev.marksman.composablerandom;

public interface InterpreterContext {
    Parameters getParameters();

    <A> Generator<A> recurse(Generate<A> gen);

    <A> Generator<A> callNextHandler(Generate<A> gen);
}
