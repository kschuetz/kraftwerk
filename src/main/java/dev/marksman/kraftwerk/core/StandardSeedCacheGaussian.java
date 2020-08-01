package dev.marksman.kraftwerk.core;

import dev.marksman.kraftwerk.Seed;

final class StandardSeedCacheGaussian implements Seed {
    private final Seed underlying;
    private final double nextGaussian;

    private StandardSeedCacheGaussian(Seed underlying, double nextGaussian) {
        this.underlying = underlying;
        this.nextGaussian = nextGaussian;
    }

    static StandardSeedCacheGaussian standardSeedCacheGaussian(Seed underlying, double nextGaussian) {
        return new StandardSeedCacheGaussian(underlying, nextGaussian);
    }

    public long getSeedValue() {
        return underlying.getSeedValue();
    }

    @Override
    public Seed perturb(long value) {
        return underlying.perturb(value);
    }

    @Override
    public Seed setNextSeedValue(long value) {
        return new StandardSeedCacheGaussian(underlying.setNextSeedValue(value),
                nextGaussian);
    }

    public Seed getUnderlying() {
        return this.underlying;
    }

    public double getNextGaussian() {
        return this.nextGaussian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StandardSeedCacheGaussian that = (StandardSeedCacheGaussian) o;

        if (Double.compare(that.nextGaussian, nextGaussian) != 0) return false;
        return underlying.equals(that.underlying);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = underlying.hashCode();
        temp = Double.doubleToLongBits(nextGaussian);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "StandardSeedCacheGaussian{" +
                "underlying=" + underlying +
                ", nextGaussian=" + nextGaussian +
                '}';
    }
}
