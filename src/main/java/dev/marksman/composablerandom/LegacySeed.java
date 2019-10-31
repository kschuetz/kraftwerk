package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface LegacySeed {

    Result<? extends LegacySeed, Boolean> nextBoolean();

    Result<? extends LegacySeed, Double> nextDouble();

    Result<? extends LegacySeed, Float> nextFloat();

    Result<? extends LegacySeed, Integer> nextInt();

    Result<? extends LegacySeed, Integer> nextIntBounded(int bound);

    Result<? extends LegacySeed, Integer> nextIntExclusive(int origin, int bound);

    Result<? extends LegacySeed, Integer> nextIntBetween(int min, int max);

    Result<? extends LegacySeed, Long> nextLong();

    Result<? extends LegacySeed, Long> nextLongBounded(long bound);

    Result<? extends LegacySeed, Long> nextLongExclusive(long origin, long bound);

    Result<? extends LegacySeed, Long> nextLongBetween(long min, long max);

    Result<? extends LegacySeed, Unit> nextBytes(byte[] dest);

    Result<? extends LegacySeed, Double> nextGaussian();

    LegacySeed perturb(long value);

    long getSeedValue();

}
