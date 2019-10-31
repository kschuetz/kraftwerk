package dev.marksman.composablerandom.legacy.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextGaussianImpl implements GeneratorImpl<Double> {
    private static NextGaussianImpl INSTANCE = new NextGaussianImpl();

    @Override
    public Result<? extends LegacySeed, Double> run(LegacySeed input) {
        return input.nextGaussian();
    }

    public static NextGaussianImpl nextGaussianImpl() {
        return INSTANCE;
    }
}
