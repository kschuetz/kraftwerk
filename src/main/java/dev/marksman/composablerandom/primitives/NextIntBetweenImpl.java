package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntBetweenImpl implements GeneratorState<Integer> {
    private final int min;
    private final int max;

    @Override
    public Result<? extends RandomState, Integer> run(RandomState input) {
        return input.nextIntBetween(min, max);
    }

    public static NextIntBetweenImpl nextIntBetweenImpl(int min, int max) {
        return new NextIntBetweenImpl(min, max);
    }
}
