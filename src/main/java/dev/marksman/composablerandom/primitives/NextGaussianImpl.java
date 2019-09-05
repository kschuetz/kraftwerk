package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextGaussianImpl implements GeneratorState<Double> {
    private static NextGaussianImpl INSTANCE = new NextGaussianImpl();

    @Override
    public Result<? extends Seed, Double> run(Seed input) {
        return input.nextGaussian();
    }

    public static NextGaussianImpl nextGaussianImpl() {
        return INSTANCE;
    }
}
