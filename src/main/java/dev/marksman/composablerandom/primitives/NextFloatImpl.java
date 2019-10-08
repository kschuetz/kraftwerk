package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextFloatImpl implements GeneratorImpl<Float> {
    private static NextFloatImpl INSTANCE = new NextFloatImpl();

    @Override
    public Result<? extends Seed, Float> run(Seed input) {
        return input.nextFloat();
    }

    public static NextFloatImpl nextFloatImpl() {
        return INSTANCE;
    }
}
