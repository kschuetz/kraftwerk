package dev.marksman.random;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.product.Product2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.adt.product.Product2.product;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CacheNextGaussian implements RandomGen {

    private final StandardGen inner;
    private final double nextGaussian;

    @Override
    public final Product2<Integer, ? extends RandomGen> nextInt(int bound) {
        return wrap(inner.nextInt(bound));
    }

    @Override
    public final Product2<Integer, ? extends RandomGen> nextInt() {
        return wrap(inner.nextInt());
    }

    @Override
    public final Product2<Double, ? extends RandomGen> nextDouble() {
        return wrap(inner.nextDouble());
    }

    @Override
    public final Product2<Float, ? extends RandomGen> nextFloat() {
        return wrap(inner.nextFloat());
    }

    @Override
    public final Product2<Long, ? extends RandomGen> nextLong() {
        return wrap(inner.nextLong());
    }

    @Override
    public final Product2<Boolean, ? extends RandomGen> nextBoolean() {
        return wrap(inner.nextBoolean());
    }

    @Override
    public final Product2<Unit, ? extends RandomGen> nextBytes(byte[] dest) {
        return wrap(inner.nextBytes(dest));
    }

    @Override
    public final Product2<Double, ? extends RandomGen> nextGaussian() {
        return product(nextGaussian, inner);
    }

    private <A> Product2<A, CacheNextGaussian> wrap(Product2<A, StandardGen> result) {
        return product(result._1(), cacheNextGaussian(result._2(), nextGaussian));
    }

    static CacheNextGaussian cacheNextGaussian(StandardGen seed, double nextGaussian) {
        return new CacheNextGaussian(seed, nextGaussian);
    }

}
