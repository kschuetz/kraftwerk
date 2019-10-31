package dev.marksman.composablerandom;

import java.util.Random;

import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Initialize {
    public static LegacySeed createInitialSeed(long initialSeedValue) {
        return initStandardGen(initialSeedValue);
    }

    public static LegacySeed randomInitialSeed() {
        return createInitialSeed(new Random().nextLong());
    }
}
