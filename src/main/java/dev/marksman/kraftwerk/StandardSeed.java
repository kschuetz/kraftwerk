package dev.marksman.kraftwerk;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Random;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StandardSeed implements Seed {
    private final long seedValue;

    @Override
    public Seed perturb(long value) {
        // TODO
        return new StandardSeed(seedValue ^ value);
    }

    @Override
    public Seed setNextSeedValue(long value) {
        return new StandardSeed(value);
    }

    public static StandardSeed standardSeed(long value) {
        return new StandardSeed(value);
    }

    public static StandardSeed initStandardSeed(long seed) {
        return standardSeed((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1));
    }

    public static StandardSeed initStandardSeed() {
        Random random = new Random();
        return standardSeed(random.nextLong());
    }

}
