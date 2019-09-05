package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntImpl implements GeneratorState<Integer> {
    private static NextIntImpl INSTANCE = new NextIntImpl();

    @Override
    public Result<? extends Seed, Integer> run(Seed input) {
        return input.nextInt();
    }

    public static NextIntImpl nextIntImpl() {
        return INSTANCE;
    }
}
