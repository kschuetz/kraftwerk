package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextShortImpl implements GeneratorState<Short> {
    private static NextShortImpl INSTANCE = new NextShortImpl();

    @Override
    public Result<? extends RandomState, Short> run(RandomState input) {
        return input.nextInt().fmap(Integer::shortValue);
    }

    public static NextShortImpl nextShortImpl() {
        return INSTANCE;
    }
}
