package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextByteImpl implements GeneratorState<Byte> {
    private static NextByteImpl INSTANCE = new NextByteImpl();

    @Override
    public Result<? extends Seed, Byte> run(Seed input) {
        return input.nextInt().fmap(Integer::byteValue);
    }

    public static NextByteImpl nextByteImpl() {
        return INSTANCE;
    }
}
