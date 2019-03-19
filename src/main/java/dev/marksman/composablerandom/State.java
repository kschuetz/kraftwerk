package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;

public interface State {

    Result<? extends State, Integer> nextInt(int bound);

    Result<? extends State, Integer> nextInt();

    Result<? extends State, Double> nextDouble();

    Result<? extends State, Float> nextFloat();

    Result<? extends State, Long> nextLong();

    Result<? extends State, Boolean> nextBoolean();

    Result<? extends State, Unit> nextBytes(byte[] dest);

    Result<? extends State, Double> nextGaussian();

}
