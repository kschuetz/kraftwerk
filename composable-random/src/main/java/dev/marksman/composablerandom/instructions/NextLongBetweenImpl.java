package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongBetweenImpl implements CompiledGenerator<Long> {
    private final long min;
    private final long max;

    @Override
    public Result<? extends RandomState, Long> run(RandomState input) {
        return input.nextLongBetween(min, max);
    }

    public static NextLongBetweenImpl nextLongBetweenImpl(long min, long max) {
        return new NextLongBetweenImpl(min, max);
    }
}
