package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongExclusiveImpl implements Generate<Long> {
    private final long origin;
    private final long bound;

    @Override
    public Result<? extends RandomState, Long> generate(RandomState input) {
        return input.nextLongExclusive(origin, bound);
    }

    public static NextLongExclusiveImpl nextLongExclusiveImpl(long origin, long bound) {
        return new NextLongExclusiveImpl(origin, bound);
    }
}
