package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongImpl implements GeneratorImpl<Long> {
    private static NextLongImpl INSTANCE = new NextLongImpl();

    @Override
    public Result<? extends LegacySeed, Long> run(LegacySeed input) {
        return input.nextLong();
    }

    public static NextLongImpl nextLongImpl() {
        return INSTANCE;
    }

}
