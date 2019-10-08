package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongBoundedImpl implements GeneratorImpl<Long> {
    private final long bound;

    @Override
    public Result<? extends Seed, Long> run(Seed input) {
        return input.nextLongBounded(bound);
    }

    public static NextLongBoundedImpl nextLongBoundedImpl(long bound) {
        return new NextLongBoundedImpl(bound);
    }
}
