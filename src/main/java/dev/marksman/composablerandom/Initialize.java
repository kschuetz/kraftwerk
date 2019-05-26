package dev.marksman.composablerandom;

import java.util.Random;

import static dev.marksman.composablerandom.random.StandardGen.initStandardGen;

public class Initialize {
    public static RandomState createInitialRandomState(long initialSeedValue) {
        return initStandardGen(initialSeedValue);
    }

    public static RandomState randomInitialRandomState() {
        return createInitialRandomState(new Random().nextLong());
    }
}
