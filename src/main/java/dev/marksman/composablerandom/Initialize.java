package dev.marksman.composablerandom;

import java.util.Random;

import static dev.marksman.composablerandom.StandardSeed.initStandardSeed;

public class Initialize {
    public static Seed createInitialSeed(long initialSeedValue) {
        return initStandardSeed(initialSeedValue);
    }

    public static Seed randomInitialSeed() {
        return createInitialSeed(new Random().nextLong());
    }
}
