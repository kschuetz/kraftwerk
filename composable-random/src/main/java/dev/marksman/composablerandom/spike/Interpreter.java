package dev.marksman.composablerandom.spike;

import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;

public interface Interpreter<A> {
    Result<? extends RandomState, A> execute(RandomState input);
}
