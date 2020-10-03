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

import static dev.marksman.kraftwerk.Collections.generateCollectionSize;

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
     * @param range the {@link DoubleRange} of values to generate
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
     * @param range the {@link FloatRange} of values to generate
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
     * @param range the {@link IntRange} of values to generate
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
     * @param range the {@link LongRange} of values to generate
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
     * @param range the {@link ByteRange} of values to generate
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
     * @param range the {@link ShortRange} of values to generate
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
     * @param range the {@link CharRange} of values to generate
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateChar(CharRange range) {
        return Primitives.generateChar(range);
    }

    /**
     * Creates a {@link Generator} that yields ASCII alphabetic {@link Character}s (A-Z, a-z).
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateAlphaChar() {
        return Strings.generateAlphaChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII uppercase alphabetic {@link Character}s (A-Z).
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateAlphaUpperChar() {
        return Strings.generateAlphaUpperChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII lowercase alphabetic {@link Character}s (a-z).
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateAlphaLowerChar() {
        return Strings.generateAlphaLowerChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII alphanumeric {@link Character}s (A-Z, a-z, 0-9).
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateAlphanumericChar() {
        return Strings.generateAlphanumericChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII numeric {@link Character}s (0-9).
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateNumericChar() {
        return Strings.generateNumericChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII punctuation {@link Character}s.
     * This include any ASCII character that is not a letter, digit, space, or control character.
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generatePunctuationChar() {
        return Strings.generatePunctuationChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII printable {@link Character}s.
     * This include any ASCII character that is not a control character.
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateAsciiPrintableChar() {
        return Strings.generateAsciiPrintableChar();
    }

    /**
     * Creates a {@link Generator} that yields ASCII control {@link Character}s.
     * A control character is any character from ASCII code 0-31.
     *
     * @return a {@code Generator<Character>}
     */
    public static Generator<Character> generateControlChar() {
        return Strings.generateControlChar();
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
            return sized(n -> fn.apply(Math.max(n, minimum)));
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
    public static <A, C extends Collection<A>> Generator<C> generateCollection(Fn0<C> constructCollection,
                                                                               Iterable<Generator<A>> elements) {
        return Collections.generateCollection(constructCollection, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link Collection}s of type {@code C} by invoking an
     * element {@code Generator}s a specific number of times, and then aggregating the results.
     * <p>
     * The size of the generated {@code Collection}s will always equal {@code size}.
     *
     * @param constructCollection the constructor for the desired type of collection (e.g. {@code ArrayList::new})
     * @param size                the size of the output collection.  Must be &gt;= 0.
     * @param elements            the element {@code Generator}
     * @param <A>                 the element type of the desired collection
     * @param <C>                 the collection type.   Instances of {@code C} must support {@link Collection#add} (which is to say, must not throw on invocation).
     * @return a {@code Generator<C>}
     */
    public static <A, C extends Collection<A>> Generator<C> generateCollection(Fn0<C> constructCollection,
                                                                               int size,
                                                                               Generator<A> elements) {
        return Collections.generateCollection(constructCollection, size, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link Collection}s of type {@code C} by invoking an
     * element {@code Generator}s a random number of times (within a specific range), and then aggregating the results.
     * <p>
     * The size of the generated {@code Collection}s will always fall within {@code sizeRange}.
     *
     * @param constructCollection the constructor for the desired type of collection (e.g. {@code ArrayList::new})
     * @param sizeRange           the {@link IntRange} of sizes for the generated collections.  Lower end of range must be &gt;= 0.
     * @param elements            the element {@code Generator}
     * @param <A>                 the element type of the desired collection
     * @param <C>                 the collection type.   Instances of {@code C} must support {@link Collection#add} (which is to say, must not throw on invocation).
     * @return a {@code Generator<C>}
     */
    public static <A, C extends Collection<A>> Generator<C> generateCollection(Fn0<C> constructCollection,
                                                                               IntRange sizeRange,
                                                                               Generator<A> elements) {
        return Collections.generateCollection(constructCollection, sizeRange, elements);
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
     * @see Generators#generateTuple(Generator, Generator)
     */
    public static <A, B, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateTuple(Generator, Generator, Generator)
     */
    public static <A, B, C, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateTuple(Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateTuple(Generator, Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, E, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateTuple(Generator, Generator, Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, E, F, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateTuple(Generator, Generator, Generator, Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, E, F, G, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateTuple(Generator, Generator, Generator, Generator, Generator, Generator, Generator, Generator)
     */
    public static <A, B, C, D, E, F, G, H, Out> Generator<Out> generateProduct(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Fn2)
     */
    public static <A, B> Generator<Tuple2<A, B>> generateTuple(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Generator, Fn3)
     */
    public static <A, B, C> Generator<Tuple3<A, B, C>> generateTuple(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Generator, Generator, Fn4)
     */
    public static <A, B, C, D> Generator<Tuple4<A, B, C, D>> generateTuple(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Generator, Generator, Generator, Fn5)
     */
    public static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> generateTuple(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Generator, Generator, Generator, Generator, Fn6)
     */
    public static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> generateTuple(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Generator, Generator, Generator, Generator, Generator, Fn7)
     */
    public static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> generateTuple(Generator<A> a,
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
     * @see Generators#generateProduct(Generator, Generator, Generator, Generator, Generator, Generator, Generator, Generator, Fn8)
     */
    public static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> generateTuple(Generator<A> a,
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
     * @param numberOfChunksRange the {@link IntRange} of the number of chunks to generate and concatenate
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
     * @param lengthRange the {@link IntRange} of the length of the string to generate
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
     * @param lengthRange the {@link IntRange} of the length of the string to generate
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
     * @param lengthRange the {@link IntRange} of the length of the identifiers to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateIdentifier()
     * @see Generators#generateIdentifier(int)
     */
    public static Generator<String> generateIdentifier(IntRange lengthRange) {
        return Strings.generateIdentifier(lengthRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII alphabetic characters, both lowercase and uppercase.
     *
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaString(int)
     * @see Generators#generateAlphaString(IntRange)
     */
    public static Generator<String> generateAlphaString() {
        return Strings.generateAlphaString();
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII alphabetic characters, both lowercase and uppercase,
     * and with a specific length.
     *
     * @param length the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaString()
     * @see Generators#generateAlphaString(IntRange)
     */
    public static Generator<String> generateAlphaString(int length) {
        return Strings.generateAlphaString(length);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII alphabetic characters, both lowercase and uppercase,
     * and with a length within a specific range.
     *
     * @param lengthRange the {@link IntRange} of the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaString()
     * @see Generators#generateAlphaString(int)
     */
    public static Generator<String> generateAlphaString(IntRange lengthRange) {
        return Strings.generateAlphaString(lengthRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII uppercase alphabetic characters.
     *
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaUpperString(int)
     * @see Generators#generateAlphaUpperString(IntRange)
     */
    public static Generator<String> generateAlphaUpperString() {
        return Strings.generateAlphaUpperString();
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII uppercase alphabetic characters,
     * and with a specific length.
     *
     * @param length the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaUpperString()
     * @see Generators#generateAlphaUpperString(IntRange)
     */
    public static Generator<String> generateAlphaUpperString(int length) {
        return Strings.generateAlphaUpperString(length);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII uppercase alphabetic characters,
     * and with a length within a specific range.
     *
     * @param lengthRange the {@link IntRange} of the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaUpperString(int)
     * @see Generators#generateAlphaUpperString(IntRange)
     */
    public static Generator<String> generateAlphaUpperString(IntRange lengthRange) {
        return Strings.generateAlphaUpperString(lengthRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII lowercase alphabetic characters.
     *
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaLowerString(int)
     * @see Generators#generateAlphaLowerString(IntRange)
     */
    public static Generator<String> generateAlphaLowerString() {
        return Strings.generateAlphaLowerString();
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII lowercase alphabetic characters,
     * and with a specific length.
     *
     * @param length the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaLowerString()
     * @see Generators#generateAlphaLowerString(IntRange)
     */
    public static Generator<String> generateAlphaLowerString(int length) {
        return Strings.generateAlphaLowerString(length);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII lowercase alphabetic characters,
     * and with a length within a specific range.
     *
     * @param lengthRange the {@link IntRange} of the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaLowerString(int)
     * @see Generators#generateAlphaLowerString(IntRange)
     */
    public static Generator<String> generateAlphaLowerString(IntRange lengthRange) {
        return Strings.generateAlphaLowerString(lengthRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII alphanumeric characters (A-Z, a-z, 0-9).
     *
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaLowerString(int)
     * @see Generators#generateAlphaLowerString(IntRange)
     */
    public static Generator<String> generateAlphanumericString() {
        return Strings.generateAlphanumericString();
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII alphanumeric characters (A-Z, a-z, 0-9),
     * and with a specific length.
     *
     * @param length the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaLowerString()
     * @see Generators#generateAlphaLowerString(IntRange)
     */
    public static Generator<String> generateAlphanumericString(int length) {
        return Strings.generateAlphanumericString(length);
    }

    /**
     * Creates a {@link Generator} that yields {@link String}s consisting of ASCII alphanumeric characters (A-Z, a-z, 0-9),
     * and with a length within a specific range.
     *
     * @param lengthRange the {@link IntRange} of the length of the strings to generate
     * @return a {@code Generator<String>}
     * @see Generators#generateAlphaLowerString(int)
     * @see Generators#generateAlphaLowerString(IntRange)
     */
    public static Generator<String> generateAlphanumericString(IntRange lengthRange) {
        return Strings.generateAlphanumericString(lengthRange);
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
     * Instantiates a {@link StringGeneratorBuilder}.
     *
     * @return a {@code StringGeneratorBuilder}
     */
    public static StringGeneratorBuilder stringGeneratorBuilder() {
        return StringGeneratorBuilder.builder();
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
     * (with custom probabilities for each).  The output is then drawn from the chosen {@code Generator}.
     *
     * @param first the first weighted candidate {@code Generator}
     * @param more  the remaining weighted candidates {@code Generator}s
     * @param <A>   the output type
     * @return a {@code Generator<A>}
     */
    @SafeVarargs
    public static <A> Generator<A> chooseOneOfWeighted(Weighted<? extends Generator<? extends A>> first,
                                                       Weighted<? extends Generator<? extends A>>... more) {
        return Choose.chooseOneOfWeighted(first, more);
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
        return Choose.chooseSomeOfValues(first, more);
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
        return Choose.chooseSomeValuesFromCollection(candidates);
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

    /**
     * Creates a {@link Generator} that yields {@link ArrayList}s of various sizes.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ArrayList<A>>}
     */
    public static <A> Generator<ArrayList<A>> generateArrayList(Generator<A> elements) {
        return Collections.generateArrayList(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ArrayList}s of various sizes, with a minimum size of one.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ArrayList<A>>}
     */
    public static <A> Generator<ArrayList<A>> generateNonEmptyArrayList(Generator<A> elements) {
        return Collections.generateNonEmptyArrayList(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ArrayList}s of a specific size.
     *
     * @param size     the size of the {@code ArrayList}s returned; must be &gt;= 0
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ArrayList<A>>}
     */
    public static <A> Generator<ArrayList<A>> generateArrayListOfSize(int size, Generator<A> elements) {
        return Collections.generateArrayListOfSize(size, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ArrayList}s of various sizes, within a specific range.
     *
     * @param sizeRange the size range of the {@code ArrayList}s returned
     * @param elements  the generator for elements
     * @param <A>       the element type
     * @return a {@code Generator<ArrayList<A>>}
     */
    public static <A> Generator<ArrayList<A>> generateArrayListOfSize(IntRange sizeRange, Generator<A> elements) {
        return Collections.generateArrayListOfSize(sizeRange, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link HashSet}s of various sizes.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<HashSet<A>>}
     */
    public static <A> Generator<HashSet<A>> generateHashSet(Generator<A> elements) {
        return Collections.generateHashSet(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link HashSet}s of various sizes, with a minimum size of one.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<HashSet<A>>}
     */
    public static <A> Generator<HashSet<A>> generateNonEmptyHashSet(Generator<A> elements) {
        return Collections.generateNonEmptyHashSet(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableVector}s of various sizes.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> generateVector(Generator<A> elements) {
        return Collections.generateVector(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableVector}s of a specific size.
     *
     * @param size     the size of the {@code ImmutableVector}s returned; must be &gt;= 0
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> generateVectorOfSize(int size, Generator<A> elements) {
        return Collections.generateVectorOfSize(size, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableVector}s of a various sizes, within a specific range.
     *
     * @param sizeRange the size range of the {@code ImmutableVector}s returned
     * @param elements  the generator for elements
     * @param <A>       the element type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> generateVectorOfSize(IntRange sizeRange, Generator<A> elements) {
        return Collections.generateVectorOfSize(sizeRange, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableNonEmptyVector}s of various sizes.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> elements) {
        return Collections.generateNonEmptyVector(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableNonEmptyVector}s of a specific size.
     *
     * @param size     the size of the {@code ImmutableNonEmptyVector}s returned; must be &gt;= 1
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(int size, Generator<A> elements) {
        return Collections.generateNonEmptyVectorOfSize(size, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link ImmutableNonEmptyVector}s of a various sizes, within a specific range.
     *
     * @param sizeRange the size range of the {@code ImmutableNonEmptyVector}s returned
     * @param elements  the generator for elements
     * @param <A>       the element type
     * @return a {@code Generator<ImmutableNonEmptyVector<A>>}
     */
    public static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(IntRange sizeRange, Generator<A> elements) {
        return Collections.generateNonEmptyVectorOfSize(sizeRange, elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link Map}s of various sizes.
     *
     * @param generateKey   the generator for keys
     * @param generateValue the generator for values
     * @param <K>           the key type
     * @param <V>           the value type
     * @return a {@code Generator<Map<K, V>>}
     */
    public static <K, V> Generator<Map<K, V>> generateMap(Generator<K> generateKey,
                                                          Generator<V> generateValue) {
        return Collections.generateMap(generateKey, generateValue);
    }

    /**
     * Creates a {@link Generator} that yields {@link Map}s with a given set of keys.
     *
     * @param keys          the keys to populate in the output.  All keys will be in the output.
     * @param generateValue the generator for values
     * @param <K>           the key type
     * @param <V>           the value type
     * @return a {@code Generator<Map<K, V>>}
     */
    public static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                          Generator<V> generateValue) {
        return Collections.generateMap(keys, generateValue);
    }

    /**
     * Creates a {@link Generator} that yields {@link Map}s with a given set of keys.
     *
     * @param keys          the keys to populate in the output.  All keys will be in the output.
     * @param generateValue the generator for values
     * @param <K>           the key type
     * @param <V>           the value type
     * @return a {@code Generator<Map<K, V>>}
     */
    public static <K, V> Generator<Map<K, V>> generateMap(Vector<K> keys,
                                                          Generator<V> generateValue) {
        return Collections.generateMap(keys, generateValue);
    }

    /**
     * Creates a {@link Generator} that yields {@link Map}s of various sizes, with a minimum size of one.
     *
     * @param generateKey   the generator for keys
     * @param generateValue the generator for values
     * @param <K>           the key type
     * @param <V>           the value type
     * @return a {@code Generator<Map<K, V>>}
     */
    public static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> generateKey,
                                                                  Generator<V> generateValue) {
        return Collections.generateNonEmptyMap(generateKey, generateValue);
    }

    /**
     * Creates a {@link Generator} that yields infinite {@link Iterable}s.
     *
     * @param elements the generator for elements
     * @param <A>      the element type
     * @return a {@code Generator<ValueSupply<A>>}
     */
    public static <A> Generator<ValueSupply<A>> generateInfiniteIterable(Generator<A> elements) {
        return Infinite.generateInfiniteIterable(elements);
    }

    /**
     * Creates a {@link Generator} that yields {@link Vector}s that contain the integers from {@code 0}..{@code count-1}
     * in random order.
     *
     * @param count the number of elements in the output; must be &gt;= 0.
     * @return a {@code Generator<Vector<Integer>>}
     */
    public static Generator<Vector<Integer>> generateShuffled(int count) {
        return Shuffle.generateShuffled(count);
    }

    /**
     * Creates a {@link Generator} that yields {@link Vector}s by shuffling the indices from {@code 0}..{@code count-1}
     * in random order, and mapping them to a given function.
     *
     * @param count the number of elements in the output; must be &gt;= 0.
     * @param fn    a pure function that converts an integer from {@code 0}..{@code count-1} to an element; should be side-effect free.
     * @param <A>   the element type
     * @return a {@code Generator<Vector<A>>}
     */
    public static <A> Generator<Vector<A>> generateShuffled(int count, Fn1<Integer, A> fn) {
        return Shuffle.generateShuffled(count, fn);
    }

    /**
     * Creates a {@link Generator} that yields {@link NonEmptyVector}s by shuffling the indices from {@code 0}..{@code count-1}
     * in random order, and mapping them to a given function.
     *
     * @param count the number of elements in the output; must be &gt;= 1.
     * @param fn    a pure function that converts an integer from {@code 0}..{@code count-1} to an element; should be side-effect free.
     * @param <A>   the element type
     * @return a {@code Generator<NonEmptyVector<A>>}
     */
    public static <A> Generator<NonEmptyVector<A>> generateNonEmptyShuffled(int count, Fn1<Integer, A> fn) {
        return Shuffle.generateNonEmptyShuffled(count, fn);
    }

    /**
     * Creates a {@link Generator} that yields {@link Vector}s by randomly shuffling the order of the elements in the input.
     *
     * @param input the input sequence.  It is not altered, and is iterated at most once.
     * @param <A>   the element type
     * @return a {@code Generator<Vector<A>>}
     */
    public static <A> Generator<Vector<A>> generateShuffled(FiniteIterable<A> input) {
        return Shuffle.generateShuffled(input);
    }

    /**
     * Creates a {@link Generator} that yields {@link NonEmptyVector}s by randomly shuffling the order of the elements in the input.
     *
     * @param input the input sequence.  It is not altered, and is iterated at most once.
     * @param <A>   the element type
     * @return a {@code Generator<NonEmptyVector<A>>}
     */
    public static <A> Generator<NonEmptyVector<A>> generateShuffled(NonEmptyFiniteIterable<A> input) {
        return Shuffle.generateShuffled(input);
    }

    /**
     * Creates a {@link Generator} that yields {@link Vector}s by randomly shuffling the order of the elements in the input.
     *
     * @param input the input sequence.  It is not altered, and is iterated at most once.
     * @param <A>   the element type
     * @return a {@code Generator<Vector<A>>}
     */
    public static <A> Generator<Vector<A>> generateShuffled(Collection<A> input) {
        return Shuffle.generateShuffled(input);
    }

    /**
     * Creates a {@link Generator} that yields {@link Vector}s by randomly shuffling the order of the elements in the input.
     *
     * @param input the input sequence.  It is not altered, and is iterated at most once.
     * @param <A>   the element type
     * @return a {@code Generator<Vector<A>>}
     */
    public static <A> Generator<Vector<A>> generateShuffled(A[] input) {
        return Shuffle.generateShuffled(input);
    }

    /**
     * Creates a {@link Generator} that yields {@link Vector}s by randomly shuffling the order of the elements in the input.
     *
     * @param input the input {@code Vector}
     * @param <A>   the element type
     * @return a {@code Generator<Vector<A>>}
     */
    public static <A> Generator<Vector<A>> generateShuffled(Vector<A> input) {
        return generateShuffled(input.size(), input::unsafeGet);
    }

    /**
     * Creates a {@link Generator} that yields {@link NonEmptyVector}s by randomly shuffling the order of the elements in the input.
     *
     * @param input the input {@code NonEmptyVector}
     * @param <A>   the element type
     * @return a {@code Generator<NonEmptyVector<A>>}
     */
    public static <A> Generator<NonEmptyVector<A>> generateShuffled(NonEmptyVector<A> input) {
        return Shuffle.generateShuffled(input);
    }

    /**
     * Creates a {@link ChoiceBuilder1}.
     *
     * @param firstChoice a weighted {@link Generator} for the first choice
     * @param <A>         the output type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilder(Weighted<? extends Generator<? extends A>> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(firstChoice);
    }

    /**
     * Creates a {@link ChoiceBuilder1}.
     *
     * @param firstChoice a {@link Generator} for the first choice
     * @param <A>         the output type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilder(Generator<? extends A> firstChoice) {
        return ChoiceBuilder1.choiceBuilder(firstChoice);
    }

    /**
     * Creates a {@link ChoiceBuilder1}.
     *
     * @param firstChoice a weighted value for the first choice
     * @param <A>         the output type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilderValue(Weighted<? extends A> firstChoice) {
        return ChoiceBuilder1.choiceBuilderValue(firstChoice);
    }

    /**
     * Creates a {@link ChoiceBuilder1}.
     *
     * @param firstChoice a value for the first choice
     * @param <A>         the output type of the first choice
     * @return a {@code ChoiceBuilder1<A>}
     */
    public static <A> ChoiceBuilder1<A> choiceBuilderValue(A firstChoice) {
        return ChoiceBuilder1.choiceBuilderValue(firstChoice);
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice2}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @return a {@code Generator<Choice2<A, B>>}
     */
    public static <A, B> Generator<Choice2<A, B>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                 Weighted<? extends Generator<? extends B>> b) {
        return choiceBuilder(a).or(b).toGenerator();
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice3}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param c   a weighted {@code Generator} for the third choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @param <C> the output type of the third choice
     * @return a {@code Generator<Choice3<A, B, C>>}
     */
    public static <A, B, C> Generator<Choice3<A, B, C>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                       Weighted<? extends Generator<? extends B>> b,
                                                                       Weighted<? extends Generator<? extends C>> c) {
        return choiceBuilder(a).or(b).or(c).toGenerator();
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice4}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param c   a weighted {@code Generator} for the third choice
     * @param d   a weighted {@code Generator} for the fourth choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @param <C> the output type of the third choice
     * @param <D> the output type of the fourth choice
     * @return a {@code Generator<Choice4<A, B, C, D>>}
     */
    public static <A, B, C, D> Generator<Choice4<A, B, C, D>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                             Weighted<? extends Generator<? extends B>> b,
                                                                             Weighted<? extends Generator<? extends C>> c,
                                                                             Weighted<? extends Generator<? extends D>> d) {
        return choiceBuilder(a).or(b).or(c).or(d).toGenerator();
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice5}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param c   a weighted {@code Generator} for the third choice
     * @param d   a weighted {@code Generator} for the fourth choice
     * @param e   a weighted {@code Generator} for the fifth choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @param <C> the output type of the third choice
     * @param <D> the output type of the fourth choice
     * @param <E> the output type of the fifth choice
     * @return a {@code Generator<Choice5<A, B, C, D, E>>}
     */
    public static <A, B, C, D, E> Generator<Choice5<A, B, C, D, E>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                   Weighted<? extends Generator<? extends B>> b,
                                                                                   Weighted<? extends Generator<? extends C>> c,
                                                                                   Weighted<? extends Generator<? extends D>> d,
                                                                                   Weighted<? extends Generator<? extends E>> e) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).toGenerator();
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice6}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param c   a weighted {@code Generator} for the third choice
     * @param d   a weighted {@code Generator} for the fourth choice
     * @param e   a weighted {@code Generator} for the fifth choice
     * @param f   a weighted {@code Generator} for the sixth choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @param <C> the output type of the third choice
     * @param <D> the output type of the fourth choice
     * @param <E> the output type of the fifth choice
     * @param <F> the output type of the sixth choice
     * @return a {@code Generator<Choice6<A, B, C, D, E, F>>}
     */
    public static <A, B, C, D, E, F> Generator<Choice6<A, B, C, D, E, F>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                         Weighted<? extends Generator<? extends B>> b,
                                                                                         Weighted<? extends Generator<? extends C>> c,
                                                                                         Weighted<? extends Generator<? extends D>> d,
                                                                                         Weighted<? extends Generator<? extends E>> e,
                                                                                         Weighted<? extends Generator<? extends F>> f) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).toGenerator();
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice7}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param c   a weighted {@code Generator} for the third choice
     * @param d   a weighted {@code Generator} for the fourth choice
     * @param e   a weighted {@code Generator} for the fifth choice
     * @param f   a weighted {@code Generator} for the sixth choice
     * @param g   a weighted {@code Generator} for the seventh choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @param <C> the output type of the third choice
     * @param <D> the output type of the fourth choice
     * @param <E> the output type of the fifth choice
     * @param <F> the output type of the sixth choice
     * @param <G> the output type of the seventh choice
     * @return a {@code Generator<Choice7<A, B, C, D, E, F, G>>}
     */
    public static <A, B, C, D, E, F, G> Generator<Choice7<A, B, C, D, E, F, G>> generateChoice(Weighted<? extends Generator<? extends A>> a,
                                                                                               Weighted<? extends Generator<? extends B>> b,
                                                                                               Weighted<? extends Generator<? extends C>> c,
                                                                                               Weighted<? extends Generator<? extends D>> d,
                                                                                               Weighted<? extends Generator<? extends E>> e,
                                                                                               Weighted<? extends Generator<? extends F>> f,
                                                                                               Weighted<? extends Generator<? extends G>> g) {
        return choiceBuilder(a).or(b).or(c).or(d).or(e).or(f).or(g).toGenerator();
    }

    /**
     * Creates a {@link Generator} that yields {@link Choice8}s.
     *
     * @param a   a weighted {@code Generator} for the first choice
     * @param b   a weighted {@code Generator} for the second choice
     * @param c   a weighted {@code Generator} for the third choice
     * @param d   a weighted {@code Generator} for the fourth choice
     * @param e   a weighted {@code Generator} for the fifth choice
     * @param f   a weighted {@code Generator} for the sixth choice
     * @param g   a weighted {@code Generator} for the seventh choice
     * @param h   a weighted {@code Generator} for the eighth choice
     * @param <A> the output type of the first choice
     * @param <B> the output type of the second choice
     * @param <C> the output type of the third choice
     * @param <D> the output type of the fourth choice
     * @param <E> the output type of the fifth choice
     * @param <F> the output type of the sixth choice
     * @param <G> the output type of the seventh choice
     * @param <H> the output type of the eighth choice
     * @return a {@code Generator<Choice8<A, B, C, D, E, F, G, H>>}
     */
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

    /**
     * Creates a {@link Generator} that yields {@link Fn0}s.
     *
     * @param result a {@code Generator} for the result of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn0<R>>}
     */
    public static <R> Generator<Fn0<R>> generateFn0(Generator<R> result) {
        return Functions.generateFn0(result);
    }

    /**
     * Creates a {@link Generator} that yields {@link Fn1}s.  Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn1<A, R>>}
     */
    public static <A, R> Generator<Fn1<A, R>> generateFn1(Cogenerator<A> param1,
                                                          Generator<R> result) {
        return Functions.generateFn1(param1, result);
    }

    /**
     * Creates a {@link Generator} that yields pure {@link Fn2}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn2<A, B, R>>}
     */
    public static <A, B, R> Generator<Fn2<A, B, R>> generateFn2(Cogenerator<A> param1,
                                                                Cogenerator<B> param2,
                                                                Generator<R> result) {
        return Functions.generateFn2(param1, param2, result);
    }

    /**
     * Creates a {@link Generator} that yields pure {@link Fn3}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param param3 a {@code Cogenerator} for the third parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <C>    the type of the third parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn3<A, B, C, R>>}
     */
    public static <A, B, C, R> Generator<Fn3<A, B, C, R>> generateFn3(Cogenerator<A> param1,
                                                                      Cogenerator<B> param2,
                                                                      Cogenerator<C> param3,
                                                                      Generator<R> result) {
        return Functions.generateFn3(param1, param2, param3, result);
    }

    /**
     * Creates a {@link Generator} that yields pure {@link Fn4}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param param3 a {@code Cogenerator} for the third parameter of the function
     * @param param4 a {@code Cogenerator} for the fourth parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <C>    the type of the third parameter of the function
     * @param <D>    the type of the fourth parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn4<A, B, C, D, R>>}
     */
    public static <A, B, C, D, R> Generator<Fn4<A, B, C, D, R>> generateFn4(Cogenerator<A> param1,
                                                                            Cogenerator<B> param2,
                                                                            Cogenerator<C> param3,
                                                                            Cogenerator<D> param4,
                                                                            Generator<R> result) {
        return Functions.generateFn4(param1, param2, param3, param4, result);
    }

    /**
     * Creates a {@link Generator} that yields pure {@link Fn5}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param param3 a {@code Cogenerator} for the third parameter of the function
     * @param param4 a {@code Cogenerator} for the fourth parameter of the function
     * @param param5 a {@code Cogenerator} for the fifth parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <C>    the type of the third parameter of the function
     * @param <D>    the type of the fourth parameter of the function
     * @param <E>    the type of the fifth parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn5<A, B, C, D, E, R>>}
     */
    public static <A, B, C, D, E, R> Generator<Fn5<A, B, C, D, E, R>> generateFn5(Cogenerator<A> param1,
                                                                                  Cogenerator<B> param2,
                                                                                  Cogenerator<C> param3,
                                                                                  Cogenerator<D> param4,
                                                                                  Cogenerator<E> param5,
                                                                                  Generator<R> result) {
        return Functions.generateFn5(param1, param2, param3, param4, param5, result);
    }

    /**
     * Creates a {@link Generator} that yields pure {@link Fn6}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param param3 a {@code Cogenerator} for the third parameter of the function
     * @param param4 a {@code Cogenerator} for the fourth parameter of the function
     * @param param5 a {@code Cogenerator} for the fifth parameter of the function
     * @param param6 a {@code Cogenerator} for the sixth parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <C>    the type of the third parameter of the function
     * @param <D>    the type of the fourth parameter of the function
     * @param <E>    the type of the fifth parameter of the function
     * @param <F>    the type of the sixth parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn6<A, B, C, D, E, F, R>>}
     */
    public static <A, B, C, D, E, F, R> Generator<Fn6<A, B, C, D, E, F, R>> generateFn6(Cogenerator<A> param1,
                                                                                        Cogenerator<B> param2,
                                                                                        Cogenerator<C> param3,
                                                                                        Cogenerator<D> param4,
                                                                                        Cogenerator<E> param5,
                                                                                        Cogenerator<F> param6,
                                                                                        Generator<R> result) {
        return Functions.generateFn6(param1, param2, param3, param4, param5, param6, result);
    }

    /**
     * Creates a {@link Generator} that yields pure {@link Fn7}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param param3 a {@code Cogenerator} for the third parameter of the function
     * @param param4 a {@code Cogenerator} for the fourth parameter of the function
     * @param param5 a {@code Cogenerator} for the fifth parameter of the function
     * @param param6 a {@code Cogenerator} for the sixth parameter of the function
     * @param param7 a {@code Cogenerator} for the seventh parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <C>    the type of the third parameter of the function
     * @param <D>    the type of the fourth parameter of the function
     * @param <E>    the type of the fifth parameter of the function
     * @param <F>    the type of the sixth parameter of the function
     * @param <G>    the type of the seventh parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn7<A, B, C, D, E, F, G, R>>}
     */
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

    /**
     * Creates a {@link Generator} that yields pure {@link Fn8}s. Generated functions will be pure and total.
     *
     * @param param1 a {@link Cogenerator} for the first parameter of the function
     * @param param2 a {@code Cogenerator} for the second parameter of the function
     * @param param3 a {@code Cogenerator} for the third parameter of the function
     * @param param4 a {@code Cogenerator} for the fourth parameter of the function
     * @param param5 a {@code Cogenerator} for the fifth parameter of the function
     * @param param6 a {@code Cogenerator} for the sixth parameter of the function
     * @param param7 a {@code Cogenerator} for the seventh parameter of the function
     * @param param8 a {@code Cogenerator} for the eighth parameter of the function
     * @param result a {@code Generator} for the result of the function
     * @param <A>    the type of the first parameter of the function
     * @param <B>    the type of the second parameter of the function
     * @param <C>    the type of the third parameter of the function
     * @param <D>    the type of the fourth parameter of the function
     * @param <E>    the type of the fifth parameter of the function
     * @param <F>    the type of the sixth parameter of the function
     * @param <G>    the type of the seventh parameter of the function
     * @param <H>    the type of the eighth parameter of the function
     * @param <R>    the result type of the function
     * @return a {@code Generator<Fn8<A, B, C, D, E, F, G, H, R>>}
     */
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

    /**
     * Creates a {@link Generator} that yields version 4 {@link UUID}s.
     *
     * @return a {@code Generator<UUID>}
     */
    public static Generator<UUID> generateUUID() {
        return UUIDs.generateUUID();
    }

    /**
     * Creates a {@link Generator} that yields {@link BigInteger}s.
     *
     * @return a {@code Generator<BigInteger>}
     */
    public static Generator<BigInteger> generateBigInteger() {
        return BigNumbers.generateBigInteger();
    }

    /**
     * Creates a {@link Generator} that yields {@link BigInteger}s within a specific range.
     *
     * @param range the {@link BigIntegerRange} of the values to generate
     * @return a {@code Generator<BigInteger>}
     */
    public static Generator<BigInteger> generateBigInteger(BigIntegerRange range) {
        return BigNumbers.generateBigInteger(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimal}s.
     *
     * @return a {@code Generator<BigDecimal>}
     */
    public static Generator<BigDecimal> generateBigDecimal() {
        return BigNumbers.generateBigDecimal();
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimal}s within a specific range.
     *
     * @param range the {@link BigDecimalRange} of the values to generate
     * @return a {@code Generator<BigDecimal>}
     */
    public static Generator<BigDecimal> generateBigDecimal(BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimal}s within a specific range, and with the number of decimal places
     * determined by a specific {@code Generator}.
     *
     * @param generateDecimalPlaces a {@code Generator} to determine the number of decimal places of the generated values
     * @param range                 the {@link BigDecimalRange} of the values to generate
     * @return a {@code Generator<BigDecimal>}
     */
    public static Generator<BigDecimal> generateBigDecimal(Generator<Integer> generateDecimalPlaces, BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(generateDecimalPlaces, range);
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimal}s within a specific range, and with the number of decimal places
     * within a specific range.
     *
     * @param decimalPlacesRange the {@link IntRange} for the number of decimal places of the generated values
     * @param range              the {@link BigDecimalRange} of the values to generate
     * @return a {@code Generator<BigDecimal>}
     */
    static Generator<BigDecimal> generateBigDecimal(IntRange decimalPlacesRange, BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(decimalPlacesRange, range);
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimal}s within a specific range, and with a specific number of
     * decimal places.
     *
     * @param decimalPlaces the number of decimal places of the generated values
     * @return a {@code Generator<BigDecimal>}
     */
    public static Generator<BigDecimal> generateBigDecimal(int decimalPlaces, BigDecimalRange range) {
        return BigNumbers.generateBigDecimal(decimalPlaces, range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Month}s.
     *
     * @return a {@code Generator<Month>}
     */
    public static Generator<Month> generateMonth() {
        return Temporal.generateMonth();
    }

    /**
     * Creates a {@link Generator} that yields {@link DayOfWeek}s.
     *
     * @return a {@code Generator<DayOfWeek>}
     */
    public static Generator<DayOfWeek> generateDayOfWeek() {
        return Temporal.generateDayOfWeek();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDate}s.
     *
     * @return a {@code Generator<LocalDate>}
     */
    public static Generator<LocalDate> generateLocalDate() {
        return Temporal.generateLocalDate();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDate}s within a specific range.
     *
     * @param range the {@link LocalDateRange} of the values to generate
     * @return a {@code Generator<LocalDate>}
     */
    public static Generator<LocalDate> generateLocalDate(LocalDateRange range) {
        return Temporal.generateLocalDate(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDate}s within a specific {@link Year}.
     *
     * @param year the year of the values to generate
     * @return a {@code Generator<LocalDate>}
     */
    public static Generator<LocalDate> generateLocalDateForYear(Year year) {
        return Temporal.generateLocalDateForYear(year);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDate}s within a specific {@link YearMonth}.
     *
     * @param yearMonth the year and month of the values to generate
     * @return a {@code Generator<LocalDate>}
     */
    public static Generator<LocalDate> generateLocalDateForMonth(YearMonth yearMonth) {
        return Temporal.generateLocalDateForMonth(yearMonth);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalTime}s.
     *
     * @return a {@code Generator<LocalTime>}
     */
    public static Generator<LocalTime> generateLocalTime() {
        return Temporal.generateLocalTime();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalTime}s within a specific range.
     *
     * @param range the {@link LocalTimeRange} of the values to generate
     * @return a {@code Generator<LocalTime>}
     */
    public static Generator<LocalTime> generateLocalTime(LocalTimeRange range) {
        return Temporal.generateLocalTime(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateTime}s.
     *
     * @return a {@code Generator<LocalDateTime>}
     */
    public static Generator<LocalDateTime> generateLocalDateTime() {
        return Temporal.generateLocalDateTime();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateTime}s within a specific date range.
     *
     * @param dateRange the {@link LocalDateRange} of the values to generate
     * @return a {@code Generator<LocalDateTime>}
     */
    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateRange dateRange) {
        return Temporal.generateLocalDateTime(dateRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateTime}s within a specific range.
     *
     * @param range the {@link LocalDateTimeRange} of the values to generate
     * @return a {@code Generator<LocalDateTime>}
     */
    public static Generator<LocalDateTime> generateLocalDateTime(LocalDateTimeRange range) {
        return Temporal.generateLocalDateTime(range);
    }

    /**
     * Creates a {@link Generator} that yields {@link Duration}s.
     *
     * @return a {@code Generator<Duration>}
     */
    public static Generator<Duration> generateDuration() {
        return Temporal.generateDuration();
    }

    /**
     * Creates a {@link Generator} that yields {@link Duration}s with a specific range.
     *
     * @param range the {@link DurationRange} of the values to generate
     * @return a {@code Generator<Duration>}
     */
    public static Generator<Duration> generateDuration(DurationRange range) {
        return Temporal.generateDuration(range);
    }

    /**
     * Creates a {@link Generator} that builds its results by generating a random number of values (one or more) and combining
     * them using {@link Semigroup}.
     *
     * @param semigroup the {@code Semigroup} to apply.  May be called zero or more times.
     * @param gen       the {@code Generator} of the values to feed through the {@code Semigroup}
     * @param <A>       the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateFromSemigroup(Semigroup<A> semigroup, Generator<A> gen) {
        return Lambda.generateFromSemigroup(semigroup, gen);
    }

    /**
     * Creates a {@link Generator} that builds its results by generating a specific number of values and combining
     * them using {@link Semigroup}.
     *
     * @param semigroup the {@code Semigroup} to apply
     * @param gen       the {@code Generator} of the values to feed through the {@code Semigroup}
     * @param count     the number of elements to feed through the {@code Semigroup}; must be &gt;= 1
     * @param <A>       the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateNFromSemigroup(Semigroup<A> semigroup, Generator<A> gen, int count) {
        return Lambda.generateNFromSemigroup(semigroup, gen, count);
    }

    /**
     * Creates a {@link Generator} that builds its results by generating a random number of values (zero or more) and combining
     * them using {@link Monoid}.
     *
     * @param monoid the {@code Monoid} to apply.  May be called zero or more times.
     * @param gen    the {@code Generator} of the values to feed through the {@code Monoid}
     * @param <A>    the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateFromMonoid(Monoid<A> monoid, Generator<A> gen) {
        return Lambda.generateFromMonoid(monoid, gen);
    }

    /**
     * Creates a {@link Generator} that builds its results by generating a specific number of values and combining
     * them using {@link Monoid}.
     *
     * @param monoid the {@code Monoid} to apply
     * @param gen    the {@code Generator} of the values to feed through the {@code Monoid}
     * @param count  the number of elements to feed through the {@code Monoid}; must be &gt;= 0
     * @param <A>    the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> generateNFromMonoid(Monoid<A> monoid, Generator<A> gen, int count) {
        return Lambda.generateNFromMonoid(monoid, gen, count);
    }

    /**
     * Creates a {@link Generator} that yields ordered pairs of some {@link Comparable} type.  The second element
     * of the pair will be greater than or equal to the first element.
     *
     * @param elements the generator for elements
     * @param <A>      the element type - must be {@code Comparable}
     * @return a {@code Generator<Tuple2<A, A>>}
     */
    public static <A extends Comparable<A>> Generator<Tuple2<A, A>> generateOrderedPair(Generator<A> elements) {
        return OrderedTuples.generateOrderedPair(elements);
    }

    /**
     * Creates a {@link Generator} that yields ordered sequences.
     *
     * @param countForEachElement a {@code Generator} that determines the count (zero or more) for each element.
     *                            For each element in {@code orderedElems}, this generator is invoked to yield a number,
     *                            and the element is repeated in the output that number of times before moving on to the
     *                            next element.
     * @param orderedElems        the ordered sequence of candidates
     * @param <A>                 the element type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> generateOrderedSequence(Generator<Integer> countForEachElement,
                                                                            ImmutableVector<A> orderedElems) {
        return Sequences.generateOrderedSequence(countForEachElement, orderedElems);
    }

    /**
     * Creates a {@link Generator} that yields ordered sequences.
     *
     * @param countForEachElementRange the {@link IntRange} of the count of each element in the output.
     *                                 For each element in {@code orderedElems}, a random number in the range is selected,
     *                                 and the element is repeated in the output that number of times before moving on to the
     *                                 next element.
     * @param orderedElems             the ordered sequence of candidates
     * @param <A>                      the element type
     * @return a {@code Generator<ImmutableVector<A>>}
     */
    public static <A> Generator<ImmutableVector<A>> generateOrderedSequence(IntRange countForEachElementRange,
                                                                            ImmutableVector<A> orderedElems) {
        return Sequences.generateOrderedSequence(countForEachElementRange, orderedElems);
    }

    /**
     * Creates a {@link Generator} that yields {@link IntRange}s.
     *
     * @return a {@code Generator<IntRange>}
     */
    public static Generator<IntRange> generateIntRange() {
        return Ranges.generateIntRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link IntRange}s, within a specific parent range.
     *
     * @param parentRange the {@code IntRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<IntRange>}
     */
    public static Generator<IntRange> generateIntRange(IntRange parentRange) {
        return Ranges.generateIntRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link LongRange}s.
     *
     * @return a {@code Generator<LongRange>}
     */
    public static Generator<LongRange> generateLongRange() {
        return Ranges.generateLongRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link LongRange}s, within a specific parent range.
     *
     * @param parentRange the {@code LongRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<LongRange>}
     */
    public static Generator<LongRange> generateLongRange(LongRange parentRange) {
        return Ranges.generateLongRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link ShortRange}s.
     *
     * @return a {@code Generator<ShortRange>}
     */
    public static Generator<ShortRange> generateShortRange() {
        return Ranges.generateShortRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link ShortRange}s, within a specific parent range.
     *
     * @param parentRange the {@code ShortRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<ShortRange>}
     */
    public static Generator<ShortRange> generateShortRange(ShortRange parentRange) {
        return Ranges.generateShortRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link ByteRange}s.
     *
     * @return a {@code Generator<ByteRange>}
     */
    public static Generator<ByteRange> generateByteRange() {
        return Ranges.generateByteRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link ByteRange}s, within a specific parent range.
     *
     * @param parentRange the {@code ByteRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<ByteRange>}
     */
    public static Generator<ByteRange> generateByteRange(ByteRange parentRange) {
        return Ranges.generateByteRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link DoubleRange}s.
     *
     * @return a {@code Generator<DoubleRange>}
     */
    public static Generator<DoubleRange> generateDoubleRange() {
        return Ranges.generateDoubleRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link DoubleRange}s, within a specific parent range.
     *
     * @param parentRange the {@code DoubleRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<DoubleRange>}
     */
    public static Generator<DoubleRange> generateDoubleRange(DoubleRange parentRange) {
        return Ranges.generateDoubleRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link FloatRange}s.
     *
     * @return a {@code Generator<FloatRange>}
     */
    public static Generator<FloatRange> generateFloatRange() {
        return Ranges.generateFloatRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link FloatRange}s, within a specific parent range.
     *
     * @param parentRange the {@code FloatRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<FloatRange>}
     */
    public static Generator<FloatRange> generateFloatRange(FloatRange parentRange) {
        return Ranges.generateFloatRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link BigIntegerRange}s.
     *
     * @return a {@code Generator<BigIntegerRange>}
     */
    public static Generator<BigIntegerRange> generateBigIntegerRange() {
        return Ranges.generateBigIntegerRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link BigIntegerRange}s, within a specific parent range.
     *
     * @param parentRange the {@code BigIntegerRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<BigIntegerRange>}
     */
    public static Generator<BigIntegerRange> generateBigIntegerRange(BigIntegerRange parentRange) {
        return Ranges.generateBigIntegerRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimalRange}s.
     *
     * @return a {@code Generator<BigDecimalRange>}
     */
    public static Generator<BigDecimalRange> generateBigDecimalRange() {
        return Ranges.generateBigDecimalRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link BigDecimalRange}s, within a specific parent range.
     *
     * @param parentRange the {@code BigDecimalRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<BigDecimalRange>}
     */
    public static Generator<BigDecimalRange> generateBigDecimalRange(BigDecimalRange parentRange) {
        return Ranges.generateBigDecimalRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateRange}s.
     *
     * @return a {@code Generator<LocalDateRange>}
     */
    public static Generator<LocalDateRange> generateLocalDateRange() {
        return Ranges.generateLocalDateRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateRange}s, within a specific parent range.
     *
     * @param parentRange the {@code LocalDateRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<LocalDateRange>}
     */
    public static Generator<LocalDateRange> generateLocalDateRange(LocalDateRange parentRange) {
        return Ranges.generateLocalDateRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalTimeRange}s.
     *
     * @return a {@code Generator<LocalTimeRange>}
     */
    public static Generator<LocalTimeRange> generateLocalTimeRange() {
        return Ranges.generateLocalTimeRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalTimeRange}s, within a specific parent range.
     *
     * @param parentRange the {@code LocalTimeRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<LocalTimeRange>}
     */
    public static Generator<LocalTimeRange> generateLocalTimeRange(LocalTimeRange parentRange) {
        return Ranges.generateLocalTimeRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateTimeRange}s.
     *
     * @return a {@code Generator<LocalDateTimeRange>}
     */
    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange() {
        return Ranges.generateLocalDateTimeRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateTimeRange}s, within a specific parent range.
     *
     * @param parentRange the {@code LocalDateRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<LocalDateTimeRange>}
     */
    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateRange parentRange) {
        return Ranges.generateLocalDateTimeRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link LocalDateTimeRange}s, within a specific parent range.
     *
     * @param parentRange the {@code LocalDateTimeRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<LocalDateTimeRange>}
     */
    public static Generator<LocalDateTimeRange> generateLocalDateTimeRange(LocalDateTimeRange parentRange) {
        return Ranges.generateLocalDateTimeRange(parentRange);
    }

    /**
     * Creates a {@link Generator} that yields {@link DurationRange}s.
     *
     * @return a {@code Generator<DurationRange>}
     */
    public static Generator<DurationRange> generateDurationRange() {
        return Ranges.generateDurationRange();
    }

    /**
     * Creates a {@link Generator} that yields {@link DurationRange}s, within a specific parent range.
     *
     * @param parentRange the {@code DurationRange} to restrict the output.
     *                    The output ranges will either be equal to or fully subsumed by this range.
     * @return a {@code Generator<DurationRange>}
     */
    public static Generator<DurationRange> generateDurationRange(DurationRange parentRange) {
        return Ranges.generateDurationRange(parentRange);
    }
}
