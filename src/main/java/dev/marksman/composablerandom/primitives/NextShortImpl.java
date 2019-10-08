package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextShortImpl implements GeneratorImpl<Short> {
    private static NextShortImpl INSTANCE = new NextShortImpl();

    @Override
    public Result<? extends Seed, Short> run(Seed input) {
        return input.nextInt().fmap(Integer::shortValue);
    }

    public static NextShortImpl nextShortImpl() {
        return INSTANCE;
    }
}
