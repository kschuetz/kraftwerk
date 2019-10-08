package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongExclusiveImpl implements GeneratorImpl<Long> {
    private final long origin;
    private final long bound;

    @Override
    public Result<? extends Seed, Long> run(Seed input) {
        return input.nextLongExclusive(origin, bound);
    }

    public static NextLongExclusiveImpl nextLongExclusiveImpl(long origin, long bound) {
        return new NextLongExclusiveImpl(origin, bound);
    }
}
