package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

public abstract class Instruction<A> {

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pure<A> extends Instruction<A> {
        private final A value;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Custom<A> extends Instruction<A> {
        private final Fn1<? super RandomState, Result<RandomState, A>> fn;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mapped<In, A> extends Instruction<A> {
        private final Fn1<In, A> fn;
        private final Instruction<In> operand;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FlatMapped<In, A> extends Instruction<A> {
        private final Fn1<? super In, ? extends Instruction<A>> fn;
        private final Instruction<In> operand;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBoolean extends Instruction<Boolean> {
        private static final NextBoolean INSTANCE = new NextBoolean();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextDouble extends Instruction<Double> {
        private static final NextDouble INSTANCE = new NextDouble();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextFloat extends Instruction<Float> {
        private static final NextFloat INSTANCE = new NextFloat();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextInt extends Instruction<Integer> {
        private static final NextInt INSTANCE = new NextInt();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBounded extends Instruction<Integer> implements HasIntExclusiveBound {
        private final int bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntExclusive extends Instruction<Integer> implements HasIntExclusiveRange {
        private final int origin;
        private final int bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBetween extends Instruction<Integer> implements HasIntInclusiveRange {
        private final int min;
        private final int max;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntIndex extends Instruction<Integer> implements HasIntExclusiveBound {
        private final int bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLong extends Instruction<Long> {
        private static final NextLong INSTANCE = new NextLong();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBounded extends Instruction<Long> implements HasLongExclusiveBound {
        private final long bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongExclusive extends Instruction<Long> implements HasLongExclusiveRange {
        private final long origin;
        private final long bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBetween extends Instruction<Long> implements HasLongInclusiveRange {
        private final long min;
        private final long max;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongIndex extends Instruction<Long> implements HasLongExclusiveBound {
        private final long bound;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextGaussian extends Instruction<Double> {
        private static final NextGaussian INSTANCE = new NextGaussian();
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBytes extends Instruction<Byte[]> implements HasIntCount {
        private final int count;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sized<A> extends Instruction<A> {
        private final Fn1<Integer, Instruction<A>> fn;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Labeled<A> extends Instruction<A> {
        private final String label;
        private final Instruction<A> operand;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Aggregate<A, Builder, Result> extends Instruction<Result> {
        private final Supplier<Builder> initialBuilderSupplier;
        private final Fn2<Builder, A, Builder> addFn;
        private final Fn1<Builder, Result> buildFn;
        private final Iterable<Instruction<A>> instructions;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product8<A, B, C, D, E, F, G, H> extends Instruction<Tuple8<A, B, C, D, E, F, G, H>> {
        private final Instruction<A> a;
        private final Instruction<B> b;
        private final Instruction<C> c;
        private final Instruction<D> d;
        private final Instruction<E> e;
        private final Instruction<F> f;
        private final Instruction<G> g;
        private final Instruction<H> h;
    }

    public static <A> Pure<A> pure(A a) {
        return new Pure<>(a);
    }

    public static <A> Custom<A> custom(Function<? super RandomState, Result> fn) {
        return custom(fn::apply);
    }

    public static <A, B> Mapped<A, B> mapped(Function<? super A, ? extends B> fn, Instruction<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    public static <A, B> FlatMapped<A, B> flatMapped(Function<? super A, ? extends Instruction<B>> fn, Instruction<A> operand) {
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

    public static <A> Sized<A> sized(Function<Integer, Instruction<A>> fn) {
        return new Sized<>(fn::apply);
    }

    public static <A> Labeled<A> labeled(String label, Instruction<A> operand) {
        return new Labeled<>(label, operand);
    }

    public static <A, Builder, Out> Aggregate<A, Builder, Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                         Fn2<Builder, A, Builder> addFn,
                                                                         Fn1<Builder, Out> buildFn,
                                                                         Iterable<Instruction<A>> instructions) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, instructions);
    }

    public static <A, Builder, Out> Aggregate<A, Builder, Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                         Fn2<Builder, A, Builder> addFn,
                                                                         Fn1<Builder, Out> buildFn,
                                                                         int size,
                                                                         Instruction<A> instruction) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, instruction));
    }

    public static <A, C extends Collection<A>> Aggregate<A, C, C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                                  Iterable<Instruction<A>> instructions) {
        return new Aggregate<>(initialCollectionSupplier,
                (collection, item) -> {
                    collection.add(item);
                    return collection;
                }, id(), instructions);
    }

    public static <A, C extends Collection<A>> Aggregate<A, C, C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                                  int size,
                                                                                  Instruction<A> instruction) {
        return buildCollection(initialCollectionSupplier, replicate(size, instruction));
    }

    public static <A, B, C, D, E, F, G, H> Product8<A, B, C, D, E, F, G, H> product8(Instruction<A> a,
                                                                                     Instruction<B> b,
                                                                                     Instruction<C> c,
                                                                                     Instruction<D> d,
                                                                                     Instruction<E> e,
                                                                                     Instruction<F> f,
                                                                                     Instruction<G> g,
                                                                                     Instruction<H> h) {
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
