package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextFloatImpl implements GeneratorImpl<Float> {
    private static NextFloatImpl INSTANCE = new NextFloatImpl();

    @Override
    public Result<? extends LegacySeed, Float> run(LegacySeed input) {
        return input.nextFloat();
    }

    public static NextFloatImpl nextFloatImpl() {
        return INSTANCE;
    }
}
