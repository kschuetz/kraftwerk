package dev.marksman.composablerandom.random;

import com.jnape.palatable.lambda.adt.Unit;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.Result.result;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CacheNextGaussian implements RandomState {
    private final StandardGen inner;
    private final double nextGaussian;

    @Override
    public final Result<CacheNextGaussian, Integer> nextInt() {
        return wrap(inner.nextInt());
    }

    @Override
    public final Result<CacheNextGaussian, Integer> nextIntBounded(int bound) {
        return wrap(inner.nextIntBounded(bound));
    }

    @Override
    public final Result<CacheNextGaussian, Integer> nextIntExclusive(int origin, int bound) {
        return wrap(inner.nextIntExclusive(origin, bound));
    }

    @Override
    public final Result<CacheNextGaussian, Integer> nextIntBetween(int min, int max) {
        return wrap(inner.nextIntBetween(min, max));
    }

    @Override
    public final Result<CacheNextGaussian, Double> nextDouble() {
        return wrap(inner.nextDouble());
    }

    @Override
    public final Result<CacheNextGaussian, Float> nextFloat() {
        return wrap(inner.nextFloat());
    }

    @Override
    public final Result<CacheNextGaussian, Long> nextLong() {
        return wrap(inner.nextLong());
    }

    @Override
    public final Result<CacheNextGaussian, Long> nextLongBounded(long bound) {
        return wrap(inner.nextLongBounded(bound));
    }

    @Override
    public final Result<CacheNextGaussian, Long> nextLongExclusive(long origin, long bound) {
        return wrap(inner.nextLongExclusive(origin, bound));
    }

    @Override
    public final Result<CacheNextGaussian, Long> nextLongBetween(long min, long max) {
        return wrap(inner.nextLongBetween(min, max));
    }

    @Override
    public final Result<CacheNextGaussian, Boolean> nextBoolean() {
        return wrap(inner.nextBoolean());
    }

    @Override
    public final Result<CacheNextGaussian, Unit> nextBytes(byte[] dest) {
        return wrap(inner.nextBytes(dest));
    }

    @Override
    public final Result<StandardGen, Double> nextGaussian() {
        return result(inner, nextGaussian);
    }

    private <A> Result<CacheNextGaussian, A> wrap(Result<StandardGen, A> result) {
        return result(cacheNextGaussian(result._1(), nextGaussian), result._2());
    }

    static CacheNextGaussian cacheNextGaussian(StandardGen seed, double nextGaussian) {
        return new CacheNextGaussian(seed, nextGaussian);
    }

}
