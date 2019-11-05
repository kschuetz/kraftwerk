package dev.marksman.kraftwerk;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class StandardSeedCacheGaussian implements Seed {
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

    public static StandardSeedCacheGaussian standardSeedCacheGaussian(Seed underlying, double nextGaussian) {
        return new StandardSeedCacheGaussian(underlying, nextGaussian);
    }
}
