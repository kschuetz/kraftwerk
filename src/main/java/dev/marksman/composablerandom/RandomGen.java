package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface RandomGen {

    Result<? extends RandomGen, Integer> nextInt(int bound);

    Result<? extends RandomGen, Integer> nextInt();

    Result<? extends RandomGen, Double> nextDouble();

    Result<? extends RandomGen, Float> nextFloat();

    Result<? extends RandomGen, Long> nextLong();

    Result<? extends RandomGen, Boolean> nextBoolean();

    Result<? extends RandomGen, Unit> nextBytes(byte[] dest);

    Result<? extends RandomGen, Double> nextGaussian();

}
