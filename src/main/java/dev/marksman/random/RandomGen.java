package dev.marksman.random;

import com.jnape.palatable.lambda.adt.product.Product2;

public interface RandomGen {
    Product2<Integer, ? extends RandomGen> nextInt(int bound);

    default Product2<Integer, ? extends RandomGen> nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    Product2<Double, ? extends RandomGen> nextDouble();

    Product2<Float, ? extends RandomGen> nextFloat();

    Product2<Long, ? extends RandomGen> nextLong();

    Product2<Boolean, ? extends RandomGen> nextBoolean();

    Product2<Byte, ? extends RandomGen> nextByte();

    Product2<Short, ? extends RandomGen> nextShort();

    Product2<Byte[], ? extends RandomGen> nextBytes(byte[] dest);

}
