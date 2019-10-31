package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntImpl implements GeneratorImpl<Integer> {
    private static NextIntImpl INSTANCE = new NextIntImpl();

    @Override
    public Result<? extends LegacySeed, Integer> run(LegacySeed input) {
        return input.nextInt();
    }

    public static NextIntImpl nextIntImpl() {
        return INSTANCE;
    }
}
