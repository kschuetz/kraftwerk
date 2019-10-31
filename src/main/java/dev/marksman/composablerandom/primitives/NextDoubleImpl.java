package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextDoubleImpl implements GeneratorImpl<Double> {
    private static NextDoubleImpl INSTANCE = new NextDoubleImpl();

    @Override
    public Result<? extends LegacySeed, Double> run(LegacySeed input) {
        return input.nextDouble();
    }

    public static NextDoubleImpl nextDoubleImpl() {
        return INSTANCE;
    }
}
