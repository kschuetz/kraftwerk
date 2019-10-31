package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextByteImpl implements GeneratorImpl<Byte> {
    private static NextByteImpl INSTANCE = new NextByteImpl();

    @Override
    public Result<? extends LegacySeed, Byte> run(LegacySeed input) {
        return input.nextInt().fmap(Integer::byteValue);
    }

    public static NextByteImpl nextByteImpl() {
        return INSTANCE;
    }
}
