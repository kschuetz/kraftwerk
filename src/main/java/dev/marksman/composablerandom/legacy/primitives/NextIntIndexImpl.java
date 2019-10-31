package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntIndexImpl implements GeneratorImpl<Integer> {
    private final int bound;

    @Override
    public Result<? extends LegacySeed, Integer> run(LegacySeed input) {
        return input.nextIntBounded(bound);
    }

    public static NextIntIndexImpl nextIntIndexImpl(int bound) {
        return new NextIntIndexImpl(bound);
    }
}
