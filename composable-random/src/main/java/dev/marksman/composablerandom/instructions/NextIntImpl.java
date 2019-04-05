package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntImpl implements Generate<Integer> {
    private static NextIntImpl INSTANCE = new NextIntImpl();

    @Override
    public Result<? extends RandomState, Integer> generate(RandomState input) {
        return input.nextInt();
    }

    public static NextIntImpl nextIntImpl() {
        return INSTANCE;
    }
}
