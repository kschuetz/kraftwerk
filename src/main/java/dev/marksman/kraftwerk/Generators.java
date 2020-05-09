package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.FiniteIterable;
import dev.marksman.enhancediterables.ImmutableNonEmptyIterable;
import dev.marksman.enhancediterables.NonEmptyFiniteIterable;
import dev.marksman.enhancediterables.NonEmptyIterable;
import dev.marksman.kraftwerk.aggregator.Aggregator;
import dev.marksman.kraftwerk.choice.ChoiceBuilder1;
import dev.marksman.kraftwerk.constraints.BigDecimalRange;
import dev.marksman.kraftwerk.constraints.BigIntegerRange;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.DurationRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LocalDateRange;
import dev.marksman.kraftwerk.constraints.LocalDateTimeRange;
import dev.marksman.kraftwerk.constraints.LocalTimeRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import dev.marksman.kraftwerk.core.BuildingBlocks;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.weights.BooleanWeights;
import dev.marksman.kraftwerk.weights.EitherWeights;
import dev.marksman.kraftwerk.weights.MaybeWeights;
import dev.marksman.kraftwerk.weights.NullWeights;
import dev.marksman.kraftwerk.weights.TernaryWeights;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static dev.marksman.kraftwerk.Result.result;
import static dev.marksman.kraftwerk.aggregator.Aggregators.collectionAggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.vectorAggregator;

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

    public static FloatingPointGenerator<Double> generateDouble(DoubleRange range) {
        return Primitives.generateDouble(range);
    }

    public static FloatingPointGenerator<Float> generateFloat() {
        return Primitives.generateFloat();
    }

    public static FloatingPointGenerator<Float> generateFloat(FloatRange range) {
        return Primitives.generateFloat(range);
    }

    public static Generator<Integer> generateInt() {
        return Primitives.generateInt();
    }

    public static Generator<Integer> generateInt(IntRange range) {
        return Primitives.generateInt(range);
    }

    public static Generator<Integer> generateIntIndex(int bound) {
        return Primitives.generateIntIndex(bound);
    }

    public static Generator<Long> generateLong() {
        return Primitives.generateLong();
    }

    public static Generator<Long> generateLong(LongRange range) {
        return Primitives.generateLong(range);
    }

    public static Generator<Long> generateLongIndex(long bound) {
        return Primitives.generateLongIndex(bound);
    }

    public static Generator<Byte> generateByte() {
        return Primitives.generateByte();
    }

    public static Generator<Byte> generateByte(ByteRange range) {
        return Primitives.generateByte(range);
    }

    public static Generator<Short> generateShort() {
        return Primitives.generateShort();
    }

    public static Generator<Short> generateShort(ShortRange range) {
        return Primitives.generateShort(range);
    }

    public static Generator<Character> generateChar() {
        return Primitives.generateChar();
    }

    public static Generator<Character> generateChar(CharRange range) {
        return Primitives.generateChar(range);
    }

    public static Generator<Double> generateGaussian() {
        return Primitives.generateGaussian();
    }

    public static Generator<Byte[]> generateByteArray() {
        return Primitives.generateByteArray();
    }

    public static Generator<Byte[]> generateByteArray(int count) {
        return Primitives.generateByteArray(count);
    }

    public static Generator<Object> generateBoxedPrimitive() {
        return Primitives.generateBoxedPrimitive();
    }

    public static Generator<Integer> generateSize() {
        return Primitives.generateSize();
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

    public static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                             Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(aggregator, elements);
    }

    public static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                             int size,
                                                             Generator<A> gen) {
        return Aggregation.aggregate(aggregator, size, gen);
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> constructCollection,
                                                                            Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(collectionAggregator(constructCollection), elements);
    }

    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> constructCollection,
                                                                            int size,
                                                                            Generator<A> gen) {
        return buildCollection(constructCollection, replicate(size, gen));
    }

    public static <A> Generator<ImmutableVector<A>> buildVector(Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(vectorAggregator(), elements);
    }

    public static <A> Generator<ImmutableVector<A>> buildVector(int size, Generator<A> gen) {
        return Aggregation.aggregate(vectorAggregator(size), replicate(size, gen));
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(NonEmptyIterable<Generator<A>> elements) {
        return Aggregation.aggregate(vectorAggregator(), elements)
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(int size, Generator<A> gen) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");

        }
        return Aggregation.aggregate(vectorAggregator(), replicate(size, gen))
                .fmap(ImmutableVector::toNonEmptyOrThrow);
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

    public static <A extends Enum<A>> Generator<A> generateFromEnum(Class<A> enumType) {
        return Enums.generateFromEnum(enumType);
    }

    @SafeVarargs
    public static <A> Generator<A> chooseOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
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
    public static <A> Generator<A> frequency(Weighted<? extends Generator<? extends A>> first,
                                             Weighted<? extends Generator<? extends A>>... more) {
        return Choose.frequency(first, more);
    }

    @SafeVarargs
    public static <A> Generator<A> frequencyValues(Weighted<? extends A> first,
                                                   Weighted<? extends A>... more) {
        return Choose.frequencyValues(first, more);
    }

    public static <A> Generator<A> frequency(Collection<Weighted<? extends Generator<? extends A>>> entries) {
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

    public static <A> ChoiceBuilder1<A> choiceBuilder(Weighted<? extends Generator<? extends A>> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilder(Generator<? extends A> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(Weighted<? extends A> firstChoice) {
        return ChoiceBuilder1.choiceBuilderValue(firstChoice);
    }

    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return ChoiceBuilder1.choiceBuilderValue(firstChoice);
    }

    public static <A, B> Generator<Choice2<A, B>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                 Weighted<? extends Generator<? extends B>> b) {
        return choiceBuilder(a).or(b).toGenerator();
    }

    public static <A, B, C> Generator<Choice3<A, B, C>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                       Weighted<? extends Generator<? extends B>> b,
                                                                       Weighted<? extends Generator<? extends C>> c) {
        return choiceBuilder(a).or(b).or(c).toGenerator();
    }

    public static <A, B, C, D> Generator<Choice4<A, B, C, D>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                             Weighted<? extends Generator<? extends B>> b,
                                                                             Weighted<? extends Generator<? extends C>> c,
                                                                             Weighted<? extends Generator<? extends D>> d) {
        return choiceBuilder(a).or(b).or(c).or(d).toGenerator();
    }

    public static <A, B, C, D, E> Generator<Choice5<A, B, C, D, E>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                   Weighted<? extends Generator<? extends B>> b,
                                                                                   Weighted<? extends Generator<? extends C>> c,
                                                                                   Weighted<? extends Generator<? extends D>> d,
                                                                                   Weighted<? extends Generator<? extends E>> e) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).toGenerator();
    }

    public static <A, B, C, D, E, F> Generator<Choice6<A, B, C, D, E, F>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                         Weighted<? extends Generator<? extends B>> b,
                                                                                         Weighted<? extends Generator<? extends C>> c,
                                                                                         Weighted<? extends Generator<? extends D>> d,
                                                                                         Weighted<? extends Generator<? extends E>> e,
                                                                                         Weighted<? extends Generator<? extends F>> f) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).toGenerator();
    }

    public static <A, B, C, D, E, F, G> Generator<Choice7<A, B, C, D, E, F, G>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                               Weighted<? extends Generator<? extends B>> b,
                                                                                               Weighted<? extends Generator<? extends C>> c,
                                                                                               Weighted<? extends Generator<? extends D>> d,
                                                                                               Weighted<? extends Generator<? extends E>> e,
                                                                                               Weighted<? extends Generator<? extends F>> f,
                                                                                               Weighted<? extends Generator<? extends G>> g) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).or(g).toGenerator();
    }

    public static <A, B, C, D, E, F, G, H> Generator<Choice8<A, B, C, D, E, F, G, H>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                                     Weighted<? extends Generator<? extends B>> b,
                                                                                                     Weighted<? extends Generator<? extends C>> c,
                                                                                                     Weighted<? extends Generator<? extends D>> d,
                                                                                                     Weighted<? extends Generator<? extends E>> e,
                                                                                                     Weighted<? extends Generator<? extends F>> f,
                                                                                                     Weighted<? extends Generator<? extends G>> g,
                                                                                                     Weighted<? extends Generator<? extends H>> h) {
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

    public static Generator<BigInteger> generateBigInteger() {
        return BigNumbers.generateBigInteger();
    }

    public static Generator<BigInteger> generateBigInteger(BigIntegerRange range) {
        return BigNumbers.generateBigInteger(range);
    }

    public static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(decimalPlaces, range);
    }

    public static Generator<LocalDate> generateLocalDate(LocalDateRange range) {
        return Temporal.generateLocalDate(range);
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

    public static Generator<LocalTime> generateLocalTime(LocalTimeRange range) {
        return Temporal.generateLocalTime(range);
    }

    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateRange dateRange) {
        return Temporal.generateLocalDateTime(dateRange);
    }

    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateTimeRange range) {
        return Temporal.generateLocalDateTime(range);
    }

    public static Generator<Duration> generateDuration(DurationRange range) {
        return Temporal.generateDuration(range);
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

    public static <A extends Comparable<A>> Generator<Tuple2<A, A>> generateOrderedPair(Generator<A> generator) {
        return OrderedTuples.generateOrderedPair(generator);
    }

    public static <A> Generator<ImmutableVector<A>> generateOrderedSequence(Generator<Integer> countForEachElement,
                                                                            ImmutableVector<A> orderedElems) {
        return Sequences.generateOrderedSequence(countForEachElement, orderedElems);
    }

    public static <A> Generator<ImmutableVector<A>> generateOrderedSequence(IntRange countForEachElementRange,
                                                                            ImmutableVector<A> orderedElems) {
        return Sequences.generateOrderedSequence(countForEachElementRange, orderedElems);
    }

    public static Generator<IntRange> generateIntRange() {
        return Ranges.generateIntRange();
    }

    public static Generator<IntRange> generateIntRange(IntRange parentRange) {
        return Ranges.generateIntRange(parentRange);
    }

    public static Generator<LongRange> generateLongRange() {
        return Ranges.generateLongRange();
    }

    public static Generator<LongRange> generateLongRange(LongRange parentRange) {
        return Ranges.generateLongRange(parentRange);
    }

    public static Generator<ShortRange> generateShortRange() {
        return Ranges.generateShortRange();
    }

    public static Generator<ShortRange> generateShortRange(ShortRange parentRange) {
        return Ranges.generateShortRange(parentRange);
    }

    public static Generator<ByteRange> generateByteRange() {
        return Ranges.generateByteRange();
    }

    public static Generator<ByteRange> generateByteRange(ByteRange parentRange) {
        return Ranges.generateByteRange(parentRange);
    }

    public static Generator<DoubleRange> generateDoubleRange(DoubleRange parentRange) {
        return Ranges.generateDoubleRange(parentRange);
    }

    public static Generator<FloatRange> generateFloatRange(FloatRange parentRange) {
        return Ranges.generateFloatRange(parentRange);
    }

    public static Generator<BigIntegerRange> generateBigIntegerRange(BigIntegerRange parentRange) {
        return Ranges.generateBigIntegerRange(parentRange);
    }

    public static Generator<LocalDateRange> generateLocalDateRange(LocalDateRange parentRange) {
        return Ranges.generateLocalDateRange(parentRange);
    }

    public static Generator<LocalTimeRange> generateLocalTimeRange(LocalTimeRange parentRange) {
        return Ranges.generateLocalTimeRange(parentRange);
    }

    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateRange parentRange) {
        return Ranges.generateLocalDateTimeRange(parentRange);
    }

    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateTimeRange parentRange) {
        return Ranges.generateLocalDateTimeRange(parentRange);
    }

    public static Generator<DurationRange> generateDurationRange(DurationRange parentRange) {
        return Ranges.generateDurationRange(parentRange);
    }

}
