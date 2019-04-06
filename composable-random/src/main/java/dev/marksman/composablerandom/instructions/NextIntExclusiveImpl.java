package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntExclusiveImpl implements CompiledGenerator<Integer> {
    private final int origin;
    private final int bound;

    @Override
    public Result<? extends RandomState, Integer> run(RandomState input) {
        return input.nextIntExclusive(origin, bound);
    }

    public static NextIntExclusiveImpl nextIntExclusiveImpl(int origin, int bound) {
        return new NextIntExclusiveImpl(origin, bound);
    }
}
