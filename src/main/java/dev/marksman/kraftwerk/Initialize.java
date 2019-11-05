package dev.marksman.kraftwerk;

import java.util.Random;

import static dev.marksman.kraftwerk.StandardSeed.initStandardSeed;

public class Initialize {
    public static Seed createInitialSeed(long initialSeedValue) {
        return initStandardSeed(initialSeedValue);
    }

    public static Seed randomInitialSeed() {
        return createInitialSeed(new Random().nextLong());
    }
}
