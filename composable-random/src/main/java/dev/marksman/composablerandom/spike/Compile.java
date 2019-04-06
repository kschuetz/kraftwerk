package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.Instruction;

public interface Compile<In, Out> {
    CompiledGenerator<Out> compile(Instruction<In> instruction);
}
