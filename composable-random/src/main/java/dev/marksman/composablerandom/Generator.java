package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

public abstract class Generator<A> implements Monad<A, Generator<?>> {

    @SuppressWarnings("unchecked")
    @Override
    public <B> Generator<B> flatMap(Function<? super A, ? extends Monad<B, Generator<?>>> f) {
        return flatMapped((Function<? super A, ? extends Generator<B>>) f, this);
    }

    @Override
    public <B> Generator<B> fmap(Function<? super A, ? extends B> fn) {
        return mapped(fn, this);
    }

    @Override
    public <B> Generator<B> pure(B b) {
        return constant(b);
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constant<A> extends Generator<A> {
        private final A value;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Custom<A> extends Generator<A> {
        private final Fn1<? super RandomState, Result<RandomState, A>> fn;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mapped<In, A> extends Generator<A> {
        private final Fn1<In, A> fn;
        private final Generator<In> operand;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FlatMapped<In, A> extends Generator<A> {
        private final Fn1<? super In, ? extends Generator<A>> fn;
        private final Generator<In> operand;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBoolean extends Generator<Boolean> {
        private static final NextBoolean INSTANCE = new NextBoolean();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextDouble extends Generator<Double> {
        private static final NextDouble INSTANCE = new NextDouble();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextFloat extends Generator<Float> {
        private static final NextFloat INSTANCE = new NextFloat();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextInt extends Generator<Integer> {
        private static final NextInt INSTANCE = new NextInt();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBounded extends Generator<Integer> implements HasIntExclusiveBound {
        private final int bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntExclusive extends Generator<Integer> implements HasIntExclusiveRange {
        private final int origin;
        private final int bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBetween extends Generator<Integer> implements HasIntInclusiveRange {
        private final int min;
        private final int max;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntIndex extends Generator<Integer> implements HasIntExclusiveBound {
        private final int bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLong extends Generator<Long> {
        private static final NextLong INSTANCE = new NextLong();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBounded extends Generator<Long> implements HasLongExclusiveBound {
        private final long bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongExclusive extends Generator<Long> implements HasLongExclusiveRange {
        private final long origin;
        private final long bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBetween extends Generator<Long> implements HasLongInclusiveRange {
        private final long min;
        private final long max;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongIndex extends Generator<Long> implements HasLongExclusiveBound {
        private final long bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextGaussian extends Generator<Double> {
        private static final NextGaussian INSTANCE = new NextGaussian();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBytes extends Generator<Byte[]> implements HasIntCount {
        private final int count;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sized<A> extends Generator<A> {
        private final Fn1<Integer, Generator<A>> fn;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Labeled<A> extends Generator<A> {
        private final String label;
        private final Generator<A> operand;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Aggregate<A, Builder, Result> extends Generator<Result> {
        private final Supplier<Builder> initialBuilderSupplier;
        private final Fn2<Builder, A, Builder> addFn;
        private final Fn1<Builder, Result> buildFn;
        private final Iterable<Generator<A>> instructions;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product8<A, B, C, D, E, F, G, H> extends Generator<Tuple8<A, B, C, D, E, F, G, H>> {
        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Generator<H> h;
    }

    public static <A> Constant<A> constant(A a) {
        return new Constant<>(a);
    }

    public static <A> Custom<A> custom(Function<? super RandomState, Result> fn) {
        return new Custom<A>(fn::apply);
    }

    public static <A, B> Mapped<A, B> mapped(Function<? super A, ? extends B> fn, Generator<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    public static <A, B> FlatMapped<A, B> flatMapped(Function<? super A, ? extends Generator<B>> fn, Generator<A> operand) {
        return new FlatMapped<>(fn::apply, operand);
    }

    public static NextBoolean nextBoolean() {
        return NextBoolean.INSTANCE;
    }

    public static NextDouble nextDouble() {
        return NextDouble.INSTANCE;
    }

    public static NextFloat nextFloat() {
        return NextFloat.INSTANCE;
    }

    public static NextInt nextInt() {
        return NextInt.INSTANCE;
    }

    public static NextIntBounded nextIntBounded(int bound) {
        checkBound(bound);
        return new NextIntBounded(bound);
    }

    public static NextIntExclusive nextIntExclusive(int origin, int bound) {
        checkOriginBound(origin, bound);
        return new NextIntExclusive(origin, bound);
    }

    public static NextIntBetween nextIntBetween(int min, int max) {
        checkMinMax(min, max);
        return new NextIntBetween(min, max);
    }

    public static NextIntIndex nextIntIndex(int bound) {
        checkBound(bound);
        return new NextIntIndex(bound);
    }

    public static NextLong nextLong() {
        return NextLong.INSTANCE;
    }

    public static NextLongBounded nextLongBounded(long bound) {
        checkBound(bound);
        return new NextLongBounded(bound);
    }

    public static NextLongExclusive nextLongExclusive(long origin, long bound) {
        checkOriginBound(origin, bound);
        return new NextLongExclusive(origin, bound);
    }

    public static NextLongBetween nextLongBetween(long min, long max) {
        checkMinMax(min, max);
        return new NextLongBetween(min, max);
    }

    public static NextLongIndex nextLongIndex(long bound) {
        checkBound(bound);
        return new NextLongIndex(bound);
    }

    public static NextGaussian nextGaussian() {
        return NextGaussian.INSTANCE;
    }

    public static NextBytes nextBytes(int count) {
        checkCount(count);
        return new NextBytes(count);
    }

    public static <A> Sized<A> sized(Function<Integer, Generator<A>> fn) {
        return new Sized<>(fn::apply);
    }

    public static <A> Labeled<A> labeled(String label, Generator<A> operand) {
        return new Labeled<>(label, operand);
    }

    public static <A, Builder, Out> Aggregate<A, Builder, Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                         Fn2<Builder, A, Builder> addFn,
                                                                         Fn1<Builder, Out> buildFn,
                                                                         Iterable<Generator<A>> instructions) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, instructions);
    }

    public static <A, Builder, Out> Aggregate<A, Builder, Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                         Fn2<Builder, A, Builder> addFn,
                                                                         Fn1<Builder, Out> buildFn,
                                                                         int size,
                                                                         Generator<A> generator) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, generator));
    }

    public static <A, C extends Collection<A>> Aggregate<A, C, C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                                  Iterable<Generator<A>> instructions) {
        return new Aggregate<>(initialCollectionSupplier,
                (collection, item) -> {
                    collection.add(item);
                    return collection;
                }, id(), instructions);
    }

    public static <A, C extends Collection<A>> Aggregate<A, C, C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                                  int size,
                                                                                  Generator<A> generator) {
        return buildCollection(initialCollectionSupplier, replicate(size, generator));
    }

    public static <A, B, C, D, E, F, G, H> Product8<A, B, C, D, E, F, G, H> product8(Generator<A> a,
                                                                                     Generator<B> b,
                                                                                     Generator<C> c,
                                                                                     Generator<D> d,
                                                                                     Generator<E> e,
                                                                                     Generator<F> f,
                                                                                     Generator<G> g,
                                                                                     Generator<H> h) {
        return new Product8<>(a, b, c, d, e, f, g, h);
    }

    private static void checkBound(long bound) {
        if (bound < 1) throw new IllegalArgumentException("bound must be > 0");
    }

    private static void checkOriginBound(long origin, long bound) {
        if (origin >= bound) throw new IllegalArgumentException("bound must be > origin");
    }

    private static void checkMinMax(long min, long max) {
        if (min > max) throw new IllegalArgumentException("max must be >= min");
    }

    private static void checkCount(int count) {
        if (count < 0) throw new IllegalArgumentException("count must be >= 0");
    }

}
