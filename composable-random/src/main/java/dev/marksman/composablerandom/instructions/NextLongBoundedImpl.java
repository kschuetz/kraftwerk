package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongBoundedImpl implements Generate<Long> {
    private final long bound;

    @Override
    public Result<? extends RandomState, Long> generate(RandomState input) {
        return input.nextLongBounded(bound);
    }

    public static NextLongBoundedImpl nextLongBoundedImpl(long bound) {
        return new NextLongBoundedImpl(bound);
    }
}
