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
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.weights.BooleanWeights;
import dev.marksman.kraftwerk.weights.EitherWeights;
import dev.marksman.kraftwerk.weights.MaybeWeights;
import dev.marksman.kraftwerk.weights.NullWeights;
import dev.marksman.kraftwerk.weights.TernaryWeights;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static dev.marksman.kraftwerk.Collections.generateCollectionSize;
import static dev.marksman.kraftwerk.aggregator.Aggregators.collectionAggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.vectorAggregator;

/**
 * A collection of built-in generators
 */
public final class Generators {
    private Generators() {

    }

    /**
     * Creates a {@link Generator} that yields the same value whenever invoked.
     *
     * @param a   the value to return when the {@code Generator} is invoked
     * @param <A> the type of the value
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> constant(A a) {
        return Constant.constant(a);
    }

    /**
     * Creates a {@link Generator} that yields {@link Boolean} values.
     *
     * @return a {@code Generator<Boolean>}
     */
    public static Generator<Boolean> generateBoolean() {
        return Primitives.generateBoolean();
    }

    /**
     * Creates a {@link Generator} that yields {@link Boolean} values, with custom probabilities for
     * {@code true} and {@code false} values.
     *
     * @param weights the probabilities for returning {@code true} vs. {@code false} values
     * @return a {@code Generator<Boolean>}
     */
    public static Generator<Boolean> generateBoolean(BooleanWeights weights) {
        return CoProducts.generateBoolean(weights);
    }

    /**
     * Creates a {@link Generator} that yields {@link Double}s within the full range of {@code Double}s
     * (between {@link Double#MIN_VALUE} and {@link Double#MAX_VALUE}, inclusive).
     *
     * @return a {@link FloatingPointGenerator}
     */
    public static FloatingPointGenerator<Double> generateDouble() {
        return Primitives.generateDouble();
    }

    /**
     * Creates a {@link Generator} that yields {@link Double}s between between 0 (inclusive) and 1 (exclusive).
     *
     * @return a {@link FloatingPointGenerator}
     */
    public static FloatingPointGenerator<Double> generateDoubleFractional() {
        return Primitives.generateDoubleFractional();
    }

