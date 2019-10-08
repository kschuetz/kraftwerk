package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntBoundedImpl implements GeneratorImpl<Integer> {
    private final int bound;

    @Override
    public Result<? extends Seed, Integer> run(Seed input) {
        return input.nextIntBounded(bound);
    }

    public static NextIntBoundedImpl nextIntBoundedImpl(int bound) {
        return new NextIntBoundedImpl(bound);
    }
}
