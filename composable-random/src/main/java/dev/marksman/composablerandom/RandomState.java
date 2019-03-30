package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface RandomState {

    Result<? extends RandomState, Boolean> nextBoolean();

    Result<? extends RandomState, Double> nextDouble();

    Result<? extends RandomState, Float> nextFloat();

    Result<? extends RandomState, Integer> nextInt();

    Result<? extends RandomState, Integer> nextInt(int bound);

    Result<? extends RandomState, Integer> nextIntExclusive(int origin, int bound);

    Result<? extends RandomState, Integer> nextIntBetween(int min, int max);

    Result<? extends RandomState, Long> nextLong();

    Result<? extends RandomState, Long> nextLong(long bound);

    Result<? extends RandomState, Long> nextLongExclusive(long origin, long bound);

    Result<? extends RandomState, Long> nextLongBetween(long min, long max);

    Result<? extends RandomState, Unit> nextBytes(byte[] dest);

    Result<? extends RandomState, Double> nextGaussian();

}
