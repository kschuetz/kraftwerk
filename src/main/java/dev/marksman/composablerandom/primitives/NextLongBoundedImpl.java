package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongBoundedImpl implements GeneratorImpl<Long> {
    private final long bound;

    @Override
    public Result<? extends LegacySeed, Long> run(LegacySeed input) {
        return input.nextLongBounded(bound);
    }

    public static NextLongBoundedImpl nextLongBoundedImpl(long bound) {
        return new NextLongBoundedImpl(bound);
    }
}
