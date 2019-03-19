package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.product.Product2;

public interface RandomGen {

    Product2<? extends RandomGen, Integer> nextInt(int bound);

    Product2<? extends RandomGen, Integer> nextInt();

    Product2<? extends RandomGen, Double> nextDouble();

    Product2<? extends RandomGen, Float> nextFloat();

    Product2<? extends RandomGen, Long> nextLong();

    Product2<? extends RandomGen, Boolean> nextBoolean();

    Product2<? extends RandomGen, Unit> nextBytes(byte[] dest);

    Product2<? extends RandomGen, Double> nextGaussian();

}
