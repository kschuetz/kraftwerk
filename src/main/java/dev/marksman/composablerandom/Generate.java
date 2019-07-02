package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.*;
import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.functions.*;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.*;
import dev.marksman.composablerandom.choice.ChoiceBuilder1;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import dev.marksman.composablerandom.util.Labeling;
import dev.marksman.enhancediterables.FiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.enhancediterables.NonEmptyIterable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

public abstract class Generate<A> implements Monad<A, Generate<?>>, ToGenerate<A> {
    private Generate() {
    }

    @Override
    public final <B> Generate<B> fmap(Fn1<? super A, ? extends B> fn) {
        return mapped(fn, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <B> Generate<B> flatMap(Fn1<? super A, ? extends Monad<B, Generate<?>>> f) {
        return flatMapped((Fn1<? super A, ? extends Generate<B>>) f, this);
    }

    @Override
    public final <B> Generate<B> pure(B b) {
        return constant(b);
    }

    @Override
    public final Generate<A> toGenerate() {
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

    public final Generate<A> labeled(String label) {
        return withMetadata(Maybe.maybe(label), this.getApplicationData(), this);
    }

    public final Generate<A> attachApplicationData(Object applicationData) {
        return withMetadata(getLabel(), Maybe.maybe(applicationData), this);
    }

    public final Generate<Tuple2<A, A>> pair() {
        return tupled(this, this);
    }

    public final Generate<Tuple3<A, A, A>> triple() {
        return tupled(this, this, this);
    }

    public final Generate<Maybe<A>> just() {
        return generateJust(this);
    }

    public final Generate<Maybe<A>> maybe() {
        return generateMaybe(this);
    }

    public final Generate<Maybe<A>> maybe(MaybeWeights weights) {
        return generateMaybe(weights, this);
    }

    public final <R> Generate<Either<A, R>> left() {
        return CoProducts.generateLeft(this);
    }

    public final <L> Generate<Either<L, A>> right() {
        return CoProducts.generateRight(this);
    }

    public final Generate<A> withNulls() {
        return generateWithNulls(this);
    }

    public final Generate<A> withNulls(NullWeights weights) {
        return generateWithNulls(weights, this);
    }

    public final Generate<ArrayList<A>> arrayList() {
        return generateArrayList(this);
    }

    public final Generate<ArrayList<A>> nonEmptyArrayList() {
        return generateNonEmptyArrayList(this);
    }

    public final Generate<ArrayList<A>> arrayListOfN(int count) {
        return generateArrayListOfN(count, this);
    }

    public final Generate<ImmutableVector<A>> vector() {
        return generateVector(this);
    }

    public final Generate<ImmutableVector<A>> vectorOfN(int count) {
        return generateVectorOfN(count, this);
    }

    public final Generate<ImmutableNonEmptyVector<A>> nonEmptyVector() {
        return generateNonEmptyVector(this);
    }

    public final Generate<ImmutableNonEmptyVector<A>> nonEmptyVectorOfN(int count) {
        return generateNonEmptyVectorOfN(count, this);
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constant<A> extends Generate<A> {
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
    public static class Custom<A> extends Generate<A> {
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
    public static class Mapped<In, A> extends Generate<A> {
        private static Maybe<String> LABEL = Maybe.just("fmap");

        private final Fn1<In, A> fn;
        private final Generate<In> operand;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FlatMapped<In, A> extends Generate<A> {
        private static Maybe<String> LABEL = Maybe.just("flatMap");

        private final Fn1<? super In, ? extends Generate<A>> fn;
        private final Generate<In> operand;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextBoolean extends Generate<Boolean> {
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
    public static class NextDouble extends Generate<Double> {
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
    public static class NextFloat extends Generate<Float> {
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
    public static class NextInt extends Generate<Integer> {
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
    public static class NextIntBounded extends Generate<Integer> {
        private final int bound;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.intInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextIntExclusive extends Generate<Integer> {
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
    public static class NextIntBetween extends Generate<Integer> {
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
    public static class NextIntIndex extends Generate<Integer> {
        private final int bound;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.interval("index", 0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLong extends Generate<Long> {
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
    public static class NextLongBounded extends Generate<Long> {
        private final long bound;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextLongExclusive extends Generate<Long> {
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
    public static class NextLongBetween extends Generate<Long> {
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
    public static class NextLongIndex extends Generate<Long> {
        private final long bound;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just(Labeling.longInterval(0, bound, true));
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NextGaussian extends Generate<Double> {
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
    public static class NextByte extends Generate<Byte> {
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
    public static class NextShort extends Generate<Short> {
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
    public static class NextBytes extends Generate<Byte[]> {
        private final int count;

        @Override
        public Maybe<String> getLabel() {
            return Maybe.just("bytes[" + count + "]");
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Sized<A> extends Generate<A> {
        private static Maybe<String> LABEL = Maybe.just("sized");

        private final Fn1<Integer, Generate<A>> fn;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WithMetadata<A> extends Generate<A> {
        private final Maybe<String> label;
        private final Maybe<Object> applicationData;
        private final Generate<A> operand;

        @Override
        public boolean isPrimitive() {
            return false;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Aggregate<Elem, Builder, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("aggregate");

        private final Fn0<Builder> initialBuilderSupplier;
        private final Fn2<Builder, Elem, Builder> addFn;
        private final Fn1<Builder, Out> buildFn;
        private final Iterable<Generate<Elem>> elements;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product2<A, B, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product2");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Fn2<A, B, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product3<A, B, C, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product3");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Generate<C> c;
        private final Fn3<A, B, C, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product4<A, B, C, D, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product4");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Generate<C> c;
        private final Generate<D> d;
        private final Fn4<A, B, C, D, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product5<A, B, C, D, E, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product5");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Generate<C> c;
        private final Generate<D> d;
        private final Generate<E> e;
        private final Fn5<A, B, C, D, E, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product6<A, B, C, D, E, F, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product6");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Generate<C> c;
        private final Generate<D> d;
        private final Generate<E> e;
        private final Generate<F> f;
        private final Fn6<A, B, C, D, E, F, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product7<A, B, C, D, E, F, G, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product7");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Generate<C> c;
        private final Generate<D> d;
        private final Generate<E> e;
        private final Generate<F> f;
        private final Generate<G> g;
        private final Fn7<A, B, C, D, E, F, G, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product8<A, B, C, D, E, F, G, H, Out> extends Generate<Out> {
        private static Maybe<String> LABEL = Maybe.just("product8");

        private final Generate<A> a;
        private final Generate<B> b;
        private final Generate<C> c;
        private final Generate<D> d;
        private final Generate<E> e;
        private final Generate<F> f;
        private final Generate<G> g;
        private final Generate<H> h;
        private final Fn8<A, B, C, D, E, F, G, H, Out> combine;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Tap<A, B> extends Generate<B> {
        private static Maybe<String> LABEL = Maybe.just("tap");

        private final Generate<A> inner;
        private final Fn2<Generator<A>, RandomState, B> fn;

        @Override
        public Maybe<String> getLabel() {
            return LABEL;
        }
    }

    public static <A> Generate<A> constant(A a) {
        return new Constant<>(a);
    }

    public static <A> Generate<A> generate(Fn1<? super RandomState, Result> fn) {
        return new Custom<A>(fn::apply);
    }

    public static <A, B> Generate<B> tap(Generate<A> inner,
                                         Fn2<Generator<A>, RandomState, B> f) {
        return new Tap<>(inner, f);
    }

    public static Generate<Boolean> generateBoolean() {
        return NextBoolean.INSTANCE;
    }

    public static Generate<Boolean> generateBoolean(BooleanWeights weights) {
        return CoProducts.generateBoolean(weights);
    }

    public static Generate<Double> generateDouble() {
        return NextDouble.INSTANCE;
    }

    public static Generate<Double> generateDouble(double scale) {
        return generateDouble().fmap(n -> n * scale);
    }

    public static Generate<Double> generateDouble(double min, double max) {
        double scale = max - min;
        return generateDouble().fmap(n -> min + n * scale);
    }

    public static Generate<Float> generateFloat() {
        return NextFloat.INSTANCE;
    }

    public static Generate<Float> generateFloat(float scale) {
        return generateFloat().fmap(n -> n * scale);
    }

    public static Generate<Float> generateFloat(float min, float max) {
        float scale = max - min;
        return generateFloat().fmap(n -> min + n * scale);
    }

    public static Generate<Integer> generateInt() {
        return NextInt.INSTANCE;
    }

    public static Generate<Integer> generateInt(int min, int max) {
        checkMinMax(min, max);
        return new NextIntBetween(min, max);
    }

    public static Generate<Integer> generateIntExclusive(int bound) {
        checkBound(bound);
        return new NextIntBounded(bound);
    }

    public static Generate<Integer> generateIntExclusive(int origin, int bound) {
        checkOriginBound(origin, bound);
        return new NextIntExclusive(origin, bound);
    }

    public static Generate<Integer> generateIntIndex(int bound) {
        checkBound(bound);
        return new NextIntIndex(bound);
    }

    public static Generate<Long> generateLong() {
        return NextLong.INSTANCE;
    }

    public static Generate<Long> generateLong(long min, long max) {
        checkMinMax(min, max);
        return new NextLongBetween(min, max);
    }

    public static Generate<Long> generateLongExclusive(long bound) {
        checkBound(bound);
        return new NextLongBounded(bound);
    }

    public static Generate<Long> generateLongExclusive(long origin, long bound) {
        checkOriginBound(origin, bound);
        return new NextLongExclusive(origin, bound);
    }

    public static Generate<Long> generateLongIndex(long bound) {
        checkBound(bound);
        return new NextLongIndex(bound);
    }

    public static Generate<Byte> generateByte() {
        return NextByte.INSTANCE;
    }

    public static Generate<Short> generateShort() {
        return NextShort.INSTANCE;
    }

    public static Generate<Double> generateGaussian() {
        return NextGaussian.INSTANCE;
    }

    public static Generate<Byte[]> generateBytes(int count) {
        checkCount(count);
        return new NextBytes(count);
    }

    public static <A> Generate<A> sized(Fn1<Integer, Generate<A>> fn) {
        return new Sized<>(fn::apply);
    }

    public static <A> Generate<A> sizedMinimum(int minimum, Fn1<Integer, Generate<A>> fn) {
        if (minimum < 1) {
            return sized(fn);
        } else {
            return sized(n -> fn.apply(Math.min(n, minimum)));
        }
    }

    public static <A, Builder, Out> Aggregate<A, Builder, Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                                         Fn2<Builder, A, Builder> addFn,
                                                                         Fn1<Builder, Out> buildFn,
                                                                         Iterable<Generate<A>> elements) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, elements);
    }

    public static <Elem, Builder, Out> Generate<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                               Fn2<Builder, Elem, Builder> addFn,
                                                               Fn1<Builder, Out> buildFn,
                                                               int size,
                                                               Generate<Elem> gen) {
        return new Aggregate<>(initialBuilderSupplier, addFn, buildFn, replicate(size, gen));
    }

    public static <A, C extends Collection<A>> Generate<C> buildCollection(Fn0<C> initialCollectionSupplier,
                                                                           Iterable<Generate<A>> elements) {
        return new Aggregate<>(initialCollectionSupplier,
                (collection, item) -> {
                    collection.add(item);
                    return collection;
                }, id(), elements);
    }

    public static <A, C extends Collection<A>> Generate<C> buildCollection(Fn0<C> initialCollectionSupplier,
                                                                           int size,
                                                                           Generate<A> gen) {
        return buildCollection(initialCollectionSupplier, replicate(size, gen));
    }

    public static <A> Generate<ImmutableVector<A>> buildVector(Iterable<Generate<A>> elements) {
        return new Aggregate<>(Vector::<A>builder, VectorBuilder::add, VectorBuilder::build, elements);
    }

    public static <A> Generate<ImmutableVector<A>> buildVector(int size, Generate<A> gen) {
        return new Aggregate<A, VectorBuilder<A>, ImmutableVector<A>>(() -> Vector.builder(size), VectorBuilder::add,
                VectorBuilder::build, replicate(size, gen));
    }

    public static <A> Generate<ImmutableNonEmptyVector<A>> buildNonEmptyVector(NonEmptyIterable<Generate<A>> elements) {
        return new Aggregate<>(Vector::<A>builder, VectorBuilder::add, b -> b.build().toNonEmptyOrThrow(), elements);
    }

    public static <A> Generate<ImmutableNonEmptyVector<A>> buildNonEmptyVector(int size, Generate<A> gen) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");

        }
        return new Aggregate<A, VectorBuilder<A>, ImmutableNonEmptyVector<A>>(() -> Vector.builder(size), VectorBuilder::add,
                aVectorBuilder -> aVectorBuilder.build().toNonEmptyOrThrow(),
                replicate(size, gen));
    }

    public static <A, B, Out> Generate<Out> product(Generate<A> a,
                                                    Generate<B> b,
                                                    Fn2<A, B, Out> combine) {
        return new Product2<>(a, b, combine);
    }

    public static <A, B, C, Out> Generate<Out> product(Generate<A> a,
                                                       Generate<B> b,
                                                       Generate<C> c,
                                                       Fn3<A, B, C, Out> combine) {
        return new Product3<>(a, b, c, combine);
    }


    public static <A, B, C, D, Out> Generate<Out> product(Generate<A> a,
                                                          Generate<B> b,
                                                          Generate<C> c,
                                                          Generate<D> d,
                                                          Fn4<A, B, C, D, Out> combine) {
        return new Product4<>(a, b, c, d, combine);
    }


    public static <A, B, C, D, E, Out> Generate<Out> product(Generate<A> a,
                                                             Generate<B> b,
                                                             Generate<C> c,
                                                             Generate<D> d,
                                                             Generate<E> e,
                                                             Fn5<A, B, C, D, E, Out> combine) {
        return new Product5<>(a, b, c, d, e, combine);
    }


    public static <A, B, C, D, E, F, Out> Generate<Out> product(Generate<A> a,
                                                                Generate<B> b,
                                                                Generate<C> c,
                                                                Generate<D> d,
                                                                Generate<E> e,
                                                                Generate<F> f,
                                                                Fn6<A, B, C, D, E, F, Out> combine) {
        return new Product6<>(a, b, c, d, e, f, combine);
    }

    public static <A, B, C, D, E, F, G, Out> Generate<Out> product(Generate<A> a,
                                                                   Generate<B> b,
                                                                   Generate<C> c,
                                                                   Generate<D> d,
                                                                   Generate<E> e,
                                                                   Generate<F> f,
                                                                   Generate<G> g,
                                                                   Fn7<A, B, C, D, E, F, G, Out> combine) {
        return new Product7<>(a, b, c, d, e, f, g, combine);
    }

    public static <A, B, C, D, E, F, G, H, Out> Generate<Out> product(Generate<A> a,
                                                                      Generate<B> b,
                                                                      Generate<C> c,
                                                                      Generate<D> d,
                                                                      Generate<E> e,
                                                                      Generate<F> f,
                                                                      Generate<G> g,
                                                                      Generate<H> h,
                                                                      Fn8<A, B, C, D, E, F, G, H, Out> combine) {
        return new Product8<>(a, b, c, d, e, f, g, h, combine);
    }

    public static <A, B> Generate<Tuple2<A, B>> tupled(Generate<A> a,
                                                       Generate<B> b) {
        return new Product2<>(a, b, Tuple2::tuple);
    }

    public static <A, B, C> Generate<Tuple3<A, B, C>> tupled(Generate<A> a,
                                                             Generate<B> b,
                                                             Generate<C> c) {
        return new Product3<>(a, b, c, Tuple3::tuple);
    }

    public static <A, B, C, D> Generate<Tuple4<A, B, C, D>> tupled(Generate<A> a,
                                                                   Generate<B> b,
                                                                   Generate<C> c,
                                                                   Generate<D> d) {
        return new Product4<>(a, b, c, d, Tuple4::tuple);
    }

    public static <A, B, C, D, E> Generate<Tuple5<A, B, C, D, E>> tupled(Generate<A> a,
                                                                         Generate<B> b,
                                                                         Generate<C> c,
                                                                         Generate<D> d,
                                                                         Generate<E> e) {
        return new Product5<>(a, b, c, d, e, Tuple5::tuple);
    }

    public static <A, B, C, D, E, F> Generate<Tuple6<A, B, C, D, E, F>> tupled(Generate<A> a,
                                                                               Generate<B> b,
                                                                               Generate<C> c,
                                                                               Generate<D> d,
                                                                               Generate<E> e,
                                                                               Generate<F> f) {
        return new Product6<>(a, b, c, d, e, f, Tuple6::tuple);
    }

    public static <A, B, C, D, E, F, G> Generate<Tuple7<A, B, C, D, E, F, G>> tupled(Generate<A> a,
                                                                                     Generate<B> b,
                                                                                     Generate<C> c,
                                                                                     Generate<D> d,
                                                                                     Generate<E> e,
                                                                                     Generate<F> f,
                                                                                     Generate<G> g) {
        return new Product7<>(a, b, c, d, e, f, g, Tuple7::tuple);
    }

    public static <A, B, C, D, E, F, G, H> Generate<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generate<A> a,
                                                                                           Generate<B> b,
                                                                                           Generate<C> c,
                                                                                           Generate<D> d,
                                                                                           Generate<E> e,
                                                                                           Generate<F> f,
                                                                                           Generate<G> g,
                                                                                           Generate<H> h) {
        return new Product8<>(a, b, c, d, e, f, g, h, Tuple8::tuple);
    }

    // TODO:  organize these


    public static Generate<String> generateString() {
        return Strings.generateString();
    }

    public static Generate<String> generateString(int length, Generate<String> g) {
        return Strings.generateString(length, g);
    }

    public static Generate<String> generateStringFromCharacters(int length, Generate<Character> g) {
        return Strings.generateStringFromCharacters(length, g);
    }

    public static Generate<String> generateStringFromCharacters(int length, NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(length, characters);
    }

    public static Generate<String> generateStringFromCharacters(Generate<Character> g) {
        return Strings.generateStringFromCharacters(g);
    }

    public static Generate<String> generateStringFromCharacters(NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(characters);
    }

    @SafeVarargs
    public static Generate<String> generateString(Generate<String> first, Generate<String>... more) {
        return Strings.generateString(first, more);
    }

    public static Generate<String> generateIdentifier() {
        return Strings.generateIdentifier();
    }

    public static Generate<String> generateIdentifier(int length) {
        return Strings.generateIdentifier(length);
    }

    public static Generate<String> concatStrings(Generate<String> separator, Iterable<Generate<String>> components) {
        return Strings.concatStrings(separator, components);
    }

    public static Generate<String> concatStrings(String separator, Iterable<Generate<String>> components) {
        return Strings.concatStrings(separator, components);
    }

    public static Generate<String> concatStrings(Iterable<Generate<String>> components) {
        return Strings.concatStrings(components);
    }

    public static Generate<String> concatMaybeStrings(Generate<String> separator, Iterable<Generate<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(separator, components);
    }

    public static Generate<String> concatMaybeStrings(String separator, Iterable<Generate<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(separator, components);
    }

    public static Generate<String> concatMaybeStrings(Iterable<Generate<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(components);
    }

    public static CompoundStringBuilder compoundStringBuilder() {
        return ConcreteCompoundStringBuilder.builder();
    }

    public static <A> Generate<A> generateNull() {
        return Nulls.generateNull();
    }

    public static <A> Generate<A> generateWithNulls(NullWeights weights, Generate<A> g) {
        return Nulls.generateWithNulls(weights, g);
    }

    public static <A> Generate<A> generateWithNulls(Generate<A> g) {
        return Nulls.generateWithNulls(g);
    }

    public static Generate<Unit> generateUnit() {
        return CoProducts.generateUnit();
    }

    public static Generate<Boolean> generateTrue() {
        return CoProducts.generateTrue();
    }

    public static Generate<Boolean> generateFalse() {
        return CoProducts.generateFalse();
    }

    public static <A> Generate<Maybe<A>> generateMaybe(MaybeWeights weights, Generate<A> g) {
        return CoProducts.generateMaybe(weights, g);
    }

    public static <A> Generate<Maybe<A>> generateMaybe(Generate<A> g) {
        return CoProducts.generateMaybe(g);
    }

    public static <A> Generate<Maybe<A>> generateJust(Generate<A> g) {
        return CoProducts.generateJust(g);
    }

    public static <A> Generate<Maybe<A>> generateNothing() {
        return CoProducts.generateNothing();
    }

    public static <L, R> Generate<Either<L, R>> generateEither(EitherWeights weights, Generate<L> leftGen, Generate<R> rightGen) {
        return CoProducts.generateEither(weights, leftGen, rightGen);
    }

    public static <L, R> Generate<Either<L, R>> generateEither(Generate<L> leftGen, Generate<R> rightGen) {
        return CoProducts.generateEither(leftGen, rightGen);
    }

    public static <L, R> Generate<Either<L, R>> generateLeft(Generate<L> g) {
        return CoProducts.generateLeft(g);
    }

    public static <L, R> Generate<Either<L, R>> generateRight(Generate<R> g) {
        return CoProducts.generateRight(g);
    }

    @SafeVarargs
    public static <A> Generate<A> chooseOneOf(Generate<A> first, Generate<? extends A>... more) {
        return Choose.chooseOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generate<A> chooseOneOfValues(A first, A... more) {
        return Choose.chooseOneOfValues(first, more);
    }

    @SafeVarargs
    public static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return Choose.chooseAtLeastOneOfValues(first, more);
    }

    @SafeVarargs
    public static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneOf(Generate<? extends A> first, Generate<? extends A>... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generate<ImmutableVector<A>> chooseSomeOfValues(A first, A... more) {
        return Choose.chooseSomeOf(first, more);
    }

    @SafeVarargs
    public static <A> Generate<ImmutableVector<A>> chooseSomeOf(Generate<? extends A> first, Generate<? extends A>... more) {
        return Choose.chooseSomeOf(first, more);
    }

    public static <A> Generate<A> chooseOneFromCollection(Collection<A> items) {
        return Choose.chooseOneFromCollection(items);
    }

    public static <A> Generate<A> chooseOneFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseOneFromDomain(domain);
    }

    public static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromCollection(Collection<A> items) {
        return Choose.chooseAtLeastOneFromCollection(items);
    }

    public static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseAtLeastOneFromDomain(domain);
    }

    public static <A> Generate<ImmutableVector<A>> chooseSomeFromCollection(Collection<A> items) {
        return Choose.chooseSomeFromDomain(items);
    }

    public static <A> Generate<ImmutableVector<A>> chooseSomeFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseSomeFromDomain(domain);
    }

    public static <K, V> Generate<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        return Choose.chooseEntryFromMap(map);
    }

    public static <K, V> Generate<K> chooseKeyFromMap(Map<K, V> map) {
        return Choose.chooseKeyFromMap(map);
    }

    public static <K, V> Generate<V> chooseValueFromMap(Map<K, V> map) {
        return Choose.chooseValueFromMap(map);
    }

    public static <A> Generate<A> frequency(FrequencyMap<A> frequencyMap) {
        return Choose.frequency(frequencyMap);
    }

    @SafeVarargs
    public static <A> Generate<A> frequency(FrequencyEntry<A> first, FrequencyEntry<A>... more) {
        return Choose.frequency(first, more);
    }

    public static <A> Generate<A> frequency(Collection<FrequencyEntry<A>> entries) {
        return Choose.frequency(entries);
    }

    public static <A> Generate<ArrayList<A>> generateArrayList(Generate<A> g) {
        return Collections.generateArrayList(g);
    }

    public static <A> Generate<ArrayList<A>> generateNonEmptyArrayList(Generate<A> g) {
        return Collections.generateNonEmptyArrayList(g);
    }

    public static <A> Generate<ArrayList<A>> generateArrayListOfN(int n, Generate<A> g) {
        return Collections.generateArrayListOfN(n, g);
    }

    public static <A> Generate<HashSet<A>> generateHashSet(Generate<A> g) {
        return Collections.generateHashSet(g);
    }

    public static <A> Generate<HashSet<A>> generateNonEmptyHashSet(Generate<A> g) {
        return Collections.generateNonEmptyHashSet(g);
    }

    public static <A> Generate<ImmutableVector<A>> generateVector(Generate<A> g) {
        return Collections.generateVector(g);
    }

    public static <A> Generate<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generate<A> g) {
        return Collections.generateNonEmptyVector(g);
    }

    public static <A> Generate<ImmutableVector<A>> generateVectorOfN(int n, Generate<A> g) {
        return Collections.generateVectorOfN(n, g);
    }

    public static <A> Generate<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfN(int n, Generate<A> g) {
        return Collections.generateNonEmptyVectorOfN(n, g);
    }

    public static <K, V> Generate<Map<K, V>> generateMap(Generate<K> generateKey,
                                                         Generate<V> generateValue) {
        return Collections.generateMap(generateKey, generateValue);
    }

    public static <K, V> Generate<Map<K, V>> generateMap(Collection<K> keys,
                                                         Generate<V> generateValue) {
        return Collections.generateMap(keys, generateValue);
    }

    public static <K, V> Generate<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                         Generate<V> generateValue) {
        return Collections.generateMap(keys, generateValue);
    }

    public static <K, V> Generate<Map<K, V>> generateNonEmptyMap(Generate<K> generateKey,
                                                                 Generate<V> generateValue) {
        return Collections.generateNonEmptyMap(generateKey, generateValue);
    }

    public static <A> Generate<ImmutableNonEmptyIterable<A>> generateInfiniteIterable(Generate<A> gen) {
        return Infinite.generateInfiniteIterable(gen);
    }

    public static Generate<Vector<Integer>> generateShuffled(int count) {
        return Shuffle.generateShuffled(count);
    }

    public static <A> Generate<Vector<A>> generateShuffled(int count, Fn1<Integer, A> fn) {
        return Shuffle.generateShuffled(count, fn);
    }

    public static <A> Generate<NonEmptyVector<A>> generateNonEmptyShuffled(int count, Fn1<Integer, A> fn) {
        return Shuffle.generateNonEmptyShuffled(count, fn);
    }

    public static <A> Generate<Vector<A>> generateShuffled(FiniteIterable<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generate<NonEmptyVector<A>> generateShuffled(NonEmptyFiniteIterable<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generate<Vector<A>> generateShuffled(Collection<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generate<Vector<A>> generateShuffled(A[] input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generate<Vector<A>> generateShuffled(Vector<A> input) {
        return generateShuffled(input.size(), input::unsafeGet);
    }

    public static <A> Generate<NonEmptyVector<A>> generateShuffled(NonEmptyVector<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(int weight, Generate<A> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(weight, firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(Generate<A> firstChoice) {
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

    public static <A, B> Generate<Choice2<A, B>> generateChoice(FrequencyEntry<A> a,
                                                                FrequencyEntry<B> b) {
        return choiceBuilder(a).or(b).toGenerate();
    }

    public static <A, B, C> Generate<Choice3<A, B, C>> generateChoice(FrequencyEntry<A> a,
                                                                      FrequencyEntry<B> b,
                                                                      FrequencyEntry<C> c) {
        return choiceBuilder(a).or(b).or(c).toGenerate();
    }

    public static <A, B, C, D> Generate<Choice4<A, B, C, D>> generateChoice(FrequencyEntry<A> a,
                                                                            FrequencyEntry<B> b,
                                                                            FrequencyEntry<C> c,
                                                                            FrequencyEntry<D> d) {
        return choiceBuilder(a).or(b).or(c).or(d).toGenerate();
    }

    public static <A, B, C, D, E> Generate<Choice5<A, B, C, D, E>> generateChoice(FrequencyEntry<A> a,
                                                                                  FrequencyEntry<B> b,
                                                                                  FrequencyEntry<C> c,
                                                                                  FrequencyEntry<D> d,
                                                                                  FrequencyEntry<E> e) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).toGenerate();
    }

    public static <A, B, C, D, E, F> Generate<Choice6<A, B, C, D, E, F>> generateChoice(FrequencyEntry<A> a,
                                                                                        FrequencyEntry<B> b,
                                                                                        FrequencyEntry<C> c,
                                                                                        FrequencyEntry<D> d,
                                                                                        FrequencyEntry<E> e,
                                                                                        FrequencyEntry<F> f) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).toGenerate();
    }

    public static <A, B, C, D, E, F, G> Generate<Choice7<A, B, C, D, E, F, G>> generateChoice(FrequencyEntry<A> a,
                                                                                              FrequencyEntry<B> b,
                                                                                              FrequencyEntry<C> c,
                                                                                              FrequencyEntry<D> d,
                                                                                              FrequencyEntry<E> e,
                                                                                              FrequencyEntry<F> f,
                                                                                              FrequencyEntry<G> g) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).or(g).toGenerate();
    }

    public static <A, B, C, D, E, F, G, H> Generate<Choice8<A, B, C, D, E, F, G, H>> generateChoice(FrequencyEntry<A> a,
                                                                                                    FrequencyEntry<B> b,
                                                                                                    FrequencyEntry<C> c,
                                                                                                    FrequencyEntry<D> d,
                                                                                                    FrequencyEntry<E> e,
                                                                                                    FrequencyEntry<F> f,
                                                                                                    FrequencyEntry<G> g,
                                                                                                    FrequencyEntry<H> h) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).or(g).or(h).toGenerate();
    }

    public static <R> Generate<Fn0<R>> generateFn0(Generate<R> result) {
        return Functions.generateFn0(result);
    }

    public static <A, R> Generate<Fn1<A, R>> generateFn1(Cogenerator<A> param1,
                                                         Generate<R> result) {
        return Functions.generateFn1(param1, result);
    }

    public static <A, B, R> Generate<Fn2<A, B, R>> generateFn2(Cogenerator<A> param1,
                                                               Cogenerator<B> param2,
                                                               Generate<R> result) {
        return Functions.generateFn2(param1, param2, result);
    }

    public static <A, B, C, R> Generate<Fn3<A, B, C, R>> generateFn3(Cogenerator<A> param1,
                                                                     Cogenerator<B> param2,
                                                                     Cogenerator<C> param3,
                                                                     Generate<R> result) {
        return Functions.generateFn3(param1, param2, param3, result);
    }

    public static <A, B, C, D, R> Generate<Fn4<A, B, C, D, R>> generateFn4(Cogenerator<A> param1,
                                                                           Cogenerator<B> param2,
                                                                           Cogenerator<C> param3,
                                                                           Cogenerator<D> param4,
                                                                           Generate<R> result) {
        return Functions.generateFn4(param1, param2, param3, param4, result);
    }

    public static <A, B, C, D, E, R> Generate<Fn5<A, B, C, D, E, R>> generateFn5(Cogenerator<A> param1,
                                                                                 Cogenerator<B> param2,
                                                                                 Cogenerator<C> param3,
                                                                                 Cogenerator<D> param4,
                                                                                 Cogenerator<E> param5,
                                                                                 Generate<R> result) {
        return Functions.generateFn5(param1, param2, param3, param4, param5, result);
    }

    public static <A, B, C, D, E, F, R> Generate<Fn6<A, B, C, D, E, F, R>> generateFn6(Cogenerator<A> param1,
                                                                                       Cogenerator<B> param2,
                                                                                       Cogenerator<C> param3,
                                                                                       Cogenerator<D> param4,
                                                                                       Cogenerator<E> param5,
                                                                                       Cogenerator<F> param6,
                                                                                       Generate<R> result) {
        return Functions.generateFn6(param1, param2, param3, param4, param5, param6, result);
    }

    public static <A, B, C, D, E, F, G, R> Generate<Fn7<A, B, C, D, E, F, G, R>> generateFn7(Cogenerator<A> param1,
                                                                                             Cogenerator<B> param2,
                                                                                             Cogenerator<C> param3,
                                                                                             Cogenerator<D> param4,
                                                                                             Cogenerator<E> param5,
                                                                                             Cogenerator<F> param6,
                                                                                             Cogenerator<G> param7,
                                                                                             Generate<R> result) {
        return Functions.generateFn7(param1, param2, param3, param4, param5, param6, param7, result);
    }

    public static <A, B, C, D, E, F, G, H, R> Generate<Fn8<A, B, C, D, E, F, G, H, R>> generateFn8(Cogenerator<A> param1,
                                                                                                   Cogenerator<B> param2,
                                                                                                   Cogenerator<C> param3,
                                                                                                   Cogenerator<D> param4,
                                                                                                   Cogenerator<E> param5,
                                                                                                   Cogenerator<F> param6,
                                                                                                   Cogenerator<G> param7,
                                                                                                   Cogenerator<H> param8,
                                                                                                   Generate<R> result) {
        return Functions.generateFn8(param1, param2, param3, param4, param5, param6, param7, param8, result);
    }

    public static Generate<UUID> generateUUID() {
        return UUIDs.generateUUID();
    }

    public static Generate<BigInteger> generateBigInteger(BigInteger min, BigInteger max) {
        return BigNumbers.generateBigInteger(min, max);
    }

    public static Generate<BigInteger> generateBigIntegerExclusive(BigInteger bound) {
        return BigNumbers.generateBigIntegerExclusive(bound);
    }

    public static Generate<BigInteger> generateBigIntegerExclusive(BigInteger origin, BigInteger bound) {
        return BigNumbers.generateBigIntegerExclusive(origin, bound);
    }

    public static Generate<BigDecimal> generateBigDecimalExclusive(int decimalPlaces, BigDecimal bound) {
        return BigNumbers.generateBigDecimalExclusive(decimalPlaces, bound);
    }

    public static Generate<BigDecimal> generateBigDecimalExclusive(int decimalPlaces, BigDecimal origin, BigDecimal bound) {
        return BigNumbers.generateBigDecimalExclusive(decimalPlaces, origin, bound);
    }

    public static Generate<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimal min, BigDecimal max) {
        return BigNumbers.generateBigDecimal(decimalPlaces, min, max);
    }

    public static Generate<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        return Temporal.generateLocalDate(min, max);
    }

    public static Generate<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        return Temporal.generateLocalDateExclusive(origin, bound);
    }

    public static Generate<LocalDate> generateLocalDateForYear(Year year) {
        return Temporal.generateLocalDateForYear(year);
    }

    public static Generate<LocalDate> generateLocalDateForMonth(YearMonth yearMonth) {
        return Temporal.generateLocalDateForMonth(yearMonth);
    }

    public static Generate<LocalTime> generateLocalTime() {
        return Temporal.generateLocalTime();
    }

    public static Generate<LocalTime> generateLocalTime(LocalTime min, LocalTime max) {
        return Temporal.generateLocalTime(min, max);
    }

    public static Generate<LocalDateTime> generateLocalDateTime(LocalDate min, LocalDate max) {
        return Temporal.generateLocalDateTime(min, max);
    }

    public static Generate<LocalDateTime> generateLocalDateTime(LocalDateTime min, LocalDateTime max) {
        return Temporal.generateLocalDateTime(min, max);
    }

    public static Generate<Duration> generateDuration(Duration max) {
        return Temporal.generateDuration(max);
    }

    public static Generate<Duration> generateDuration(Duration min, Duration max) {
        return Temporal.generateDuration(min, max);
    }

    public static <A> Generate<A> generateFromSemigroup(Semigroup<A> semigroup, Generate<A> gen) {
        return Lambda.generateFromSemigroup(semigroup, gen);
    }

    public static <A> Generate<A> generateNFromSemigroup(Semigroup<A> semigroup, Generate<A> gen, int count) {
        return Lambda.generateNFromSemigroup(semigroup, gen, count);
    }

    public static <A> Generate<A> generateFromMonoid(Monoid<A> monoid, Generate<A> gen) {
        return Lambda.generateFromMonoid(monoid, gen);
    }

    public static <A> Generate<A> generateNFromMonoid(Monoid<A> monoid, Generate<A> gen, int count) {
        return Lambda.generateNFromMonoid(monoid, gen, count);
    }

    private static <A, B> Generate<B> mapped(Fn1<? super A, ? extends B> fn, Generate<A> operand) {
        return new Mapped<>(fn::apply, operand);
    }

    private static <A, B> Generate<B> flatMapped(Fn1<? super A, ? extends Generate<B>> fn, Generate<A> operand) {
        return new FlatMapped<>(fn::apply, operand);
    }

    private static <A> Generate<A> withMetadata(Maybe<String> label, Maybe<Object> applicationData, Generate<A> operand) {
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
