package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.*;
import com.jnape.palatable.lambda.adt.hlist.*;
import com.jnape.palatable.lambda.functions.*;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.*;
import dev.marksman.enhancediterables.FiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.enhancediterables.NonEmptyIterable;
import dev.marksman.kraftwerk.choice.ChoiceBuilder1;
import dev.marksman.kraftwerk.core.BuildingBlocks;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.weights.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static dev.marksman.kraftwerk.Result.result;

public class Generators {

    public static <A> Generator<A> constant(A a) {
        return Constant.constant(a);
    }

    public static <A, B> Generator<B> tap(Generator<A> gen,
                                          Fn2<Generate<A>, Seed, B> f) {
        return parameters -> {
            Generate<A> runA = gen.prepare(parameters);
            return input -> {
                Seed nextState = BuildingBlocks.nextInt(input).getNextState();
                return result(nextState,
                        f.apply(runA, input));
            };
        };
    }

    public static Generator<Boolean> generateBoolean() {
        return Primitives.generateBoolean();
    }

    public static Generator<Boolean> generateBoolean(BooleanWeights weights) {
        return CoProducts.generateBoolean(weights);
    }

    public static FloatingPointGenerator<Double> generateDouble() {
        return Primitives.generateDouble();
    }

    public static Generator<Double> generateDouble(double scale) {
        return generateDouble().fmap(n -> n * scale);
    }

    public static Generator<Double> generateDouble(double min, double max) {
        double scale = max - min;
        return generateDouble().fmap(n -> min + n * scale);
    }

    public static Generator<Float> generateFloat() {
        return Primitives.generateFloat();
    }

    public static Generator<Float> generateFloat(float scale) {
        return generateFloat().fmap(n -> n * scale);
    }

    public static Generator<Float> generateFloat(float min, float max) {
        float scale = max - min;
        return generateFloat().fmap(n -> min + n * scale);
    }

    public static Generator<Integer> generateInt() {
        return Primitives.generateInt();
    }

    public static Generator<Integer> generateInt(int min, int max) {
        return Primitives.generateInt(min, max);
    }

    public static Generator<Integer> generateIntExclusive(int bound) {
        return Primitives.generateIntExclusive(bound);
    }

    public static Generator<Integer> generateIntExclusive(int origin, int bound) {
        return Primitives.generateIntExclusive(origin, bound);
    }

    public static Generator<Integer> generateIntIndex(int bound) {
        return Primitives.generateIntIndex(bound);
    }

    public static Generator<Long> generateLong() {
        return Primitives.generateLong();
    }

    public static Generator<Long> generateLong(long min, long max) {
        return Primitives.generateLong(min, max);
    }

    public static Generator<Long> generateLongExclusive(long bound) {
        return Primitives.generateLongExclusive(bound);
    }

    public static Generator<Long> generateLongExclusive(long origin, long bound) {
        return Primitives.generateLongExclusive(origin, bound);
    }

    public static Generator<Long> generateLongIndex(long bound) {
        return Primitives.generateLongIndex(bound);
    }

    public static Generator<Byte> generateByte() {
        return Primitives.generateByte();
    }

    public static Generator<Short> generateShort() {
        return Primitives.generateShort();
    }

    public static Generator<Double> generateGaussian() {
        return Primitives.generateGaussian();
    }

    public static Generator<Byte[]> generateBytes(int count) {
        return Primitives.generateBytes(count);
    }

    public static <A> Generator<A> sized(Fn1<Integer, Generator<A>> fn) {
        return Primitives.sized(fn);
    }

    public static <A> Generator<A> sizedMinimum(int minimum, Fn1<Integer, Generator<A>> fn) {
        if (minimum < 1) {
            return sized(fn);
        } else {
            return sized(n -> fn.apply(Math.min(n, minimum)));
        }
    }

