package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongBetweenImpl implements Generator<Long> {
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
