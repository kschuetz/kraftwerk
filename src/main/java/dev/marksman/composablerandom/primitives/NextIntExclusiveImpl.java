package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntExclusiveImpl implements Generator<Integer> {
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
