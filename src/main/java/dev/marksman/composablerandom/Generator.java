package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.*;
import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.functions.*;
import com.jnape.palatable.lambda.monad.Monad;
import dev.marksman.collectionviews.*;
import dev.marksman.composablerandom.choice.ChoiceBuilder1;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import dev.marksman.composablerandom.util.Labeling;
import dev.marksman.enhancediterables.NonEmptyIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

public abstract class Generator<A> implements Monad<A, Generator<?>>, ToGenerator<A> {
    private Generator() {
    }

    @Override
    public final <B> Generator<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mapped(fn, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <B> Generator<B> flatMap(Fn1<? super A, ? extends Monad<B, Generator<?>>> f) {
        return flatMapped((Fn1<? super A, ? extends Generator<B>>) f, this);
    }

    @Override
    public final <B> Generator<B> pure(B b) {
        return constant(b);
    }

    @Override
    public final Generator<A> toGenerator() {
        return this;
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
        return withMetadata(Maybe.maybe(label), this.getApplicationData(), this);
    }

    public final Generator<A> attachApplicationData(Object applicationData) {
        return withMetadata(getLabel(), Maybe.maybe(applicationData), this);
    }

    public final Generator<Tuple2<A, A>> pair() {
        return tupled(this, this);
    }

    public final Generator<Tuple3<A, A, A>> triple() {
        return tupled(this, this, this);
    }

    public final Generator<Maybe<A>> just() {
        return generateJust(this);
    }

    public final Generator<Maybe<A>> maybe() {
        return generateMaybe(this);
    }

    public final Generator<Maybe<A>> maybe(MaybeWeights weights) {
        return generateMaybe(weights, this);
    }

    public final <R> Generator<Either<A, R>> left() {
        return CoProducts.generateLeft(this);
    }

    public final <L> Generator<Either<L, A>> right() {
        return CoProducts.generateRight(this);
    }

    public final Generator<ArrayList<A>> list() {
        return generateList(this);
    }

    public final Generator<ArrayList<A>> listOfN(int count) {
        return generateListOfN(count, this);
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constant<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("constant");

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
        private static Maybe<String> LABEL = Maybe.just("custom");

        private final Fn1<? super RandomState, Result<RandomState, A>> fn;

        @Override
        public boolean isPrimitive() {
            return false;
        }

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Mapped<In, A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

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
        private static Maybe<String> LABEL = Maybe.just("flatMap");

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
        private static Maybe<String> LABEL = Maybe.just("boolean");

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
        private static Maybe<String> LABEL = Maybe.just("double");

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
        private static Maybe<String> LABEL = Maybe.just("float");

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
        private static Maybe<String> LABEL = Maybe.just("int");

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
            return Maybe.just(Labeling.intInterval(0, bound, true));
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
            return Maybe.just(Labeling.intInterval(origin, bound, true));
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
            return Maybe.just(Labeling.intInterval(min, max, false));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntIndex extends Generator<Integer> {
        private final int bound;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.interval("index", 0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLong extends Generator<Long> {
        private static Maybe<String> LABEL = Maybe.just("long");

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
            return Maybe.just(Labeling.longInterval(0, bound, true));
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
            return Maybe.just(Labeling.longInterval(origin, bound, true));
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
            return Maybe.just(Labeling.longInterval(min, max, false));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongIndex extends Generator<Long> {
        private final long bound;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextGaussian extends Generator<Double> {
        private static Maybe<String> LABEL = Maybe.just("gaussian");

        private static final NextGaussian INSTANCE = new NextGaussian();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextByte extends Generator<Byte> {
        private static Maybe<String> LABEL = Maybe.just("byte");

        private static final NextByte INSTANCE = new NextByte();

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextShort extends Generator<Short> {
        private static Maybe<String> LABEL = Maybe.just("short");

        private static final NextShort INSTANCE = new NextShort();

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
            return Maybe.just("bytes[" + count + "]");
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sized<A> extends Generator<A> {
        private static Maybe<String> LABEL = Maybe.just("sized");

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
        private static Maybe<String> LABEL = Maybe.just("aggregate");

        private final Supplier<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generator<Elem>> elements;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product2<A, B, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product2");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Fn2<A, B, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product3<A, B, C, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product3");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Fn3<A, B, C, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product4<A, B, C, D, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product4");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Fn4<A, B, C, D, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product5<A, B, C, D, E, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product5");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Fn5<A, B, C, D, E, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product6<A, B, C, D, E, F, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product6");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Fn6<A, B, C, D, E, F, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product7<A, B, C, D, E, F, G, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product7");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Fn7<A, B, C, D, E, F, G, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product8<A, B, C, D, E, F, G, H, Out> extends Generator<Out> {
        private static Maybe<String> LABEL = Maybe.just("product8");

        private final Generator<A> a;
        private final Generator<B> b;
        private final Generator<C> c;
        private final Generator<D> d;
        private final Generator<E> e;
        private final Generator<F> f;
        private final Generator<G> g;
        private final Generator<H> h;
        private final Fn8<A, B, C, D, E, F, G, H, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    public static <A> Generator<A> constant(A a) {
        return new Constant<>(a);
    }

    public static <A> Generator<A> generator(Function<? super RandomState, Result> fn) {
        return new Custom<A>(fn::apply);
    }

    public static Generator<Boolean> generateBoolean() {
        return NextBoolean.INSTANCE;
    }

    public static Generator<Boolean> generateBoolean(BooleanWeights weights) {
        return CoProducts.generateBoolean(weights);
    }

    public static Generator<Double> generateDouble() {
        return NextDouble.INSTANCE;
    }

    public static Generator<Double> generateDouble(double scale) {
        return generateDouble().fmap(n -> n * scale);
    }

    public static Generator<Double> generateDouble(double min, double max) {
        double scale = max - min;
        return generateDouble().fmap(n -> min + n * scale);
    }

    public static Generator<Float> generateFloat() {
        return NextFloat.INSTANCE;
    }

    public static Generator<Float> generateFloat(float scale) {
        return generateFloat().fmap(n -> n * scale);
    }

    public static Generator<Float> generateFloat(float min, float max) {
        float scale = max - min;
        return generateFloat().fmap(n -> min + n * scale);
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

    public static Generator<Byte> generateByte() {
        return NextByte.INSTANCE;
    }

    public static Generator<Short> generateShort() {
        return NextShort.INSTANCE;
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
                                                                         Iterable<Generator<A>> elements) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, elements);
    }

    public static <Elem, Builder, Out> Generator<Out> aggregate(Supplier<Builder> initialBuilderSupplier,
                                                                Fn2<Builder, Elem, Builder> addFn,
                                                                Fn1<Builder, Out> buildFn,
                                                                int size,
                                                                Generator<Elem> generator) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, generator));
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                            Iterable<Generator<A>> elements) {
        return new Aggregate<>(initialCollectionSupplier,
                (collection, item) -> {
                    collection.add(item);
                    return collection;
                }, id(), elements);
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Supplier<C> initialCollectionSupplier,
                                                                            int size,
                                                                            Generator<A> generator) {
        return buildCollection(initialCollectionSupplier, replicate(size, generator));
    }

    public static <A> Generator<ImmutableVector<A>> buildVector(Iterable<Generator<A>> elements) {
        return new Aggregate<>(Vector::<A>builder, VectorBuilder::add, VectorBuilder::build, elements);
    }

    public static <A> Generator<ImmutableVector<A>> buildVector(int size, Generator<A> generator) {
        return new Aggregate<A, VectorBuilder<A>, ImmutableVector<A>>(() -> Vector.builder(size), VectorBuilder::add,
                VectorBuilder::build, replicate(size, generator));
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(NonEmptyIterable<Generator<A>> elements) {
        return new Aggregate<>(Vector::<A>builder, VectorBuilder::add, b -> b.build().toNonEmptyOrThrow(), elements);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(int size, Generator<A> generator) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");

        }
        return new Aggregate<A, VectorBuilder<A>, ImmutableNonEmptyVector<A>>(() -> Vector.builder(size), VectorBuilder::add,
                aVectorBuilder -> aVectorBuilder.build().toNonEmptyOrThrow(),
                replicate(size, generator));
    }

    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Fn2<A, B, Out> combine) {
        return new Product2<>(a, b, combine);
    }

    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Generator<C> c,
                                                                       Fn3<A, B, C, Out> combine) {
        return new Product3<>(a, b, c, combine);
    }


    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Generator<C> c,
                                                                       Generator<D> d,
                                                                       Fn4<A, B, C, D, Out> combine) {
        return new Product4<>(a, b, c, d, combine);
    }


    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Generator<C> c,
                                                                       Generator<D> d,
                                                                       Generator<E> e,
                                                                       Fn5<A, B, C, D, E, Out> combine) {
        return new Product5<>(a, b, c, d, e, combine);
    }


    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Generator<C> c,
                                                                       Generator<D> d,
                                                                       Generator<E> e,
                                                                       Generator<F> f,
                                                                       Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6<>(a, b, c, d, e, f, combine);
    }

    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Generator<C> c,
                                                                       Generator<D> d,
                                                                       Generator<E> e,
                                                                       Generator<F> f,
                                                                       Generator<G> g,
                                                                       Fn7<A, B, C, D, E, F, G, Out> combine) {
        return new Product7<>(a, b, c, d, e, f, g, combine);
    }

    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> product(Generator<A> a,
                                                                       Generator<B> b,
                                                                       Generator<C> c,
                                                                       Generator<D> d,
                                                                       Generator<E> e,
                                                                       Generator<F> f,
                                                                       Generator<G> g,
                                                                       Generator<H> h,
                                                                       Fn8<A, B, C, D, E, F, G, H, Out> combine) {
        return new Product8<>(a, b, c, d, e, f, g, h, combine);
    }

    public static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> a,
                                                        Generator<B> b) {
        return new Product2<>(a, b, Tuple2::tuple);
    }

    public static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> a,
                                                              Generator<B> b,
                                                              Generator<C> c) {
        return new Product3<>(a, b, c, Tuple3::tuple);
    }

    public static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> a,
                                                                    Generator<B> b,
                                                                    Generator<C> c,
                                                                    Generator<D> d) {
        return new Product4<>(a, b, c, d, Tuple4::tuple);
    }

    public static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> a,
                                                                          Generator<B> b,
                                                                          Generator<C> c,
                                                                          Generator<D> d,
                                                                          Generator<E> e) {
        return new Product5<>(a, b, c, d, e, Tuple5::tuple);
    }

    public static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> a,
                                                                                Generator<B> b,
                                                                                Generator<C> c,
                                                                                Generator<D> d,
                                                                                Generator<E> e,
                                                                                Generator<F> f) {
        return new Product6<>(a, b, c, d, e, f, Tuple6::tuple);
    }

    public static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> a,
                                                                                      Generator<B> b,
                                                                                      Generator<C> c,
                                                                                      Generator<D> d,
                                                                                      Generator<E> e,
                                                                                      Generator<F> f,
                                                                                      Generator<G> g) {
        return new Product7<>(a, b, c, d, e, f, g, Tuple7::tuple);
    }

    public static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> a,
                                                                                            Generator<B> b,
                                                                                            Generator<C> c,
                                                                                            Generator<D> d,
                                                                                            Generator<E> e,
                                                                                            Generator<F> f,
                                                                                            Generator<G> g,
                                                                                            Generator<H> h) {
        return new Product8<>(a, b, c, d, e, f, g, h, Tuple8::tuple);
    }

    // TODO:  organize these


    public static Generator<String> generateString() {
        return Strings.generateString();
    }

    public static Generator<String> generateString(int length, Generator<String> g) {
        return Strings.generateString(length, g);
    }

    public static Generator<String> generateStringFromCharacters(int length, Generator<Character> g) {
        return Strings.generateStringFromCharacters(length, g);
    }

    public static Generator<String> generateStringFromCharacters(int length, NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(length, characters);
    }

    public static Generator<String> generateStringFromCharacters(Generator<Character> g) {
        return Strings.generateStringFromCharacters(g);
    }

    public static Generator<String> generateStringFromCharacters(NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(characters);
    }

    @SafeVarargs
    public static Generator<String> generateString(Generator<String> first, Generator<String>... more) {
        return Strings.generateString(first, more);
    }

    public static Generator<Unit> generateUnit() {
        return CoProducts.generateUnit();
    }

    public static Generator<Boolean> generateTrue() {
        return CoProducts.generateTrue();
    }

    public static Generator<Boolean> generateFalse() {
        return CoProducts.generateFalse();
    }

    public static <A> Generator<Maybe<A>> generateMaybe(MaybeWeights weights, Generator<A> g) {
        return CoProducts.generateMaybe(weights, g);
    }

    public static <A> Generator<Maybe<A>> generateMaybe(Generator<A> g) {
        return CoProducts.generateMaybe(g);
    }

    public static <A> Generator<Maybe<A>> generateJust(Generator<A> g) {
        return CoProducts.generateJust(g);
    }

    public static <A> Generator<Maybe<A>> generateNothing() {
        return CoProducts.generateNothing();
    }

    public static <L, R> Generator<Either<L, R>> generateEither(EitherWeights weights, Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return CoProducts.generateEither(weights, leftGenerator, rightGenerator);
    }

    public static <L, R> Generator<Either<L, R>> generateEither(Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return CoProducts.generateEither(leftGenerator, rightGenerator);
    }

    public static <L, R> Generator<Either<L, R>> generateLeft(Generator<L> g) {
        return CoProducts.generateLeft(g);
    }

    public static <L, R> Generator<Either<L, R>> generateRight(Generator<R> g) {
        return CoProducts.generateRight(g);
    }

    @SafeVarargs
    public static <A> Generator<A> chooseOneOf(Generator<A> first, Generator<? extends A>... more) {
        return Choose.chooseOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<A> chooseOneOfValues(A first, A... more) {
        return Choose.chooseOneOfValues(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return Choose.chooseAtLeastOneOfValues(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseSomeOfValues(A first, A... more) {
        return Choose.chooseSomeOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseSomeOf(first, more);
    }

    public static <A> Generator<A> chooseOneFromCollection(Collection<A> items) {
        return Choose.chooseOneFromCollection(items);
    }

    public static <A> Generator<A> chooseOneFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseOneFromDomain(domain);
    }

    public static <A> Generator<ArrayList<A>> chooseAtLeastOneFromCollection(Collection<A> items) {
        return Choose.chooseAtLeastOneFromCollection(items);
    }

    public static <A> Generator<ArrayList<A>> chooseAtLeastOneFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseAtLeastOneFromDomain(domain);
    }

    public static <A> Generator<ArrayList<A>> chooseSomeFromCollection(Collection<A> items) {
        return Choose.chooseSomeFromDomain(items);
    }

    public static <A> Generator<ArrayList<A>> chooseSomeFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseSomeFromDomain(domain);
    }

    public static <K, V> Generator<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        return Choose.chooseEntryFromMap(map);
    }

    public static <K, V> Generator<K> chooseKeyFromMap(Map<K, V> map) {
        return Choose.chooseKeyFromMap(map);
    }

    public static <K, V> Generator<V> chooseValueFromMap(Map<K, V> map) {
        return Choose.chooseValueFromMap(map);
    }

    public static <A> Generator<A> frequency(FrequencyMap<A> frequencyMap) {
        return Choose.frequency(frequencyMap);
    }

    @SafeVarargs
    public static <A> Generator<A> frequency(FrequencyEntry<A> first, FrequencyEntry<A>... more) {
        return Choose.frequency(first, more);
    }

    public static <A> Generator<A> frequency(Collection<FrequencyEntry<A>> entries) {
        return Choose.frequency(entries);
    }

    public static <A> Generator<ArrayList<A>> generateList(Generator<A> g) {
        return Collections.generateList(g);
    }

    public static <A> Generator<ArrayList<A>> generateNonEmptyList(Generator<A> g) {
        return Collections.generateNonEmptyList(g);
    }

    public static <A> Generator<ArrayList<A>> generateListOfN(int n, Generator<A> g) {
        return Collections.generateListOfN(n, g);
    }

    public static <A> Generator<HashSet<A>> generateSet(Generator<A> g) {
        return Collections.generateSet(g);
    }

    public static <A> Generator<HashSet<A>> generateNonEmptySet(Generator<A> g) {
        return Collections.generateNonEmptySet(g);
    }

    static <A> Generator<ImmutableVector<A>> generateVector(Generator<A> g) {
        return Collections.generateVector(g);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> g) {
        return Collections.generateNonEmptyVector(g);
    }

    static <A> Generator<ImmutableVector<A>> generateVectorOfN(int n, Generator<A> g) {
        return Collections.generateVectorOfN(n, g);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfN(int n, Generator<A> g) {
        return Collections.generateNonEmptyVectorOfN(n, g);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(Generator<K> keyGenerator,
                                                          Generator<V> valueGenerator) {
        return Collections.generateMap(keyGenerator, valueGenerator);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                          Generator<V> valueGenerator) {
        return Collections.generateMap(keys, valueGenerator);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                          Generator<V> valueGenerator) {
        return Collections.generateMap(keys, valueGenerator);
    }

    public static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> keyGenerator,
                                                                  Generator<V> valueGenerator) {
        return Collections.generateNonEmptyMap(keyGenerator, valueGenerator);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(int weight, Generator<A> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(weight, firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(Generator<A> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(FrequencyEntry<A> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(int weight, A firstChoice) {
        return ChoiceBuilder1.choiceBuilderValue(weight, firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return ChoiceBuilder1.choiceBuilderValue(firstChoice);
    }

    public static <A, B> Generator<Choice2<A, B>> generateChoice(FrequencyEntry<A> a,
                                                                 FrequencyEntry<B> b) {
        return choiceBuilder(a).or(b).toGenerator();
    }

    public static <A, B, C> Generator<Choice3<A, B, C>> generateChoice(FrequencyEntry<A> a,
                                                                       FrequencyEntry<B> b,
                                                                       FrequencyEntry<C> c) {
        return choiceBuilder(a).or(b).or(c).toGenerator();
    }

    public static <A, B, C, D> Generator<Choice4<A, B, C, D>> generateChoice(FrequencyEntry<A> a,
                                                                             FrequencyEntry<B> b,
                                                                             FrequencyEntry<C> c,
                                                                             FrequencyEntry<D> d) {
        return choiceBuilder(a).or(b).or(c).or(d).toGenerator();
    }

    public static <A, B, C, D, E> Generator<Choice5<A, B, C, D, E>> generateChoice(FrequencyEntry<A> a,
                                                                                   FrequencyEntry<B> b,
                                                                                   FrequencyEntry<C> c,
                                                                                   FrequencyEntry<D> d,
                                                                                   FrequencyEntry<E> e) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).toGenerator();
    }

    public static <A, B, C, D, E, F> Generator<Choice6<A, B, C, D, E, F>> generateChoice(FrequencyEntry<A> a,
                                                                                         FrequencyEntry<B> b,
                                                                                         FrequencyEntry<C> c,
                                                                                         FrequencyEntry<D> d,
                                                                                         FrequencyEntry<E> e,
                                                                                         FrequencyEntry<F> f) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).toGenerator();
    }

    public static <A, B, C, D, E, F, G> Generator<Choice7<A, B, C, D, E, F, G>> generateChoice(FrequencyEntry<A> a,
                                                                                               FrequencyEntry<B> b,
                                                                                               FrequencyEntry<C> c,
                                                                                               FrequencyEntry<D> d,
                                                                                               FrequencyEntry<E> e,
                                                                                               FrequencyEntry<F> f,
                                                                                               FrequencyEntry<G> g) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).or(g).toGenerator();
    }

    public static <A, B, C, D, E, F, G, H> Generator<Choice8<A, B, C, D, E, F, G, H>> generateChoice(FrequencyEntry<A> a,
                                                                                                     FrequencyEntry<B> b,
                                                                                                     FrequencyEntry<C> c,
                                                                                                     FrequencyEntry<D> d,
                                                                                                     FrequencyEntry<E> e,
                                                                                                     FrequencyEntry<F> f,
                                                                                                     FrequencyEntry<G> g,
                                                                                                     FrequencyEntry<H> h) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).or(g).or(h).toGenerator();
    }

    public static Generator<BigInteger> generateBigInteger(BigInteger min, BigInteger max) {
        return BigNumbers.generateBigInteger(min, max);
    }

    public static Generator<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
        return BigNumbers.generateBigIntegerExclusive(bound);
    }

    public static Generator<BigInteger> generateBigIntegerExclusive(BigInteger origin, BigInteger bound) {
        return BigNumbers.generateBigIntegerExclusive(origin, bound);
    }

    public static Generator<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        return Temporal.generateLocalDate(min, max);
    }

    public static Generator<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        return Temporal.generateLocalDateExclusive(origin, bound);
    }

    public static Generator<LocalDate> generateLocalDateForYear(int year) {
        return Temporal.generateLocalDateForYear(year);
    }

    private static <A, B> Generator<B> mapped(Fn1<? super A, ? extends B> fn, Generator<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    private static <A, B> Generator<B> flatMapped(Fn1<? super A, ? extends Generator<B>> fn, Generator<A> operand) {
        return new FlatMapped<>(fn::apply, operand);
    }

    private static <A> Generator<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generator<A> operand) {
        if (operand instanceof WithMetadata) {
            WithMetadata<A> target1 = (WithMetadata<A>) operand;
            return new WithMetadata<>(label, applicationData, target1.getOperand());
        } else {
            return new WithMetadata<>(label, applicationData, operand);
        }
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
