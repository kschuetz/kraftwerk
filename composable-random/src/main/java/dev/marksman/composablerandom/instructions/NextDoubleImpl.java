package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextDoubleImpl implements Generate<Double> {
    private static NextDoubleImpl INSTANCE = new NextDoubleImpl();

    @Override
    public Result<? extends RandomState, Double> generate(RandomState input) {
        return input.nextDouble();
    }

    public static NextDoubleImpl nextDoubleImpl() {
        return INSTANCE;
    }
}
