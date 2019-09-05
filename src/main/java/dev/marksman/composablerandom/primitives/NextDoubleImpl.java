package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextDoubleImpl implements GeneratorState<Double> {
    private static NextDoubleImpl INSTANCE = new NextDoubleImpl();

    @Override
    public Result<? extends Seed, Double> run(Seed input) {
        return input.nextDouble();
    }

    public static NextDoubleImpl nextDoubleImpl() {
        return INSTANCE;
    }
}
