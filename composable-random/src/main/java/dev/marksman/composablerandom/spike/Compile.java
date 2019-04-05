package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.Instruction;

public interface Compile<In, Out> {
    Generate<Out> compile(Instruction<In> instruction);
}
