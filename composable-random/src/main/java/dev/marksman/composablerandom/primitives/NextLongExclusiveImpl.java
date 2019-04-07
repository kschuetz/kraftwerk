package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongExclusiveImpl implements CompiledGenerator<Long> {
    private final long origin;
    private final long bound;

    @Override
    public Result<? extends RandomState, Long> run(RandomState input) {
        return input.nextLongExclusive(origin, bound);
    }

    public static NextLongExclusiveImpl nextLongExclusiveImpl(long origin, long bound) {
        return new NextLongExclusiveImpl(origin, bound);
    }
}
