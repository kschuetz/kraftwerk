package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

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

}
