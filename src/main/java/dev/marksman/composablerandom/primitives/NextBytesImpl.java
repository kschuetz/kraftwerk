package dev.marksman.composablerandom.primitives;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.GeneratorImpl;
import dev.marksman.composablerandom.LegacySeed;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextBytesImpl implements GeneratorImpl<Byte[]> {
    private final int count;

    @Override
    public Result<? extends LegacySeed, Byte[]> run(LegacySeed input) {
        byte[] buffer = new byte[count];
        Result<? extends LegacySeed, Unit> next = input.nextBytes(buffer);
        Byte[] result = new Byte[count];
        int i = 0;
        for (byte b : buffer) {
            result[i++] = b;
        }
        return next.fmap(__ -> result);
    }

    public static NextBytesImpl nextBytesImpl(int count) {
        return new NextBytesImpl(Math.max(count, 0));
    }
}
