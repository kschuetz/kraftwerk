package dev.marksman.composablerandom;

public interface Interpreter {
    <A, R> Result<RandomState, R> execute(RandomState input, Instruction<A> instruction);
}
