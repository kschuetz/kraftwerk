package dev.marksman.composablerandom;

import java.util.Random;

import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Initialize {
    public static Seed createInitialSeed(long initialSeedValue) {
        return initStandardGen(initialSeedValue);
    }

    public static Seed randomInitialSeed() {
        return createInitialSeed(new Random().nextLong());
    }
}
