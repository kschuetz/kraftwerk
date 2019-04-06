package dev.marksman.composablerandom.instructions;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.CompiledGenerator;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextBytesImpl implements CompiledGenerator<Byte[]> {
    private final int count;

    @Override
    public Result<? extends RandomState, Byte[]> run(RandomState input) {
        byte[] buffer = new byte[count];
        Result<? extends RandomState, Unit> next = input.nextBytes(buffer);
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
