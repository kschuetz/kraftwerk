package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface EntropySource {

    Result<? extends EntropySource, Integer> nextInt(int bound);

    Result<? extends EntropySource, Integer> nextInt();

    Result<? extends EntropySource, Double> nextDouble();

    Result<? extends EntropySource, Float> nextFloat();

    Result<? extends EntropySource, Long> nextLong();

    Result<? extends EntropySource, Boolean> nextBoolean();

    Result<? extends EntropySource, Unit> nextBytes(byte[] dest);

    Result<? extends EntropySource, Double> nextGaussian();

}
