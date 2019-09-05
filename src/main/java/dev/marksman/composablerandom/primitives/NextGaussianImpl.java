package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextGaussianImpl implements GeneratorState<Double> {
    private static NextGaussianImpl INSTANCE = new NextGaussianImpl();

    @Override
    public Result<? extends RandomState, Double> run(RandomState input) {
        return input.nextGaussian();
    }

    public static NextGaussianImpl nextGaussianImpl() {
        return INSTANCE;
    }
}
