package dev.marksman.composablerandom;

public interface Seed {
    long getSeedValue();

    Seed perturb(long value);

    Seed setNextSeedValue(long value);
}
