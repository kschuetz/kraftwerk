package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.product.Product2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.jnape.palatable.lambda.adt.product.Product2.product;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CacheNextGaussian implements RandomGen {
    private final StandardGen inner;
    private final double nextGaussian;

    @Override
    public final Product2<CacheNextGaussian, Integer> nextInt(int bound) {
        return wrap(inner.nextInt(bound));
    }

    @Override
    public final Product2<CacheNextGaussian, Integer> nextInt() {
        return wrap(inner.nextInt());
    }

    @Override
    public final Product2<CacheNextGaussian, Double> nextDouble() {
        return wrap(inner.nextDouble());
    }

    @Override
    public final Product2<CacheNextGaussian, Float> nextFloat() {
        return wrap(inner.nextFloat());
    }

    @Override
    public final Product2<CacheNextGaussian, Long> nextLong() {
        return wrap(inner.nextLong());
    }

    @Override
    public final Product2<CacheNextGaussian, Boolean> nextBoolean() {
        return wrap(inner.nextBoolean());
    }

    @Override
    public final Product2<CacheNextGaussian, Unit> nextBytes(byte[] dest) {
        return wrap(inner.nextBytes(dest));
    }

    @Override
    public final Product2<StandardGen, Double> nextGaussian() {
        return product(inner, nextGaussian);
    }

    private <A> Product2<CacheNextGaussian, A> wrap(Product2<StandardGen, A> result) {
        return product(cacheNextGaussian(result._1(), nextGaussian), result._2());
    }

    static CacheNextGaussian cacheNextGaussian(StandardGen seed, double nextGaussian) {
        return new CacheNextGaussian(seed, nextGaussian);
    }

}
