package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextBooleanImpl implements Generate<Boolean> {
    private static NextBooleanImpl INSTANCE = new NextBooleanImpl();

    @Override
    public Result<? extends RandomState, Boolean> generate(RandomState input) {
        return input.nextBoolean();
    }

    public static NextBooleanImpl nextBooleanImpl() {
        return INSTANCE;
    }
}
