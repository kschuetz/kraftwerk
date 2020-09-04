package dev.marksman.kraftwerk.core;

import dev.marksman.kraftwerk.Seed;

import java.util.Random;

public final class StandardSeed implements Seed {
    private final long seedValue;

    private StandardSeed(long seedValue) {
        this.seedValue = seedValue;
    }

    private static StandardSeed standardSeed(long value) {
        return new StandardSeed(value);
    }

    public static StandardSeed initStandardSeed(long seed) {
        return standardSeed((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1));
    }

    public static StandardSeed initStandardSeed() {
        Random random = new Random();
        return standardSeed(random.nextLong());
    }

    @Override
    public Seed perturb(long value) {
        return BuildingBlocks.perturb(value, this);
    }

    @Override
    public Seed setNextSeedValue(long value) {
        return new StandardSeed(value);
    }

    public long getSeedValue() {
        return this.seedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StandardSeed that = (StandardSeed) o;

        return seedValue == that.seedValue;
    }

    @Override
    public int hashCode() {
        return (int) (seedValue ^ (seedValue >>> 32));
    }

    @Override
    public String toString() {
        return "StandardSeed{" +
                "seedValue=" + seedValue +
                '}';
    }
}
