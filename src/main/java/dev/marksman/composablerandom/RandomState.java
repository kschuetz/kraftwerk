package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface RandomState {

    Result<? extends RandomState, Integer> nextInt(int bound);

    Result<? extends RandomState, Integer> nextInt();

    Result<? extends RandomState, Double> nextDouble();

    Result<? extends RandomState, Float> nextFloat();

    Result<? extends RandomState, Long> nextLong();

    Result<? extends RandomState, Boolean> nextBoolean();

    Result<? extends RandomState, Unit> nextBytes(byte[] dest);

    Result<? extends RandomState, Double> nextGaussian();

}
