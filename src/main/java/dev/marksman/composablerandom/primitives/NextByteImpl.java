package dev.marksman.composablerandom.primitives;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextByteImpl implements Generator<Byte> {
    private static NextByteImpl INSTANCE = new NextByteImpl();

    @Override
    public Result<? extends RandomState, Byte> run(RandomState input) {
        return input.nextInt().fmap(Integer::byteValue);
    }

    public static NextByteImpl nextByteImpl() {
        return INSTANCE;
    }
}
