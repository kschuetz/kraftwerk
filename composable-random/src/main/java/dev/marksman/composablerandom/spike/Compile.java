package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.Instruction;

public interface Compile<In, Out> {
    Interpreter<Out> compile(Instruction<In> instruction);
}
