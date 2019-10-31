package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntExclusiveImpl implements GeneratorImpl<Integer> {
    private final int origin;
    private final int bound;

    @Override
    public Result<? extends LegacySeed, Integer> run(LegacySeed input) {
        return input.nextIntExclusive(origin, bound);
    }

    public static NextIntExclusiveImpl nextIntExclusiveImpl(int origin, int bound) {
        return new NextIntExclusiveImpl(origin, bound);
    }
}