    public static <A, Builder, Out> Generator<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                             Fn2<Builder, A, Builder> addFn,
                                                             Fn1<Builder, Out> buildFn,
                                                             Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(initialBuilderSupplier, addFn, buildFn, elements);
    }

    public static <Elem, Builder, Out> Generator<Out> aggregate(Fn0<Builder> initialBuilderSupplier,
                                                                Fn2<Builder, Elem, Builder> addFn,
                                                                Fn1<Builder, Out> buildFn,
                                                                int size,
                                                                Generator<Elem> gen) {
        return Aggregation.aggregate(initialBuilderSupplier, addFn, buildFn, replicate(size, gen));
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> initialCollectionSupplier,
                                                                            Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(initialCollectionSupplier,
                (collection, item) -> {
                    collection.add(item);
                    return collection;
                }, id(), elements);
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> initialCollectionSupplier,
                                                                            int size,
                                                                            Generator<A> gen) {
        return buildCollection(initialCollectionSupplier, replicate(size, gen));
    }

    public static <A> Generator<ImmutableVector<A>> buildVector(Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(Vector::<A>builder, VectorBuilder::add, VectorBuilder::build, elements);
    }

    public static <A> Generator<ImmutableVector<A>> buildVector(int size, Generator<A> gen) {
        return Aggregation.<A, VectorBuilder<A>, ImmutableVector<A>>aggregate(() -> Vector.builder(size), VectorBuilder::add,
                VectorBuilder::build, replicate(size, gen));
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(NonEmptyIterable<Generator<A>> elements) {
        return Aggregation.aggregate(Vector::<A>builder, VectorBuilder::add, b -> b.build().toNonEmptyOrThrow(), elements);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(int size, Generator<A> gen) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");

        }
        return Aggregation.<A, VectorBuilder<A>, ImmutableNonEmptyVector<A>>aggregate(() -> Vector.builder(size), VectorBuilder::add,
                aVectorBuilder -> aVectorBuilder.build().toNonEmptyOrThrow(),
                replicate(size, gen));
    }

    public static <A, B, Out> Generator<Out> product(Generator<A> a,
                                                     Generator<B> b,
                                                     Fn2<A, B, Out> combine) {
        return Products.product(a, b, combine);
    }

    public static <A, B, C, Out> Generator<Out> product(Generator<A> a,
                                                        Generator<B> b,
                                                        Generator<C> c,
                                                        Fn3<A, B, C, Out> combine) {
        return Products.product(a, b, c, combine);
    }

    public static <A, B, C, D, Out> Generator<Out> product(Generator<A> a,
                                                           Generator<B> b,
                                                           Generator<C> c,
                                                           Generator<D> d,
                                                           Fn4<A, B, C, D, Out> combine) {
        return Products.product(a, b, c, d, combine);
    }

    public static <A, B, C, D, E, Out> Generator<Out> product(Generator<A> a,
                                                              Generator<B> b,
                                                              Generator<C> c,
                                                              Generator<D> d,
                                                              Generator<E> e,
                                                              Fn5<A, B, C, D, E, Out> combine) {
        return Products.product(a, b, c, d, e, combine);
    }

    public static <A, B, C, D, E, F, Out> Generator<Out> product(Generator<A> a,
                                                                 Generator<B> b,
                                                                 Generator<C> c,
                                                                 Generator<D> d,
                                                                 Generator<E> e,
                                                                 Generator<F> f,
                                                                 Fn6<A, B, C, D, E, F, Out> combine) {
        return Products.product(a, b, c, d, e, f, combine);
    }

    public static <A, B, C, D, E, F, G, Out> Generator<Out> product(Generator<A> a,
                                                                    Generator<B> b,
                                                                    Generator<C> c,
                                                                    Generator<D> d,
                                                                    Generator<E> e,
                                                                    Generator<F> f,
                                                                    Generator<G> g,
                                                                    Fn7<A, B, C, D, E, F, G, Out> combine) {
        return Products.product(a, b, c, d, e, f, g, combine);
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
        return Products.product(a, b, c, d, e, f, g, h, combine);
    }

    public static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> a,
                                                        Generator<B> b) {
        return Products.product(a, b, Tuple2::tuple);
    }

    public static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> a,
                                                              Generator<B> b,
                                                              Generator<C> c) {
        return Products.product(a, b, c, Tuple3::tuple);
    }

    public static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> a,
                                                                    Generator<B> b,
                                                                    Generator<C> c,
                                                                    Generator<D> d) {
        return Products.product(a, b, c, d, Tuple4::tuple);
    }

    public static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> a,
                                                                          Generator<B> b,
                                                                          Generator<C> c,
                                                                          Generator<D> d,
                                                                          Generator<E> e) {
        return Products.product(a, b, c, d, e, Tuple5::tuple);
    }

    public static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> a,
                                                                                Generator<B> b,
                                                                                Generator<C> c,
                                                                                Generator<D> d,
                                                                                Generator<E> e,
                                                                                Generator<F> f) {
        return Products.product(a, b, c, d, e, f, Tuple6::tuple);
    }

    public static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> a,
                                                                                      Generator<B> b,
                                                                                      Generator<C> c,
                                                                                      Generator<D> d,
                                                                                      Generator<E> e,
                                                                                      Generator<F> f,
                                                                                      Generator<G> g) {
        return Products.product(a, b, c, d, e, f, g, Tuple7::tuple);
    }

    public static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> a,
                                                                                            Generator<B> b,
                                                                                            Generator<C> c,
                                                                                            Generator<D> d,
                                                                                            Generator<E> e,
                                                                                            Generator<F> f,
                                                                                            Generator<G> g,
                                                                                            Generator<H> h) {
        return Products.product(a, b, c, d, e, f, g, h, Tuple8::tuple);
    }

    public static <A> Generator<ImmutableVector<A>> sequence(Iterable<Generator<A>> gs) {
        return Sequence.sequence(gs);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> sequenceNonEmpty(NonEmptyIterable<Generator<A>> gs) {
        return Sequence.sequenceNonEmpty(gs);
    }

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

    public static Generator<String> generateIdentifier() {
        return Strings.generateIdentifier();
    }

    public static Generator<String> generateIdentifier(int length) {
        return Strings.generateIdentifier(length);
    }

    public static Generator<String> concatStrings(Generator<String> separator, Iterable<Generator<String>> components) {
        return Strings.concatStrings(separator, components);
    }

    public static Generator<String> concatStrings(String separator, Iterable<Generator<String>> components) {
        return Strings.concatStrings(separator, components);
    }

    public static Generator<String> concatStrings(Iterable<Generator<String>> components) {
        return Strings.concatStrings(components);
    }

    public static Generator<String> concatMaybeStrings(Generator<String> separator, Iterable<Generator<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(separator, components);
    }

    public static Generator<String> concatMaybeStrings(String separator, Iterable<Generator<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(separator, components);
    }

    public static Generator<String> concatMaybeStrings(Iterable<Generator<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(components);
    }

    public static CompoundStringBuilder compoundStringBuilder() {
        return ConcreteCompoundStringBuilder.builder();
    }

    public static <A> Generator<A> generateNull() {
        return Nulls.generateNull();
    }

    public static <A> Generator<A> generateWithNulls(NullWeights weights, Generator<A> g) {
        return Nulls.generateWithNulls(weights, g);
    }

    public static <A> Generator<A> generateWithNulls(Generator<A> g) {
        return Nulls.generateWithNulls(g);
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

    public static <L, R> Generator<Either<L, R>> generateEither(EitherWeights weights, Generator<L> leftGen, Generator<R> rightGen) {
        return CoProducts.generateEither(weights, leftGen, rightGen);
    }

    public static <L, R> Generator<Either<L, R>> generateEither(Generator<L> leftGen, Generator<R> rightGen) {
        return CoProducts.generateEither(leftGen, rightGen);
    }

    public static <L, R> Generator<Either<L, R>> generateLeft(Generator<L> g) {
        return CoProducts.generateLeft(g);
    }

    public static <L, R> Generator<Either<L, R>> generateRight(Generator<R> g) {
        return CoProducts.generateRight(g);
    }

    public static <A, B> Generator<These<A, B>> generateThese(Generator<A> generatorA, Generator<B> generatorB) {
        return CoProducts.generateThese(generatorA, generatorB);
    }

    public static <A, B> Generator<These<A, B>> generateThese(TernaryWeights weights, Generator<A> generatorA, Generator<B> generatorB) {
        return CoProducts.generateThese(weights, generatorA, generatorB);
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
    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return Choose.chooseAtLeastOneOfValues(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ImmutableVector<A>> chooseSomeOfValues(A first, A... more) {
        return Choose.chooseSomeOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ImmutableVector<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseSomeOf(first, more);
    }

    public static <A> Generator<A> chooseOneFromCollection(Collection<A> items) {
        return Choose.chooseOneFromCollection(items);
    }

    public static <A> Generator<A> chooseOneFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseOneFromDomain(domain);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromCollection(Collection<A> items) {
        return Choose.chooseAtLeastOneFromCollection(items);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseAtLeastOneFromDomain(domain);
    }

    public static <A> Generator<ImmutableVector<A>> chooseSomeFromCollection(Collection<A> items) {
        return Choose.chooseSomeFromDomain(items);
    }

    public static <A> Generator<ImmutableVector<A>> chooseSomeFromDomain(NonEmptyVector<A> domain) {
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

    public static <A> Generator<ArrayList<A>> generateArrayList(Generator<A> g) {
        return Collections.generateArrayList(g);
    }

    public static <A> Generator<ArrayList<A>> generateNonEmptyArrayList(Generator<A> g) {
        return Collections.generateNonEmptyArrayList(g);
    }

    public static <A> Generator<ArrayList<A>> generateArrayListOfN(int n, Generator<A> g) {
        return Collections.generateArrayListOfN(n, g);
    }

    public static <A> Generator<HashSet<A>> generateHashSet(Generator<A> g) {
        return Collections.generateHashSet(g);
    }

    public static <A> Generator<HashSet<A>> generateNonEmptyHashSet(Generator<A> g) {
        return Collections.generateNonEmptyHashSet(g);
    }

    public static <A> Generator<ImmutableVector<A>> generateVector(Generator<A> g) {
        return Collections.generateVector(g);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> g) {
        return Collections.generateNonEmptyVector(g);
    }

    public static <A> Generator<ImmutableVector<A>> generateVectorOfN(int n, Generator<A> g) {
        return Collections.generateVectorOfN(n, g);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfN(int n, Generator<A> g) {
        return Collections.generateNonEmptyVectorOfN(n, g);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(Generator<K> generateKey,
                                                          Generator<V> generateValue) {
        return Collections.generateMap(generateKey, generateValue);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                          Generator<V> generateValue) {
        return Collections.generateMap(keys, generateValue);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                          Generator<V> generateValue) {
        return Collections.generateMap(keys, generateValue);
    }

    public static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> generateKey,
                                                                  Generator<V> generateValue) {
        return Collections.generateNonEmptyMap(generateKey, generateValue);
    }

    public static <A> Generator<ImmutableNonEmptyIterable<A>> generateInfiniteIterable(Generator<A> gen) {
        return Infinite.generateInfiniteIterable(gen);
    }

    public static Generator<Vector<Integer>> generateShuffled(int count) {
        return Shuffle.generateShuffled(count);
    }

    public static <A> Generator<Vector<A>> generateShuffled(int count, Fn1<Integer, A> fn) {
        return Shuffle.generateShuffled(count, fn);
    }

    public static <A> Generator<NonEmptyVector<A>> generateNonEmptyShuffled(int count, Fn1<Integer, A> fn) {
        return Shuffle.generateNonEmptyShuffled(count, fn);
    }

    public static <A> Generator<Vector<A>> generateShuffled(FiniteIterable<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generator<NonEmptyVector<A>> generateShuffled(NonEmptyFiniteIterable<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generator<Vector<A>> generateShuffled(Collection<A> input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generator<Vector<A>> generateShuffled(A[] input) {
        return Shuffle.generateShuffled(input);
    }

    public static <A> Generator<Vector<A>> generateShuffled(Vector<A> input) {
        return generateShuffled(input.size(), input::unsafeGet);
    }

    public static <A> Generator<NonEmptyVector<A>> generateShuffled(NonEmptyVector<A> input) {
        return Shuffle.generateShuffled(input);
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

    public static <R> Generator<Fn0<R>> generateFn0(Generator<R> result) {
        return Functions.generateFn0(result);
    }

    public static <A, R> Generator<Fn1<A, R>> generateFn1(Cogenerator<A> param1,
                                                          Generator<R> result) {
        return Functions.generateFn1(param1, result);
    }

    public static <A, B, R> Generator<Fn2<A, B, R>> generateFn2(Cogenerator<A> param1,
                                                                Cogenerator<B> param2,
                                                                Generator<R> result) {
        return Functions.generateFn2(param1, param2, result);
    }

    public static <A, B, C, R> Generator<Fn3<A, B, C, R>> generateFn3(Cogenerator<A> param1,
                                                                      Cogenerator<B> param2,
                                                                      Cogenerator<C> param3,
                                                                      Generator<R> result) {
        return Functions.generateFn3(param1, param2, param3, result);
    }

    public static <A, B, C, D, R> Generator<Fn4<A, B, C, D, R>> generateFn4(Cogenerator<A> param1,
                                                                            Cogenerator<B> param2,
                                                                            Cogenerator<C> param3,
                                                                            Cogenerator<D> param4,
                                                                            Generator<R> result) {
        return Functions.generateFn4(param1, param2, param3, param4, result);
    }

    public static <A, B, C, D, E, R> Generator<Fn5<A, B, C, D, E, R>> generateFn5(Cogenerator<A> param1,
                                                                                  Cogenerator<B> param2,
                                                                                  Cogenerator<C> param3,
                                                                                  Cogenerator<D> param4,
                                                                                  Cogenerator<E> param5,
                                                                                  Generator<R> result) {
        return Functions.generateFn5(param1, param2, param3, param4, param5, result);
    }

    public static <A, B, C, D, E, F, R> Generator<Fn6<A, B, C, D, E, F, R>> generateFn6(Cogenerator<A> param1,
                                                                                        Cogenerator<B> param2,
                                                                                        Cogenerator<C> param3,
                                                                                        Cogenerator<D> param4,
                                                                                        Cogenerator<E> param5,
                                                                                        Cogenerator<F> param6,
                                                                                        Generator<R> result) {
        return Functions.generateFn6(param1, param2, param3, param4, param5, param6, result);
    }

    public static <A, B, C, D, E, F, G, R> Generator<Fn7<A, B, C, D, E, F, G, R>> generateFn7(Cogenerator<A> param1,
                                                                                              Cogenerator<B> param2,
                                                                                              Cogenerator<C> param3,
                                                                                              Cogenerator<D> param4,
                                                                                              Cogenerator<E> param5,
                                                                                              Cogenerator<F> param6,
                                                                                              Cogenerator<G> param7,
                                                                                              Generator<R> result) {
        return Functions.generateFn7(param1, param2, param3, param4, param5, param6, param7, result);
    }

    public static <A, B, C, D, E, F, G, H, R> Generator<Fn8<A, B, C, D, E, F, G, H, R>> generateFn8(Cogenerator<A> param1,
                                                                                                    Cogenerator<B> param2,
                                                                                                    Cogenerator<C> param3,
                                                                                                    Cogenerator<D> param4,
                                                                                                    Cogenerator<E> param5,
                                                                                                    Cogenerator<F> param6,
                                                                                                    Cogenerator<G> param7,
                                                                                                    Cogenerator<H> param8,
                                                                                                    Generator<R> result) {
        return Functions.generateFn8(param1, param2, param3, param4, param5, param6, param7, param8, result);
    }

    public static Generator<UUID> generateUUID() {
        return UUIDs.generateUUID();
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

    public static Generator<BigDecimal> generateBigDecimalExclusive(int decimalPlaces, BigDecimal bound) {
        return BigNumbers.generateBigDecimalExclusive(decimalPlaces, bound);
    }

    public static Generator<BigDecimal> generateBigDecimalExclusive(int decimalPlaces, BigDecimal origin, BigDecimal bound) {
        return BigNumbers.generateBigDecimalExclusive(decimalPlaces, origin, bound);
    }

    public static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimal min, BigDecimal max) {
        return BigNumbers.generateBigDecimal(decimalPlaces, min, max);
    }

    public static Generator<LocalDate> generateLocalDate(LocalDate min, LocalDate max) {
        return Temporal.generateLocalDate(min, max);
    }

    public static Generator<LocalDate> generateLocalDateExclusive(LocalDate origin, LocalDate bound) {
        return Temporal.generateLocalDateExclusive(origin, bound);
    }

    public static Generator<LocalDate> generateLocalDateForYear(Year year) {
        return Temporal.generateLocalDateForYear(year);
    }

    public static Generator<LocalDate> generateLocalDateForMonth(YearMonth yearMonth) {
        return Temporal.generateLocalDateForMonth(yearMonth);
    }

    public static Generator<LocalTime> generateLocalTime() {
        return Temporal.generateLocalTime();
    }

    public static Generator<LocalTime> generateLocalTime(LocalTime min, LocalTime max) {
        return Temporal.generateLocalTime(min, max);
    }

    public static Generator<LocalDateTime> generateLocalDateTime(LocalDate min, LocalDate max) {
        return Temporal.generateLocalDateTime(min, max);
    }

    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateTime min, LocalDateTime max) {
        return Temporal.generateLocalDateTime(min, max);
    }

    public static Generator<Duration> generateDuration(Duration max) {
        return Temporal.generateDuration(max);
    }

    public static Generator<Duration> generateDuration(Duration min, Duration max) {
        return Temporal.generateDuration(min, max);
    }

    public static <A> Generator<A> generateFromSemigroup(Semigroup<A> semigroup, Generator<A> gen) {
        return Lambda.generateFromSemigroup(semigroup, gen);
    }

    public static <A> Generator<A> generateNFromSemigroup(Semigroup<A> semigroup, Generator<A> gen, int count) {
        return Lambda.generateNFromSemigroup(semigroup, gen, count);
    }

    public static <A> Generator<A> generateFromMonoid(Monoid<A> monoid, Generator<A> gen) {
        return Lambda.generateFromMonoid(monoid, gen);
    }

    public static <A> Generator<A> generateNFromMonoid(Monoid<A> monoid, Generator<A> gen, int count) {
        return Lambda.generateNFromMonoid(monoid, gen, count);
    }

    public static <A> Generator<ImmutableVector<A>> generateOrderedSequence(Generator<Integer> countForEachElement,
                                                                            ImmutableVector<A> orderedElems) {
        return Sequences.generateOrderedSequence(countForEachElement, orderedElems);
    }

    public static <A> Generator<ImmutableVector<A>> generateOrderedSequence(int minCountEachElement,
                                                                            int maxCountEachElement,
                                                                            ImmutableVector<A> orderedElems) {
        return Sequences.generateOrderedSequence(minCountEachElement, maxCountEachElement, orderedElems);
    }
}
