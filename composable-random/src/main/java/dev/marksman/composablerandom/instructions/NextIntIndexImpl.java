package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntIndexImpl implements CompiledGenerator<Integer> {
    private final int bound;

    @Override
    public Result<? extends RandomState, Integer> run(RandomState input) {
        return input.nextIntBounded(bound);
    }

    public static NextIntIndexImpl nextIntIndexImpl(int bound) {
        return new NextIntIndexImpl(bound);
    }
}
