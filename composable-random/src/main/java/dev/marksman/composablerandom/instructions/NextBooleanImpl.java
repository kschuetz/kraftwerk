package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextBooleanImpl implements CompiledGenerator<Boolean> {
    private static NextBooleanImpl INSTANCE = new NextBooleanImpl();

    @Override
    public Result<? extends RandomState, Boolean> run(RandomState input) {
        return input.nextBoolean();
    }

    public static NextBooleanImpl nextBooleanImpl() {
        return INSTANCE;
    }
}
