package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongIndexImpl implements GeneratorState<Long> {
    private final long bound;

    @Override
    public Result<? extends Seed, Long> run(Seed input) {
        return input.nextLongBounded(bound);
    }

    public static NextLongIndexImpl nextLongIndexImpl(long bound) {
        return new NextLongIndexImpl(bound);
    }
}
