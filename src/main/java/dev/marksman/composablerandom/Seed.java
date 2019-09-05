package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface Seed {

    Result<? extends Seed, Boolean> nextBoolean();

    Result<? extends Seed, Double> nextDouble();

    Result<? extends Seed, Float> nextFloat();

    Result<? extends Seed, Integer> nextInt();

    Result<? extends Seed, Integer> nextIntBounded(int bound);

    Result<? extends Seed, Integer> nextIntExclusive(int origin, int bound);

    Result<? extends Seed, Integer> nextIntBetween(int min, int max);

    Result<? extends Seed, Long> nextLong();

    Result<? extends Seed, Long> nextLongBounded(long bound);

    Result<? extends Seed, Long> nextLongExclusive(long origin, long bound);

    Result<? extends Seed, Long> nextLongBetween(long min, long max);

    Result<? extends Seed, Unit> nextBytes(byte[] dest);

    Result<? extends Seed, Double> nextGaussian();

    Seed perturb(long value);

}
