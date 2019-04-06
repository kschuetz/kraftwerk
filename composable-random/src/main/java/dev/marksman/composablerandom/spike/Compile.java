package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.Generator;

public interface Compile<In, Out> {
    CompiledGenerator<Out> compile(Generator<In> generator);
}