    /**
     * Creates a {@link Generator} that yields {@link Double}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@link FloatingPointGenerator}
     */
    public static FloatingPointGenerator<Double> generateDouble(DoubleRange range) {
        return Primitives.generateDouble(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Float}s within the full range of {@code Float}s
     * (between {@link Float#MIN_VALUE} and {@link Float#MAX_VALUE}, inclusive).
     *
     * @return a {@link FloatingPointGenerator}
     */
    public static FloatingPointGenerator<Float> generateFloat() {
        return Primitives.generateFloat();
    }

    /**
     * Creates a {@link Generator} that yields {@link Float}s between between 0 (inclusive) and 1 (exclusive).
     *
     * @return a {@link FloatingPointGenerator}
     */
    public static FloatingPointGenerator<Float> generateFloatFractional() {
        return Primitives.generateFloatFractional();
    }

    /**
     * Creates a {@link Generator} that yields {@link Float}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@link FloatingPointGenerator}
     */
    public static FloatingPointGenerator<Float> generateFloat(FloatRange range) {
        return Primitives.generateFloat(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Integer}s within the full range of {@code Integer}s
     * (between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}, inclusive).
     *
     * @return a {@code Generator<Integer>}
     */
    public static Generator<Integer> generateInt() {
        return Primitives.generateInt();
    }

    /**
     * Creates a {@link Generator} that yields {@link Integer}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@code Generator<Integer>}
     */
    public static Generator<Integer> generateInt(IntRange range) {
        return Primitives.generateInt(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Integer}s that are intended to be used as indices (e.g. into arrays or collections).
     * Values returned range from 0..{@code bound} (exclusive). Ignores any bias settings.
     *
     * @param bound the maximum value (exclusive)
     * @return a {@code Generator<Integer>}
     */
    public static Generator<Integer> generateIntIndex(int bound) {
        return Primitives.generateIntIndex(bound);
    }

    /**
     * Creates a {@link Generator} that yields {@link Long}s within the full range of {@code Long}s
     * (between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE}, inclusive).
     *
     * @return a {@code Generator<Long>}
     */
    public static Generator<Long> generateLong() {
        return Primitives.generateLong();
    }

    /**
     * Creates a {@link Generator} that yields {@link Long}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@code Generator<Long>}
     */
    public static Generator<Long> generateLong(LongRange range) {
        return Primitives.generateLong(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Long}s that are intended to be used as indices (e.g. into arrays or collections).
     * Values returned range from 0..{@code bound} (exclusive). Ignores any bias settings.
     *
     * @param bound the maximum value (exclusive)
     * @return a {@code Generator<Long>}
     */
    public static Generator<Long> generateLongIndex(long bound) {
        return Primitives.generateLongIndex(bound);
    }

    /**
     * Creates a {@link Generator} that yields {@link Byte}s within the full range of {@code Byte}s
     * (between -128 and 127, inclusive).
     *
     * @return a {@code Generator<Byte>}
     */
    public static Generator<Byte> generateByte() {
        return Primitives.generateByte();
    }

    /**
     * Creates a {@link Generator} that yields {@link Byte}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@code Generator<Byte>}
     */
    public static Generator<Byte> generateByte(ByteRange range) {
        return Primitives.generateByte(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Short}s within the full range of {@code Short}s
     * (between -32768 and 32767, inclusive).
     *
     * @return a {@code Generator<Short>}
     */
    public static Generator<Short> generateShort() {
        return Primitives.generateShort();
    }

    /**
     * Creates a {@link Generator} that yields {@link Short}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@code Generator<Short>}
     */
    public static Generator<Short> generateShort(ShortRange range) {
        return Primitives.generateShort(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Character}s within the full range of {@code Character}s
     * (between {@link Character#MIN_VALUE} and {@link Character#MAX_VALUE}, inclusive).
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateChar() {
        return Primitives.generateChar();
    }

    /**
     * Creates a {@link Generator} that yields {@link Character}s within a specific range.
     *
     * @param range the range of values to generate
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateChar(CharRange range) {
        return Primitives.generateChar(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Double}s, which, when accumulated, will result in
     * normal distribution.
     *
     * @return a {@code Generator<Double>}
     */
    public static Generator<Double> generateGaussian() {
        return Primitives.generateGaussian();
    }

    /**
     * Creates a {@link Generator} that yields {@link Byte} arrays of varying sizes.
     *
     * @return a {@code Generator<Byte[]>}
     */
    public static Generator<Byte[]> generateByteArray() {
        return Primitives.generateByteArray();
    }

    /**
     * Creates a {@link Generator} that yields {@link Byte} arrays of varying sizes.
     *
     * @param size the size of the arrays returned; must be &gt;= 0
     * @return a {@code Generator<Byte[]>}
     */
    public static Generator<Byte[]> generateByteArray(int size) {
        return Primitives.generateByteArray(size);
    }

    /**
     * Creates a {@link Generator} that yields boxed primitives.  When invoked, will return one of the following types:
     * {@link Integer}, {@link Long}, {@link Short}, {@link Byte}, {@link Double}, {@link Float}, {@link Boolean}, or {@link Character}.
     *
     * @return a {@code Generator<Object>}
     */
    public static Generator<Object> generateBoxedPrimitive() {
        return Primitives.generateBoxedPrimitive();
    }

    /**
     * Creates a {@link Generator} that yields {@link Seed}s.
     *
     * @return a {@code Generator<Seed>}
     */
    public static Generator<Seed> generateSeed() {
        return Primitives.generateSeed();
    }

    /**
     * Creates a {@link Generator} that yields sizes, generally used to determine sizes of collections.
     * Respects the size settings in the {@link GeneratorParameters} used to configure the generator.
     *
     * @return a {@code Generator<Integer>}
     */
    public static Generator<Integer> generateSize() {
        return Primitives.generateSize();
    }

    /**
     * Creates a {@link Generator} that yields sizes within a specific range, generally used to determine sizes of collections.
     * Overrides the {@link GeneratorParameters}'s size parameters used to configure the generator, but will respect a preferred size if it can.
     *
     * @return a {@code Generator<Integer>}
     */
    public static Generator<Integer> generateSize(IntRange sizeRange) {
        return Primitives.generateSize(sizeRange);
    }

    /**
     * Creates a {@link Generator} that dynamically creates another {@code Generator} depending on a randomly
     * generated size value.
     * Respects the size settings in the {@link GeneratorParameters} used to configure the generator.
     *
     * @param fn  a function that takes a size (an {@link Integer} &gt;= 0) and returns a {@code Generator}
     * @param <A> the type of value to generate
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> sized(Fn1<Integer, Generator<A>> fn) {
        return Primitives.sized(fn);
    }

    /**
     * Creates a {@link Generator} that dynamically creates another {@code Generator} depending on a randomly
     * generated size value.
     * Respects the size settings in the {@link GeneratorParameters} used to configure the generator, but the size
     * will always be {@code minimum} or greater.
     *
     * @param minimum the minimum size to generate
     * @param fn      a function that takes a size (an {@link Integer} &gt;= {@code minimum}) and returns a {@code Generator}
     * @param <A>     the type of value to generate
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> sizedMinimum(int minimum, Fn1<Integer, Generator<A>> fn) {
        if (minimum < 1) {
            return sized(fn);
        } else {
            return sized(n -> fn.apply(Math.min(n, minimum)));
        }
    }

    /**
     * Creates a {@link Generator} that aggregates elements generated from a collection of {@code Generators}.
     *
     * @param aggregator an {@link Aggregator} used to aggregate a group of generated elements
     * @param elements   an {@code Iterable} contain zero of more {@code Generator<A>}s
     * @param <A>        the input type of {@code aggregator}
     * @param <Builder>  the builder type of {@code aggregator}
     * @param <Out>      the output type of {@code aggregator}
     * @return a {@code Generator<Out>}
     */
    public static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                             Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(aggregator, elements);
    }

    /**
     * Creates a {@link Generator} that, on each invocation, yields a value that is derived by invoking an element {@code Generator}
     * a specific number of times, and feeding these elements to an {@link Aggregator}.
     *
     * @param aggregator an {@code Aggregator} used to aggregate a group of generated elements
     * @param size       the number of input elements that {@code aggregator} will be supplied to create one output.  Must be &gt;= 0.
     * @param gen        the {@code Generator} that generates elements
     * @param <A>        the input type of {@code aggregator}
     * @param <Builder>  the builder type of {@code aggregator}
     * @param <Out>      the output type of {@code aggregator}
     * @return a {@code Generator<Out>}
     */
    public static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                             int size,
                                                             Generator<A> gen) {
        Preconditions.requireNaturalSize(size);
        return Aggregation.aggregate(aggregator, size, gen);
    }

    /**
     * Creates a {@link Generator} that, on each invocation, yields a value that is derived by invoking an element {@code Generator}
     * a random number of times (within a specific range), and feeding these elements to an {@link Aggregator}.
     *
     * @param aggregator an {@code Aggregator} used to aggregate a group of generated elements
     * @param sizeRange  a range of the number of input elements that {@code aggregator} will be supplied to create one output.
     *                   Lower end of range must be &gt;= 0.
     * @param gen        the {@code Generator} that yields elements
     * @param <A>        the input type of {@code aggregator}
     * @param <Builder>  the builder type of {@code aggregator}
     * @param <Out>      the output type of {@code aggregator}
     * @return a {@code Generator<Out>}
     */
    public static <A, Builder, Out> Generator<Out> aggregate(Aggregator<A, Builder, Out> aggregator,
                                                             IntRange sizeRange,
                                                             Generator<A> gen) {
        Preconditions.requireNaturalSize(sizeRange);
        return generateCollectionSize(sizeRange).flatMap(size -> aggregate(aggregator, size, gen));
    }

    /**
     * Creates a {@link Generator} that yields {@link Collection}s of type {@code C} by invoking a collection of
     * element {@code Generator}s in sequence, and then aggregating the results.
     * <p>
     * The size of the generated {@code Collection}s will always equal the size of {@code elements}.
     *
     * @param constructCollection the constructor for the desired type of collection (e.g. {@code ArrayList::new})
     * @param elements            an {@link Iterable} containing {@code Generator<A>}s.  Each {@code Generator} will correspond with a single output in the final result.
     * @param <A>                 the element type of the desired collection
     * @param <C>                 the collection type.   Instances of {@code C} must support {@link Collection#add} (which is to say, must not throw on invocation).
     * @return a {@code Generator<C>}
     */
    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> constructCollection,
                                                                            Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(collectionAggregator(constructCollection), elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link Collection}s of type {@code C} by invoking an
     * element {@code Generator}s a specific number of times, and then aggregating the results.
     * <p>
     * The size of the generated {@code Collection}s will always equal {@code size}.
     *
     * @param constructCollection the constructor for the desired type of collection (e.g. {@code ArrayList::new})
     * @param size                the size of the output collection.  Must be &gt;= 0.
     * @param gen                 the element {@code Generator}
     * @param <A>                 the element type of the desired collection
     * @param <C>                 the collection type.   Instances of {@code C} must support {@link Collection#add} (which is to say, must not throw on invocation).
     * @return a {@code Generator<C>}
     */
    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> constructCollection,
                                                                            int size,
                                                                            Generator<A> gen) {
        Preconditions.requireNaturalSize(size);
        return buildCollection(constructCollection, replicate(size, gen));
    }

    /**
     * Creates a {@link Generator} that yields {@link Collection}s of type {@code C} by invoking an
     * element {@code Generator}s a random number of times (within a specific range), and then aggregating the results.
     * <p>
     * The size of the generated {@code Collection}s will always fall within {@code sizeRange}.
     *
     * @param constructCollection the constructor for the desired type of collection (e.g. {@code ArrayList::new})
     * @param sizeRange           the range of sizes for the generated collections.  Lower end of range must be &gt;= 0.
     * @param gen                 the element {@code Generator}
     * @param <A>                 the element type of the desired collection
     * @param <C>                 the collection type.   Instances of {@code C} must support {@link Collection#add} (which is to say, must not throw on invocation).
     * @return a {@code Generator<C>}
     */
    public static <A, C extends Collection<A>> Generator<C> buildCollection(Fn0<C> constructCollection,
                                                                            IntRange sizeRange,
                                                                            Generator<A> gen) {
        Preconditions.requireNaturalSize(sizeRange);
        return generateCollectionSize(sizeRange).flatMap(size -> buildCollection(constructCollection, size, gen));
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableVector}s by invoking a collection of
     * element {@code Generator}s in sequence, and then aggregating the results.
     *
     * @param elements an {@link Iterable} containing {@code Generator<A>}s.  Each {@code Generator} will correspond with a single output in the final result.
     * @param <A>      the element type of the desired {@code ImmutableVector}
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> buildVector(Iterable<Generator<A>> elements) {
        return Aggregation.aggregate(vectorAggregator(), elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableVector}s by invoking an element {@code Generator}s a specific number of times,
     * and then aggregating the results.
     * <p>
     * The size of the generated {@code ImmutableVector}s will always equal {@code size}.
     *
     * @param size the size of the output {@code ImmutableVector}.  Must be &gt;= 0.
     * @param gen  the element {@code Generator}
     * @param <A>  the element type of the desired {@code ImmutableVector}
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> buildVector(int size, Generator<A> gen) {
        Preconditions.requireNaturalSize(size);
        return Aggregation.aggregate(vectorAggregator(size), replicate(size, gen));
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableVector}s by invoking an element {@code Generator}s a random number of times
     * (within a specific range),  and then aggregating the results.
     * <p>
     * The size of the generated {@code ImmutableVector}s will always fall within {@code sizeRange}.
     *
     * @param sizeRange the range of sizes for the generated {@code ImmutableVector}s.  Lower end of range must be &gt;= 0.
     * @param gen       the element {@code Generator}
     * @param <A>       the element type of the desired {@code ImmutableVector}
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> buildVector(IntRange sizeRange, Generator<A> gen) {
        Preconditions.requireNaturalSize(sizeRange);
        return generateCollectionSize(sizeRange).flatMap(size -> buildVector(size, gen));
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableNonEmptyVector}s by invoking a collection of
     * element {@code Generator}s in sequence, and then aggregating the results.
     *
     * @param elements an {@link NonEmptyIterable} containing {@code Generator<A>}s.  Each {@code Generator} will correspond with a single output in the final result.
     * @param <A>      the element type of the desired {@code ImmutableNonEmptyVector}
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(NonEmptyIterable<Generator<A>> elements) {
        return Aggregation.aggregate(vectorAggregator(), elements)
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableNonEmptyVector}s by invoking an element {@code Generator}s a specific number of times,
     * and then aggregating the results.
     * <p>
     * The size of the generated {@code ImmutableNonEmptyVector}s will always equal {@code size}.
     *
     * @param size the size of the output {@code ImmutableNonEmptyVector}.  Must be &gt;= 1.
     * @param gen  the element {@code Generator}
     * @param <A>  the element type of the desired {@code ImmutableNonEmptyVector}
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(int size, Generator<A> gen) {
        Preconditions.requirePositiveSize(size);
        return Aggregation.aggregate(vectorAggregator(), replicate(size, gen))
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableNonEmptyVector}s by invoking an element {@code Generator}s a random number of times
     * (within a specific range),  and then aggregating the results.
     * <p>
     * The size of the generated {@code ImmutableNonEmptyVector}s will always fall within {@code sizeRange}.
     *
     * @param sizeRange the range of sizes for the generated {@code ImmutableVector}s.  Lower end of range must be &gt;= 1.
     * @param gen       the element {@code Generator}
     * @param <A>       the element type of the desired {@code ImmutableNonEmptyVector}
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> buildNonEmptyVector(IntRange sizeRange, Generator<A> gen) {
        Preconditions.requirePositiveSize(sizeRange);
        return generateCollectionSize(sizeRange).flatMap(size -> buildNonEmptyVector(size, gen));
    }

    /**
     * Creates a {@link Generator} that is a product of two other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator)
     */
    public static <A, B, Out> Generator<Out> product(Generator<A> a,
                                                     Generator<B> b,
                                                     Fn2<A, B, Out> combine) {
        return Products.product(a, b, combine);
    }

    /**
     * Creates a {@link Generator} that is a product of three other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param c       the third component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <C>     the type of the values generated by the third component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator, Generator)
     */
    public static <A, B, C, Out> Generator<Out> product(Generator<A> a,
                                                        Generator<B> b,
                                                        Generator<C> c,
                                                        Fn3<A, B, C, Out> combine) {
        return Products.product(a, b, c, combine);
    }

    /**
     * Creates a {@link Generator} that is a product of four other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param c       the third component generator
     * @param d       the fourth component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <C>     the type of the values generated by the third component generator
     * @param <D>     the type of the values generated by the fourth component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, Out> Generator<Out> product(Generator<A> a,
                                                           Generator<B> b,
                                                           Generator<C> c,
                                                           Generator<D> d,
                                                           Fn4<A, B, C, D, Out> combine) {
        return Products.product(a, b, c, d, combine);
    }

    /**
     * Creates a {@link Generator} that is a product of five other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param c       the third component generator
     * @param d       the fourth component generator
     * @param e       the fifth component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <C>     the type of the values generated by the third component generator
     * @param <D>     the type of the values generated by the fourth component generator
     * @param <E>     the type of the values generated by the fifth component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, E, Out> Generator<Out> product(Generator<A> a,
                                                              Generator<B> b,
                                                              Generator<C> c,
                                                              Generator<D> d,
                                                              Generator<E> e,
                                                              Fn5<A, B, C, D, E, Out> combine) {
        return Products.product(a, b, c, d, e, combine);
    }

    /**
     * Creates a {@link Generator} that is a product of six other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param c       the third component generator
     * @param d       the fourth component generator
     * @param e       the fifth component generator
     * @param f       the sixth component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <C>     the type of the values generated by the third component generator
     * @param <D>     the type of the values generated by the fourth component generator
     * @param <E>     the type of the values generated by the fifth component generator
     * @param <F>     the type of the values generated by the sixth component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator, Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, E, F, Out> Generator<Out> product(Generator<A> a,
                                                                 Generator<B> b,
                                                                 Generator<C> c,
                                                                 Generator<D> d,
                                                                 Generator<E> e,
                                                                 Generator<F> f,
                                                                 Fn6<A, B, C, D, E, F, Out> combine) {
        return Products.product(a, b, c, d, e, f, combine);
    }

    /**
     * Creates a {@link Generator} that is a product of seven other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param c       the third component generator
     * @param d       the fourth component generator
     * @param e       the fifth component generator
     * @param f       the sixth component generator
     * @param g       the seventh component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <C>     the type of the values generated by the third component generator
     * @param <D>     the type of the values generated by the fourth component generator
     * @param <E>     the type of the values generated by the fifth component generator
     * @param <F>     the type of the values generated by the sixth component generator
     * @param <G>     the type of the values generated by the seventh component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator, Generator, Generator, Generator, Generator, Generator)
     */
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

    /**
     * Creates a {@link Generator} that is a product of eight other {@code Generators}.
     *
     * @param a       the first component generator
     * @param b       the second component generator
     * @param c       the third component generator
     * @param d       the fourth component generator
     * @param e       the fifth component generator
     * @param f       the sixth component generator
     * @param g       the seventh component generator
     * @param h       the eighth component generator
     * @param combine a function to combine the results from the component generators
     * @param <A>     the type of the values generated by the first component generator
     * @param <B>     the type of the values generated by the second component generator
     * @param <C>     the type of the values generated by the third component generator
     * @param <D>     the type of the values generated by the fourth component generator
     * @param <E>     the type of the values generated by the fifth component generator
     * @param <F>     the type of the values generated by the sixth component generator
     * @param <G>     the type of the values generated by the seventh component generator
     * @param <H>     the type of the values generated by the eighth component generator
     * @param <Out>   the type of the values to be ultimately yielded by the result {@code Generator} (i.e, the return type of the {@code combine} function)
     * @return a {@code Generator<Out>}
     * @see Generators#tupled(Generator, Generator, Generator, Generator, Generator, Generator, Generator, Generator)
     */
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

    /**
     * Creates a {@link Generator} that yields {@link Tuple2}s by combining the outputs of two other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @return a {@code Generator<Tuple2<A, B>}
     * @see Generators#product(Generator, Generator, Fn2)
     */
    public static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> a,
                                                        Generator<B> b) {
        return Products.product(a, b, Tuple2::tuple);
    }

    /**
     * Creates a {@link Generator} that yields {@link Tuple3}s by combining the outputs of three other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param c   the third component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @param <C> the type of the values generated by the third component generator
     * @return a {@code Generator<Tuple3<A, B, C>}
     * @see Generators#product(Generator, Generator, Generator, Fn3)
     */
    public static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> a,
                                                              Generator<B> b,
                                                              Generator<C> c) {
        return Products.product(a, b, c, Tuple3::tuple);
    }

    /**
     * Creates a {@link Generator} that yields {@link Tuple4}s by combining the outputs of four other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param c   the third component generator
     * @param d   the fourth component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @param <C> the type of the values generated by the third component generator
     * @param <D> the type of the values generated by the fourth component generator
     * @return a {@code Generator<Tuple4<A, B, C, D>}
     * @see Generators#product(Generator, Generator, Generator, Generator, Fn4)
     */
    public static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> a,
                                                                    Generator<B> b,
                                                                    Generator<C> c,
                                                                    Generator<D> d) {
        return Products.product(a, b, c, d, Tuple4::tuple);
    }

    /**
     * Creates a {@link Generator} that yields {@link Tuple5}s by combining the outputs of five other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param c   the third component generator
     * @param d   the fourth component generator
     * @param e   the fifth component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @param <C> the type of the values generated by the third component generator
     * @param <D> the type of the values generated by the fourth component generator
     * @param <E> the type of the values generated by the fifth component generator
     * @return a {@code Generator<Tuple5<A, B, C, D, E>}
     * @see Generators#product(Generator, Generator, Generator, Generator, Generator, Fn5)
     */
    public static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> a,
                                                                          Generator<B> b,
                                                                          Generator<C> c,
                                                                          Generator<D> d,
                                                                          Generator<E> e) {
        return Products.product(a, b, c, d, e, Tuple5::tuple);
    }

    /**
     * Creates a {@link Generator} that yields {@link Tuple6}s by combining the outputs of six other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param c   the third component generator
     * @param d   the fourth component generator
     * @param e   the fifth component generator
     * @param f   the sixth component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @param <C> the type of the values generated by the third component generator
     * @param <D> the type of the values generated by the fourth component generator
     * @param <E> the type of the values generated by the fifth component generator
     * @param <F> the type of the values generated by the sixth component generator
     * @return a {@code Generator<Tuple6<A, B, C, D, E, F>}
     * @see Generators#product(Generator, Generator, Generator, Generator, Generator, Generator, Fn6)
     */
    public static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> a,
                                                                                Generator<B> b,
                                                                                Generator<C> c,
                                                                                Generator<D> d,
                                                                                Generator<E> e,
                                                                                Generator<F> f) {
        return Products.product(a, b, c, d, e, f, Tuple6::tuple);
    }

    /**
     * Creates a {@link Generator} that yields {@link Tuple7}s by combining the outputs of seven other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param c   the third component generator
     * @param d   the fourth component generator
     * @param e   the fifth component generator
     * @param f   the sixth component generator
     * @param g   the seventh component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @param <C> the type of the values generated by the third component generator
     * @param <D> the type of the values generated by the fourth component generator
     * @param <E> the type of the values generated by the fifth component generator
     * @param <F> the type of the values generated by the sixth component generator
     * @param <G> the type of the values generated by the seventh component generator
     * @return a {@code Generator<Tuple7<A, B, C, D, E, F, G>}
     * @see Generators#product(Generator, Generator, Generator, Generator, Generator, Generator, Generator, Fn7)
     */
    public static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> a,
                                                                                      Generator<B> b,
                                                                                      Generator<C> c,
                                                                                      Generator<D> d,
                                                                                      Generator<E> e,
                                                                                      Generator<F> f,
                                                                                      Generator<G> g) {
        return Products.product(a, b, c, d, e, f, g, Tuple7::tuple);
    }

    /**
     * Creates a {@link Generator} that yields {@link Tuple8}s by combining the outputs of eight other {@code Generators}.
     *
     * @param a   the first component generator
     * @param b   the second component generator
     * @param c   the third component generator
     * @param d   the fourth component generator
     * @param e   the fifth component generator
     * @param f   the sixth component generator
     * @param g   the seventh component generator
     * @param h   the eighth component generator
     * @param <A> the type of the values generated by the first component generator
     * @param <B> the type of the values generated by the second component generator
     * @param <C> the type of the values generated by the third component generator
     * @param <D> the type of the values generated by the fourth component generator
     * @param <E> the type of the values generated by the fifth component generator
     * @param <F> the type of the values generated by the sixth component generator
     * @param <G> the type of the values generated by the seventh component generator
     * @param <H> the type of the values generated by the eighth component generator
     * @return a {@code Generator<Tuple8<A, B, C, D, E, F, G, H>}
     * @see Generators#product(Generator, Generator, Generator, Generator, Generator, Generator, Generator, Generator, Fn8)
     */
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

    /**
     * Converts a sequence of {@link Generator}s to a {@code Generator} of sequences.
     *
     * @param gs  a sequence of zero or more {@code Generator<A>}s
     * @param <A> the element type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> sequence(Iterable<Generator<A>> gs) {
        return Sequence.sequence(gs);
    }

    /**
     * Converts a non-empty sequence of {@link Generator}s to a {@code Generator} of non-empty sequences.
     *
     * @param gs  a sequence of one or more {@code Generator<A>}s
     * @param <A> the element type
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> sequenceNonEmpty(NonEmptyIterable<Generator<A>> gs) {
        return Sequence.sequenceNonEmpty(gs);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s of printable ASCII characters, with varying lengths.
     *
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateString() {
        return Strings.generateString();
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s of printable ASCII characters of a specific length.
     * <p>
     * The length of the generated {@code String}s will always be {@code length}.
     *
     * @param length the length of the generated strings.  If &lt;=0, then all strings will be empty.
     * @return a {@code Generator<String>}
     */
    static Generator<String> generateString(int length) {
        return Strings.generateString(length);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s of printable ASCII characters, with varying lengths
     * within a specific range.
     * <p>
     * The length of the generated {@code String}s will always fall within {@code lengthRange}.
     *
     * @param lengthRange the length range of the generated strings.
     * @return a {@code Generator<String>}
     */
    static Generator<String> generateString(IntRange lengthRange) {
        return Strings.generateString(lengthRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking another {@code Generator} a specific number
     * of times, and concatenating the results.
     *
     * @param numberOfChunks the number of chunks to generate and concatenate
     * @param chunkGenerator the generator for each chunk to be concatenated
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateString(int numberOfChunks, Generator<String> chunkGenerator) {
        return Strings.generateString(numberOfChunks, chunkGenerator);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking another {@code Generator} a random number
     * of times within a specific range, and concatenating the results.
     *
     * @param numberOfChunksRange the range of the number of chunks to generate and concatenate
     * @param chunkGenerator      the generator for each chunk to be concatenated
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateString(IntRange numberOfChunksRange, Generator<String> chunkGenerator) {
        return Strings.generateString(numberOfChunksRange, chunkGenerator);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a {@code Generator<Character>} a varying number of times,
     * and concatenating the results.
     *
     * @param g the generator for each character to be concatenated
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateStringFromCharacters(Generator<Character> g) {
        return Strings.generateStringFromCharacters(g);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by choosing from a collection of {@code Character}s a varying number of times,
     * and concatenating the results.
     *
     * @param characters the characters to choose from
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateStringFromCharacters(NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(characters);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a {@code Generator<Character>} a specific number of times,
     * and concatenating the results.
     * <p>
     * The length of the generated {@code String}s will always be {@code length}.
     *
     * @param g the generator for each character to be concatenated
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateStringFromCharacters(int length, Generator<Character> g) {
        return Strings.generateStringFromCharacters(length, g);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by choosing from a collection of {@code Character}s a specific number of times,
     * and concatenating the results.
     * <p>
     * The length of the generated {@code String}s will always be {@code length}.
     *
     * @param characters the characters to choose from
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateStringFromCharacters(int length, NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(length, characters);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a {@code Generator<Character>} a varying number of times
     * within a specific range, and concatenating the results.
     * <p>
     * The length of the generated {@code String}s will always be within {@code lengthRange}.
     *
     * @param lengthRange the range of the length of the string to generate
     * @param g           the generator for each character to be concatenated
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateStringFromCharacters(IntRange lengthRange, Generator<Character> g) {
        return Strings.generateStringFromCharacters(lengthRange, g);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by choosing from a collection of {@code Character}s a varying number of times
     * within a specific range, and concatenating the results.
     * <p>
     * The length of the generated {@code String}s will always be within {@code lengthRange}.
     *
     * @param lengthRange the range of the length of the string to generate
     * @param characters  the characters to choose from
     * @return a {@code Generator<String>}
     */
    public static Generator<String> generateStringFromCharacters(IntRange lengthRange, NonEmptyVector<Character> characters) {
        return Strings.generateStringFromCharacters(lengthRange, characters);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking one or more {@code Generator<String>}s in order and concatenating the result.
     *
     * @param first the generator of the first string to be concatenated
     * @param more  the generators of the remaining strings to be concatenated
     * @return a {@code Generator<String>}
     */
    @SafeVarargs
    public static Generator<String> generateString(Generator<String> first, Generator<String>... more) {
        return Strings.generateString(first, more);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s that are legal Java identifiers.  May also yield empty strings.
     *
     * @return a {@code Generator<String>}
     * @see Generators#generateIdentifier(int)
     * @see Generators#generateIdentifier(IntRange)
     */
    public static Generator<String> generateIdentifier() {
        return Strings.generateIdentifier();
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s that are legal Java identifiers of a specific length.
     *
     * @param length the length of the identifiers to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateIdentifier()
     * @see Generators#generateIdentifier(IntRange)
     */
    public static Generator<String> generateIdentifier(int length) {
        return Strings.generateIdentifier(length);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s that are legal Java identifiers with a length within a specific range.
     *
     * @param lengthRange the range of the length of the identifiers to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateIdentifier()
     * @see Generators#generateIdentifier(int)
     */
    public static Generator<String> generateIdentifier(IntRange lengthRange) {
        return Strings.generateIdentifier(lengthRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a sequence of component {@code Generator<String>}s
     * and concatenating the results.
     *
     * @param components a sequence of {@code Generator<String>}s to be invoked to generate the components of the output
     * @return a {@code Generator<String>}
     * @see Generators#concatStrings(String, Iterable)
     * @see Generators#concatStrings(Generator, Iterable)
     */
    public static Generator<String> concatStrings(Iterable<Generator<String>> components) {
        return Strings.concatStrings(components);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a sequence of component {@code Generator<String>}s
     * and concatenating the results, separating them with a specific separator.
     *
     * @param separator  a {@code String} that will be inserted between each of the components of the output
     * @param components a sequence of {@code Generator<String>}s to be invoked to generate the components of the output
     * @return a {@code Generator<String>}
     * @see Generators#concatStrings(Iterable)
     * @see Generators#concatStrings(Generator, Iterable)
     */
    public static Generator<String> concatStrings(String separator, Iterable<Generator<String>> components) {
        return Strings.concatStrings(separator, components);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a sequence of component {@code Generator<String>}s
     * and concatenating the results, separating them by a separator that is also generated.
     *
     * @param separator  a {@code Generator<String>} to generate the separator strings.
     *                   This generator is invoked every time a separator is called for (i.e., between the invocations of each component generator).
     *                   Note that this can potentially result in a different separator used within the same result.
     * @param components a sequence of {@code Generator<String>}s to be invoked to generate the components of the output
     * @return a {@code Generator<String>}
     * @see Generators#concatStrings(Iterable)
     * @see Generators#concatStrings(String, Iterable)
     */
    public static Generator<String> concatStrings(Generator<String> separator, Iterable<Generator<String>> components) {
        return Strings.concatStrings(separator, components);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a sequence of component {@code Generator<Maybe<String>>}s
     * and concatenating the results.
     *
     * @param components a sequence of {@code Generator<Maybe<String>>}s to be invoked to generate the components of the output.
     *                   If a generator outputs a {@link Maybe#just(Object)}, it will be concatenated to the final output, otherwise ignored.
     * @return a {@code Generator<String>}
     * @see Generators#concatMaybeStrings(String, Iterable)
     * @see Generators#concatMaybeStrings(Generator, Iterable)
     */
    public static Generator<String> concatMaybeStrings(Iterable<Generator<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(components);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a sequence of component {@code Generator<Maybe<String>>}s
     * and concatenating the results, separating them with a specific separator.
     *
     * @param separator  a {@code String} that will be inserted between each of the components of the output (but only those that yield a {@link Maybe#just(Object)}
     * @param components a sequence of {@code Generator<Maybe<String>>}s to be invoked to generate the components of the output.
     *                   If a generator outputs a {@code Maybe#just}, it will be concatenated to the final output, otherwise ignored.
     * @return a {@code Generator<String>}
     * @see Generators#concatMaybeStrings(Iterable)
     * @see Generators#concatMaybeStrings(Generator, Iterable)
     */
    public static Generator<String> concatMaybeStrings(String separator, Iterable<Generator<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(separator, components);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s by invoking a sequence of component {@code Generator<Maybe<String>>}s
     * and concatenating the results, separating them by a separator that is also generated.
     *
     * @param separator  a {@code Generator<String>} to generate the separator strings.
     *                   This generator is invoked every time a separator is called for (i.e., between the invocations of each component generator that yield a {@link Maybe#just(Object)}).
     *                   Note that this can potentially result in a different separator used within the same result.
     * @param components a sequence of {@code Generator<Maybe<String>>}s to be invoked to generate the components of the output.
     *                   If a generator outputs a {@code Maybe#just}, it will be concatenated to the final output, otherwise ignored.
     * @return a {@code Generator<String>}
     * @see Generators#concatMaybeStrings(Iterable)
     * @see Generators#concatMaybeStrings(String, Iterable)
     */
    public static Generator<String> concatMaybeStrings(Generator<String> separator, Iterable<Generator<Maybe<String>>> components) {
        return Strings.concatMaybeStrings(separator, components);
    }

    /**
     * Instantiates a {@link CompoundStringBuilder}.
     *
     * @return a {@code CompoundStringBuilder}
     */
    public static CompoundStringBuilder compoundStringBuilder() {
        return ConcreteCompoundStringBuilder.builder();
    }

    /**
     * Creates a {@link Generator} that, when invoked, always yields {@code null} of a specific type.
     *
     * @param <A> the type of the output
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateNull() {
        return Nulls.generateNull();
    }

    /**
     * Creates a {@link Generator} that mixes occasional {@code null} values into the output of an existing {@code Generator}.
     *
     * @param gen the original generator
     * @param <A> the type of the output
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateWithNulls(Generator<A> gen) {
        return Nulls.generateWithNulls(gen);
    }

    /**
     * Creates a {@link Generator} that mixes occasional {@code null} values into the output of an existing {@code Generator},
     * with a specific probability for null.
     *
     * @param weights the probability for a null value to occur in the output
     * @param gen     the original generator
     * @param <A>     the type of the output
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateWithNulls(NullWeights weights, Generator<A> gen) {
        return Nulls.generateWithNulls(weights, gen);
    }

    /**
     * Creates a {@link Generator} that, when invoked, always yields {@code Unit.UNIT}.
     *
     * @return a {@code Generator<Unit>}
     */
    public static Generator<Unit> generateUnit() {
        return CoProducts.generateUnit();
    }

    /**
     * Creates a {@link Generator} that, when invoked, always yields {@code true}.
     *
     * @return a {@code Generator<Boolean>}
     */
    public static Generator<Boolean> generateTrue() {
        return CoProducts.generateTrue();
    }

    /**
     * Creates a {@link Generator} that, when invoked, always yields {@code false}.
     *
     * @return a {@code Generator<Boolean>}
     */
    public static Generator<Boolean> generateFalse() {
        return CoProducts.generateFalse();
    }

    /**
     * Converts a {@link Generator}{@code <A>} into a {@code Generator<Maybe<A>>} that most of the time yields a {@code just},
     * but will occasionally yield a {@code nothing}.
     *
     * @param gen the generator to convert
     * @param <A> the output type of the generator to convert
     * @return a {@code Generator<Maybe<A>>}
     */
    public static <A> Generator<Maybe<A>> generateMaybe(Generator<A> gen) {
        return CoProducts.generateMaybe(gen);
    }

    /**
     * Converts a {@link Generator}{@code <A>} into a {@code Generator<Maybe<A>>}, with custom probabilities
     * for yielding {@code just} vs. {@code nothing}.
     *
     * @param weights the probabilities for {@code just} vs. {@code nothing}
     * @param gen     the generator to convert
     * @param <A>     the output type of the generator to convert
     * @return a {@code Generator<Maybe<A>>}
     */
    public static <A> Generator<Maybe<A>> generateMaybe(MaybeWeights weights, Generator<A> gen) {
        return CoProducts.generateMaybe(weights, gen);
    }

    /**
     * Converts a {@link Generator}{@code <A>} into a {@code Generator<Maybe<A>>} that will always yield {@code just}.
     *
     * @param gen the generator to convert
     * @param <A> the output type of the generator to convert
     * @return a {@code Generator<Maybe<A>>}
     */
    public static <A> Generator<Maybe<A>> generateJust(Generator<A> gen) {
        return CoProducts.generateJust(gen);
    }

    /**
     * Creates a {@link Generator} that, when invoked, always yields {@link Maybe#nothing()}.
     *
     * @param <A> the output type of the generator to convert
     * @return a {@code Generator<Maybe<A>>}
     */
    public static <A> Generator<Maybe<A>> generateNothing() {
        return CoProducts.generateNothing();
    }

    /**
     * Creates a {@link Generator} that yields {@link Either}s, with equal probabilities for returning a {@code left}
     * or a {@code right}.
     *
     * @param leftGen  the generator of left values
     * @param rightGen the generator of right values
     * @param <L>      the left type
     * @param <R>      the right type
     * @return a {@code Generator<Either<L, R>>}
     */
    public static <L, R> Generator<Either<L, R>> generateEither(Generator<L> leftGen, Generator<R> rightGen) {
        return CoProducts.generateEither(leftGen, rightGen);
    }

    /**
     * Creates a {@link Generator} that yields {@link Either}s, with custom probabilities for returning a {@code left}
     * or a {@code right}.
     *
     * @param weights  the probabilities for {@code left} vs. {@code right}
     * @param leftGen  the generator of left values
     * @param rightGen the generator of right values
     * @param <L>      the left type
     * @param <R>      the right type
     * @return a {@code Generator<Either<L, R>>}
     */
    public static <L, R> Generator<Either<L, R>> generateEither(EitherWeights weights, Generator<L> leftGen, Generator<R> rightGen) {
        return CoProducts.generateEither(weights, leftGen, rightGen);
    }

    /**
     * Converts a {@link Generator}{@code <L>} into a {@code Generator<Either<L, R>>} that will always yield {@code left}.
     *
     * @param gen the generator of left values
     * @param <L> the left type
     * @param <R> the right type
     * @return a {@code Generator<Either<L, R>>}
     */
    public static <L, R> Generator<Either<L, R>> generateLeft(Generator<L> gen) {
        return CoProducts.generateLeft(gen);
    }

    /**
     * Converts a {@link Generator}{@code <L>} into a {@code Generator<Either<L, R>>} that will always yield {@code right}.
     *
     * @param gen the generator of right values
     * @param <L> the left type
     * @param <R> the right type
     * @return a {@code Generator<Either<L, R>>}
     */
    public static <L, R> Generator<Either<L, R>> generateRight(Generator<R> gen) {
        return CoProducts.generateRight(gen);
    }

    /**
     * Creates a {@link Generator} that yields {@link These}s, with equal probabilities for returning a value from {@code generatorA},
     * {@code generatorB}, or a combination of both.
     *
     * @param generatorA the generator of type {@code A}
     * @param generatorB the generator of type {@code B}
     * @param <A>        the first possible type
     * @param <B>        the second possible type
     * @return a {@code Generator<These<A, B>>}
     */
    public static <A, B> Generator<These<A, B>> generateThese(Generator<A> generatorA, Generator<B> generatorB) {
        return CoProducts.generateThese(generatorA, generatorB);
    }

    /**
     * Creates a {@link Generator} that yields {@link These}s, with custom probabilities for returning a value from {@code generatorA},
     * {@code generatorB}, or a combination of both.
     *
     * @param weights    the probabilities for returning a value for {@code generatorA} vs. {@code generatorB} vs. both
     * @param generatorA the generator of type {@code A}
     * @param generatorB the generator of type {@code B}
     * @param <A>        the first possible type
     * @param <B>        the second possible type
     * @return a {@code Generator<These<A, B>>}
     */
    public static <A, B> Generator<These<A, B>> generateThese(TernaryWeights weights, Generator<A> generatorA, Generator<B> generatorB) {
        return CoProducts.generateThese(weights, generatorA, generatorB);
    }

    /**
     * Creates a {@link Generator} that chooses values from an enum.
     *
     * @param enumType the class of the enum type
     * @param <A>      the enum type
     * @return a {@code Generator<A>}
     */
    public static <A extends Enum<A>> Generator<A> generateFromEnum(Class<A> enumType) {
        return Enums.generateFromEnum(enumType);
    }

    /**
     * Creates a {@link Generator} that, when invoked, randomly selects from a list of candidate {@code Generator}s,
     * (with equal probabilities for each).  The output is then drawn from the chosen {@code Generator}.
     *
     * @param first the first candidate {@code Generator}
     * @param more  the remaining candidate {@code Generator}s
     * @param <A>   the output type
     * @return a {@code Generator<A>}
     */
    @SafeVarargs
    public static <A> Generator<A> chooseOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseOneOf(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, randomly selects from a list of candidate {@code Generator}s,
     * (with equal probabilities for each).  The output is then drawn from the chosen {@code Generator}.
     *
     * @param first the first weighted candidate {@code Generator}
     * @param more  the remaining weighted candidates {@code Generator}s
     * @param <A>   the output type
     * @return a {@code Generator<A>}
     */
    @SafeVarargs
    public static <A> Generator<A> chooseOneOf(Weighted<? extends Generator<? extends A>> first,
                                               Weighted<? extends Generator<? extends A>>... more) {
        return Choose.chooseOneOf(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, randomly chooses an item from one or more candidate values, with an equal probability for each.
     *
     * @param first the first candidate value
     * @param more  the remaining candidate values
     * @param <A>   the output type
     * @return a {@code Generator<A>}
     */
    @SafeVarargs
    public static <A> Generator<A> chooseOneOfValues(A first, A... more) {
        return Choose.chooseOneOfValues(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, randomly chooses and item from one or more candidate values, with custom probabilities for each.
     *
     * @param first the first weighted candidate value
     * @param more  the remaining weighted candidate values
     * @param <A>   the output type
     * @return a {@code Generator<A>}
     */
    @SafeVarargs
    public static <A> Generator<A> chooseOneOfWeightedValues(Weighted<? extends A> first,
                                                             Weighted<? extends A>... more) {
        return Choose.chooseOneOfWeightedValues(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses one or more of the supplied candidate {@code Generator}s.
     * The chosen {@code Generator}s are then invoked, and the outputs are collected for the result.
     *
     * @param first the first candidate {@code Generator}
     * @param more  the remaining candidate {@code Generator}s
     * @param <A>   the output type
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    @SafeVarargs
    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses one or more of the supplied candidate values, and returns
     * a collection of all those chosen.
     *
     * @param first the first candidate value
     * @param more  the remaining candidate values
     * @param <A>   the output type
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    @SafeVarargs
    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return Choose.chooseAtLeastOneOfValues(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses zero or more of the supplied candidate {@code Generator}s.
     * The chosen {@code Generator}s are then invoked, and the outputs are collected for the result.
     *
     * @param first the first candidate {@code Generator}
     * @param more  the remaining candidate {@code Generator}s
     * @param <A>   the output type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    @SafeVarargs
    public static <A> Generator<ImmutableVector<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseSomeOf(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses zero or more of the supplied candidate values,
     * and returns a collection of all those chosen.
     *
     * @param first the first candidate {@code Generator}
     * @param more  the remaining candidate {@code Generator}s
     * @param <A>   the output type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    @SafeVarargs
    public static <A> Generator<ImmutableVector<A>> chooseSomeOfValues(A first, A... more) {
        return Choose.chooseSomeOf(first, more);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a value from a collection, with an equal probability
     * for each element.
     *
     * @param <A>        the output type
     * @param candidates the collection of candidate values.  Must have at least one element.
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> chooseOneValueFromCollection(Iterable<A> candidates) {
        return Choose.chooseOneValueFromCollection(candidates);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a {@code Generator} from a collection of candidate {@code Generator}s
     * (with an equal probability for each).  The output is then drawn from the chosen {@code Generator}.
     *
     * @param <A>        the output type
     * @param candidates the collection of candidate {@code Generator}s.  Must have at least one element.
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> chooseOneFromCollection(Iterable<Generator<? extends A>> candidates) {
        return Choose.chooseOneFromCollection(candidates);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a {@code Generator} from a collection of candidate {@code Generator}s
     * (with a custom probability for each).  The output is then drawn from the chosen {@code Generator}.
     *
     * @param <A>        the output type
     * @param candidates the collection of weighted candidate {@code Generator}s.  Must have at least one element.
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> chooseOneFromCollectionWeighted(Iterable<Weighted<? extends Generator<? extends A>>> candidates) {
        return Choose.chooseOneFromCollectionWeighted(candidates);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a value from a custom domain.
     *
     * @param domain the collection of candidates values
     * @param <A>    the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> chooseOneValueFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseOneValueFromDomain(domain);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses one or more values from a collection and returns
     * a collection of the values chosen.
     *
     * @param candidates the collection of candidate values.  Must have at least one element.
     * @param <A>        the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneValueFromCollection(Collection<A> candidates) {
        return Choose.chooseAtLeastOneValueFromCollection(candidates);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses one or more values from a custom domain and returns
     * a collection of the values chosen.
     *
     * @param domain the collection of candidate values
     * @param <A>    the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneValueFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseAtLeastOneValueFromDomain(domain);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses zero or more values from a collection and returns
     * a collection of the values chosen.
     *
     * @param candidates the collection of candidate values
     * @param <A>        the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<ImmutableVector<A>> chooseSomeValuesFromCollection(Collection<A> candidates) {
        return Choose.chooseSomeValuesFromDomain(candidates);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses zero or more values from a custom domain and returns
     * a collection of the values chosen.
     *
     * @param domain the collection of candidate values
     * @param <A>    the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<ImmutableVector<A>> chooseSomeValuesFromDomain(NonEmptyVector<A> domain) {
        return Choose.chooseSomeValuesFromDomain(domain);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a {@link Map.Entry} from a {@code Map}.
     *
     * @param map a map with all candidate entries.  Must contain at least one entry.
     * @param <K> the key type
     * @param <V> the value type
     * @return a {@code Generator<Map.Entry<K, V>>}
     */
    public static <K, V> Generator<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        return Choose.chooseEntryFromMap(map);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a key from a {@code Map}.
     *
     * @param map a map with all candidate keys.  Must contain at least one key.
     * @param <K> the key type
     * @param <V> the value type
     * @return a {@code Generator<K>}
     */
    public static <K, V> Generator<K> chooseKeyFromMap(Map<K, V> map) {
        return Choose.chooseKeyFromMap(map);
    }

    /**
     * Creates a {@link Generator} that, when invoked, chooses a value from a {@code Map}.
     *
     * @param map a map with all candidate values.  Must contain at least one value.
     * @param <K> the key type
     * @param <V> the value type
     * @return a {@code Generator<V>}
     */
    public static <K, V> Generator<V> chooseValueFromMap(Map<K, V> map) {
        return Choose.chooseValueFromMap(map);
    }

    /**
     * Creates a {@link Generator} that chooses its values from a {@link FrequencyMap}.
     *
     * @param frequencyMap the {@code FrequencyMap}
     * @param <A>          the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> frequency(FrequencyMap<A> frequencyMap) {
        return Choose.frequency(frequencyMap);
    }

    public static <A> Generator<ArrayList<A>> generateArrayList(Generator<A> g) {
        return Collections.generateArrayList(g);
    }

    public static <A> Generator<ArrayList<A>> generateNonEmptyArrayList(Generator<A> g) {
        return Collections.generateNonEmptyArrayList(g);
    }

    public static <A> Generator<ArrayList<A>> generateArrayListOfSize(int size, Generator<A> g) {
        return Collections.generateArrayListOfSize(size, g);
    }

    public static <A> Generator<ArrayList<A>> generateArrayListOfSize(IntRange sizeRange, Generator<A> g) {
        return Collections.generateArrayListOfSize(sizeRange, g);
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

    public static <A> Generator<ImmutableVector<A>> generateVectorOfSize(int size, Generator<A> g) {
        return Collections.generateVectorOfSize(size, g);
    }

    public static <A> Generator<ImmutableVector<A>> generateVectorOfSize(IntRange sizeRange, Generator<A> g) {
        return Collections.generateVectorOfSize(sizeRange, g);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> g) {
        return Collections.generateNonEmptyVector(g);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(int size, Generator<A> g) {
        return Collections.generateNonEmptyVectorOfSize(size, g);
    }

    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(IntRange sizeRange, Generator<A> g) {
        return Collections.generateNonEmptyVectorOfSize(sizeRange, g);
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

    public static Generator<BigDecimal> generateBigDecimal() {
        return BigNumbers.generateBigDecimal();
    }

    public static Generator<BigDecimal> generateBigDecimal(BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(range);
    }

    public static Generator<BigDecimal> generateBigDecimal(Generator<Integer> generateDecimalPlaces, BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(generateDecimalPlaces, range);
    }

    public static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(decimalPlaces, range);
    }

    public static Generator<Month> generateMonth() {
        return Temporal.generateMonth();
    }

    public static Generator<DayOfWeek> generateDayOfWeek() {
        return Temporal.generateDayOfWeek();
    }

    public static Generator<LocalDate> generateLocalDate() {
        return Temporal.generateLocalDate();
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

    public static Generator<LocalDateTime> generateLocalDateTime() {
        return Temporal.generateLocalDateTime();
    }

    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateRange dateRange) {
        return Temporal.generateLocalDateTime(dateRange);
    }

    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateTimeRange range) {
        return Temporal.generateLocalDateTime(range);
    }

    public static Generator<Duration> generateDuration() {
        return Temporal.generateDuration();
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

    public static Generator<DoubleRange> generateDoubleRange() {
        return Ranges.generateDoubleRange();
    }

    public static Generator<DoubleRange> generateDoubleRange(DoubleRange parentRange) {
        return Ranges.generateDoubleRange(parentRange);
    }

    public static Generator<FloatRange> generateFloatRange() {
        return Ranges.generateFloatRange();
    }

    public static Generator<FloatRange> generateFloatRange(FloatRange parentRange) {
        return Ranges.generateFloatRange(parentRange);
    }

    public static Generator<BigIntegerRange> generateBigIntegerRange() {
        return Ranges.generateBigIntegerRange();
    }

    public static Generator<BigIntegerRange> generateBigIntegerRange(BigIntegerRange parentRange) {
        return Ranges.generateBigIntegerRange(parentRange);
    }

    public static Generator<BigDecimalRange> generateBigDecimalRange() {
        return Ranges.generateBigDecimalRange();
    }

    public static Generator<BigDecimalRange> generateBigDecimalRange(BigDecimalRange parentRange) {
        return Ranges.generateBigDecimalRange(parentRange);
    }

    public static Generator<LocalDateRange> generateLocalDateRange() {
        return Ranges.generateLocalDateRange();
    }

    public static Generator<LocalDateRange> generateLocalDateRange(LocalDateRange parentRange) {
        return Ranges.generateLocalDateRange(parentRange);
    }

    public static Generator<LocalTimeRange> generateLocalTimeRange() {
        return Ranges.generateLocalTimeRange();
    }

    public static Generator<LocalTimeRange> generateLocalTimeRange(LocalTimeRange parentRange) {
        return Ranges.generateLocalTimeRange(parentRange);
    }

    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange() {
        return Ranges.generateLocalDateTimeRange();
    }

    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateRange parentRange) {
        return Ranges.generateLocalDateTimeRange(parentRange);
    }

    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateTimeRange parentRange) {
        return Ranges.generateLocalDateTimeRange(parentRange);
    }

    public static Generator<DurationRange> generateDurationRange() {
        return Ranges.generateDurationRange();
    }

    public static Generator<DurationRange> generateDurationRange(DurationRange parentRange) {
        return Ranges.generateDurationRange(parentRange);
    }
}
