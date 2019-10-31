package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntBetweenImpl implements GeneratorImpl<Integer> {
    private final int min;
    private final int max;

    @Override
    public Result<? extends LegacySeed, Integer> run(LegacySeed input) {
        return input.nextIntBetween(min, max);
    }

    public static NextIntBetweenImpl nextIntBetweenImpl(int min, int max) {
        return new NextIntBetweenImpl(min, max);
    }
}
