package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static dev.marksman.shrink.builtins.ShrinkNumerics.shrinkInt;
import static dev.marksman.shrink.util.LazyCons.lazyCons;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntImpl implements GeneratorImpl<Integer> {
    private static NextIntImpl INSTANCE = new NextIntImpl();

    @Override
    public Result<? extends Seed, Integer> run(Seed input) {
        return input.nextInt();
    }

    @Override
    public Result<? extends Seed, ImmutableNonEmptyFiniteIterable<Integer>> runShrinking(Seed input) {
        return run(input).fmap(n -> lazyCons(n, () -> shrinkInt().apply(n)));
    }

    public static NextIntImpl nextIntImpl() {
        return INSTANCE;
    }
}
