package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextBooleanImpl implements GeneratorState<Boolean> {
    private static NextBooleanImpl INSTANCE = new NextBooleanImpl();

    @Override
    public Result<? extends Seed, Boolean> run(Seed input) {
        return input.nextBoolean();
    }

    public static NextBooleanImpl nextBooleanImpl() {
        return INSTANCE;
    }
}
