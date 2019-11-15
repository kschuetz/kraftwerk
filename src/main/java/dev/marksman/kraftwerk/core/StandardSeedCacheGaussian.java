package dev.marksman.kraftwerk.core;

import dev.marksman.kraftwerk.Seed;
import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
class StandardSeedCacheGaussian implements Seed {
    private final Seed underlying;
    private final double nextGaussian;

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

    static StandardSeedCacheGaussian standardSeedCacheGaussian(Seed underlying, double nextGaussian) {
        return new StandardSeedCacheGaussian(underlying, nextGaussian);
    }
}
