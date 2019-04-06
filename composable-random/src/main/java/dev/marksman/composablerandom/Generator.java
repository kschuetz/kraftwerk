package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.composablerandom.util.Labeling;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Maybe.*;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

public abstract class Generator<A> implements Monad<A, Generator<?>>, ToGenerator<A> {
    private Generator() {
    }

    public Maybe<String> getLabel() {
        return nothing();
    }

    public Maybe<Object> getApplicationData() {
        return nothing();
    }

    public boolean isPrimitive() {
        return true;
    }

    public final Generator<A> labeled(String label) {
        return withMetadata(maybe(label), this.getApplicationData(), this);
    }

    public final Generator<A> attachApplicationData(Object applicationData) {
        return withMetadata(getLabel(), maybe(applicationData), this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <B> Generator<B> flatMap(Function<? super A, ? extends Monad<B, Generator<?>>> f) {
        return flatMapped((Function<? super A, ? extends Generator<B>>) f, this);
    }

    @Override
    public final <B> Generator<B> fmap(Function<? super A, ? extends B> fn) {
        return mapped(fn, this);
    }

    @Override
    public final <B> Generator<B> pure(B b) {
        return constant(b);
    }

    @Override
    public final Generator<A> toGenerator() {
        return this;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constant<A> extends Generator<A> {
        private static Maybe<String> LABEL = just("constant");

        private final A value;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Custom<A> extends Generator<A> {
        private static Maybe<String> LABEL = just("custom");

        private final Fn1<? super RandomState, Result<RandomState, A>> fn;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = just("fmap");

        private final Fn1<In, A> fn;
        private final Generator<In> operand;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FlatMapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = just("flatMap");

        private final Fn1<? super In, ? extends Generator<A>> fn;
        private final Generator<In> operand;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBoolean extends Generator<Boolean> {
        private static Maybe<String> LABEL = just("boolean");

        private static final NextBoolean INSTANCE = new NextBoolean();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextDouble extends Generator<Double> {
        private static Maybe<String> LABEL = just("double");

        private static final NextDouble INSTANCE = new NextDouble();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextFloat extends Generator<Float> {
        private static Maybe<String> LABEL = just("float");

        private static final NextFloat INSTANCE = new NextFloat();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextInt extends Generator<Integer> {
        private static Maybe<String> LABEL = just("int");

        private static final NextInt INSTANCE = new NextInt();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBounded extends Generator<Integer> {
        private final int bound;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.intInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntExclusive extends Generator<Integer> {
        private final int origin;
        private final int bound;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.intInterval(origin, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntBetween extends Generator<Integer> {
        private final int min;
        private final int max;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.intInterval(min, max, false));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntIndex extends Generator<Integer> {
        private final int bound;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.interval("index", 0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLong extends Generator<Long> {
        private static Maybe<String> LABEL = just("long");

        private static final NextLong INSTANCE = new NextLong();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBounded extends Generator<Long> {
        private final long bound;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.longInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongExclusive extends Generator<Long> {
        private final long origin;
        private final long bound;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.longInterval(origin, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongBetween extends Generator<Long> {
        private final long min;
        private final long max;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.longInterval(min, max, false));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongIndex extends Generator<Long> {
        private final long bound;

        @Override
        public Maybe<String> getLabel() {
            return just(Labeling.longInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextGaussian extends Generator<Double> {
        private static Maybe<String> LABEL = just("gaussian");

        private static final NextGaussian INSTANCE = new NextGaussian();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBytes extends Generator<Byte[]> {
        private final int count;

        @Override
        public Maybe<String> getLabel() {
            return just("bytes[" + count + "]");
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sized<A> extends Generator<A> {
        private static Maybe<String> LABEL = just("sized");

        private final Fn1<Integer, Generator<A>> fn;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WithMetadata<A> extends Generator<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generator<A> operand;

        @Override
        public boolean isPrimitive() {
            return false;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Aggregate<Elem, Builder, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = just("aggregate");

        private final Supplier<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generator<Elem>> instructions;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product8<A, B, C, D, E, F, G, H> extends Generator<Tuple8<A, B, C, D, E, F, G, H>> {
        private static Maybe<String> LABEL = just("constant");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Generator<H> h;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    private static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        if (operand instanceof WithMetadata) {
            WithMetadata<A> target1 = (WithMetadata<A>) operand;
            return new WithMetadata<>(label, applicationData, target1.getOperand());
        } else {
            return new WithMetadata<>(label, applicationData, operand);
        }
    }

    public static <A> Generator<A> constant(A a) {
        return new Constant<>(a);
    }

    public static <A> Generator<A> generator(Function<? super RandomState, Result> fn) {
        return new Custom<A>(fn::apply);
    }

    private static <A, B> Generator<B> mapped(Function<? super A, ? extends B> fn, Generator<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    private static <A, B> Generator<B> flatMapped(Function<? super A, ? extends Generator<B>> fn, Generator<A> operand) {
        return new FlatMapped<>(fn::apply, operand);
    }

    public static Generator<Boolean> generateBoolean() {
        return NextBoolean.INSTANCE;
    }

    public static Generator<Double> generateDouble() {
        return NextDouble.INSTANCE;
    }

    public static Generator<Float> generateFloat() {
        return NextFloat.INSTANCE;
    }

    public static Generator<Integer> generateInt() {
        return NextInt.INSTANCE;
    }

    public static Generator<Integer> generateInt(int min, int max) {
        checkMinMax(min, max);
        return new NextIntBetween(min, max);
    }

    public static Generator<Integer> generateIntExclusive(int bound) {
        checkBound(bound);
        return new NextIntBounded(bound);
    }

    public static Generator<Integer> generateIntExclusive(int origin, int bound) {
        checkOriginBound(origin, bound);
        return new NextIntExclusive(origin, bound);
    }

    public static Generator<Integer> generateIntIndex(int bound) {
        checkBound(bound);
        return new NextIntIndex(bound);
    }

    public static Generator<Long> generateLong() {
        return NextLong.INSTANCE;
    }

    public static Generator<Long> generateLong(long min, long max) {
        checkMinMax(min, max);
        return new NextLongBetween(min, max);
    }

    public static Generator<Long> generateLongExclusive(long bound) {
        checkBound(bound);
        return new NextLongBounded(bound);
    }

    public static Generator<Long> generateLongExclusive(long origin, long bound) {
        checkOriginBound(origin, bound);
        return new NextLongExclusive(origin, bound);
    }

    public static Generator<Long> generateLongIndex(long bound) {
        checkBound(bound);
        return new NextLongIndex(bound);
    }

    public static Generator<Double> generateGaussian() {
        return NextGaussian.INSTANCE;
    }

    public static Generator<Byte[]> generateBytes(int count) {
        checkCount(count);
        return new NextBytes(count);
    }

    public static <A> Generator<A> sized(Function<Integer, Generator<A>> fn) {
        return new Sized<>(fn::apply);
    }

    public static <A, Builder, Out> Aggregate<A, Builder, Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                         Fn2<Builder, A, Builder> addFn,
                                                                         Fn1<Builder, Out> buildFn,
                                                                         Iterable<Generator<A>> instructions) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, instructions);
    }

    public static <Elem, Builder, Out> Generator<Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                Fn2<Builder, Elem, Builder> addFn,
                                                                Fn1<Builder, Out> buildFn,
                                                                int size,
                                                                Generator<Elem> generator) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, generator));
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                            Iterable<Generator<A>> instructions) {
        return new Aggregate<>(initialCollectionSupplier,
                (collection, item) -> {
                    collection.add(item);
                    return collection;
                }, id(), instructions);
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                            int size,
                                                                            Generator<A> generator) {
        return buildCollection(initialCollectionSupplier, replicate(size, generator));
    }

    public static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> a,
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
