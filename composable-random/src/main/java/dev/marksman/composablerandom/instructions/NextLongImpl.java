package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongImpl implements Generate<Long> {
    private static NextLongImpl INSTANCE = new NextLongImpl();

    @Override
    public Result<? extends RandomState, Long> generate(RandomState input) {
        return input.nextLong();
    }

    public static NextLongImpl nextLongImpl() {
        return INSTANCE;
    }

}
