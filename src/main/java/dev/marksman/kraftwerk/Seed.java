package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.core.StandardSeed;

import java.util.Random;

public interface Seed {
    long getSeedValue();

    Seed perturb(long value);

    Seed setNextSeedValue(long value);

    static Seed create(long value) {
        return StandardSeed.initStandardSeed(value);
    }

    static Seed random() {
        return create(new Random().nextLong());
    }
}
