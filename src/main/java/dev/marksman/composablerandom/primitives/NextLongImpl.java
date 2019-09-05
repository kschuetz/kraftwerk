package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextLongImpl implements GeneratorState<Long> {
    private static NextLongImpl INSTANCE = new NextLongImpl();

    @Override
    public Result<? extends Seed, Long> run(Seed input) {
        return input.nextLong();
    }

    public static NextLongImpl nextLongImpl() {
        return INSTANCE;
    }

}
