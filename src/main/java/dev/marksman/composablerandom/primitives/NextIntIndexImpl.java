package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntIndexImpl implements GeneratorImpl<Integer> {
    private final int bound;

    @Override
    public Result<? extends Seed, Integer> run(Seed input) {
        return input.nextIntBounded(bound);
    }

    public static NextIntIndexImpl nextIntIndexImpl(int bound) {
        return new NextIntIndexImpl(bound);
    }
}
