package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongBetweenImpl implements GeneratorState<Long> {
    private final long min;
    private final long max;

    @Override
    public Result<? extends Seed, Long> run(Seed input) {
        return input.nextLongBetween(min, max);
    }

    public static NextLongBetweenImpl nextLongBetweenImpl(long min, long max) {
        return new NextLongBetweenImpl(min, max);
    }
}
