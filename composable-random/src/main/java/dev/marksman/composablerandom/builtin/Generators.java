package dev.marksman.composablerandom.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Instruction;
import dev.marksman.composablerandom.frequency.FrequencyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class Generators {

    public static Generator<Boolean> generateBoolean() {
        return Primitives.generateBoolean();
    }

    public static Generator<Double> generateDouble() {
        return Primitives.generateDouble();
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

    public static Generator<Float> generateFloat() {
        return Primitives.generateFloat();
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

    public static Generator<Double> generateGaussian() {
        return Primitives.generateGaussian();
    }

    public static Generator<Byte[]> generateBytes(int count) {
        return Primitives.generateBytes(count);
    }

    public static Generator<Byte> generateByte() {
        return Primitives.generateByte();
    }

    public static Generator<Short> generateShort() {
        return Primitives.generateShort();
    }

    public static Generator<String> generateString(int length, Generator<String> g) {
        return Strings.generateString(length, g);
    }

    public static Generator<String> generateStringFromCharacters(int length, Generator<Character> g) {
        return Strings.generateStringFromCharacters(length, g);
    }

    public static Generator<String> generateStringFromCharacters(Generator<Character> g) {
        return Strings.generateStringFromCharacters(g);
    }

    @SafeVarargs
    public static Generator<String> generateString(Generator<String> first, Generator<String>... more) {
        return Strings.generateString(first, more);
    }

    public static Generator<Unit> generateUnit() {
        return CoProducts.generateUnit();
    }

    public static <A> Generator<Maybe<A>> generateMaybe(int nothingWeight, int justWeight, Generator<A> g) {
        return CoProducts.generateMaybe(nothingWeight, justWeight, g);
    }

    public static <A> Generator<Maybe<A>> generateMaybe(int justWeight, Generator<A> g) {
        return CoProducts.generateMaybe(justWeight, g);
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

    public static <L, R> Generator<Either<L, R>> generateEither(int leftWeight, int rightWeight, Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return CoProducts.generateEither(leftWeight, rightWeight, leftGenerator, rightGenerator);
    }

    public static <L, R> Generator<Either<L, R>> generateEither(int rightWeight, Generator<L> leftGenerator, Generator<R> rightGenerator) {
        return CoProducts.generateEither(rightWeight, leftGenerator, rightGenerator);
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

    public static <A> Generator<Tuple2<A, A>> pair(Generator<A> g) {
        return tupled(g, g);
    }

    public static <A> Generator<Tuple3<A, A, A>> triple(Generator<A> g) {
        return tupled(g, g, g);
    }

    public static <A, B> Generator<Tuple2<A, B>> tupled(Generator<A> ga, Generator<B> gb) {
        return Tuples.tupled(ga, gb);
    }

    public static <A, B, C> Generator<Tuple3<A, B, C>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc) {
        return Tuples.tupled(ga, gb, gc);
    }

    public static <A, B, C, D> Generator<Tuple4<A, B, C, D>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc, Generator<D> gd) {
        return Tuples.tupled(ga, gb, gc, gd);
    }

    public static <A, B, C, D, E> Generator<Tuple5<A, B, C, D, E>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                          Generator<D> gd, Generator<E> ge) {
        return Tuples.tupled(ga, gb, gc, gd, ge);
    }

    public static <A, B, C, D, E, F> Generator<Tuple6<A, B, C, D, E, F>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                                Generator<D> gd, Generator<E> ge, Generator<F> gf) {
        return Tuples.tupled(ga, gb, gc, gd, ge, gf);
    }

    public static <A, B, C, D, E, F, G> Generator<Tuple7<A, B, C, D, E, F, G>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                                      Generator<D> gd, Generator<E> ge, Generator<F> gf,
                                                                                      Generator<G> gg) {
        return Tuples.tupled(ga, gb, gc, gd, ge, gf, gg);
    }

    public static <A, B, C, D, E, F, G, H> Generator<Tuple8<A, B, C, D, E, F, G, H>> tupled(Generator<A> ga, Generator<B> gb, Generator<C> gc,
                                                                                            Generator<D> gd, Generator<E> ge, Generator<F> gf,
                                                                                            Generator<G> gg, Generator<H> rh) {
        return Tuples.tupled(ga, gb, gc, gd, ge, gf, gg, rh);
    }

    @SafeVarargs
    public static <A> Generator<A> chooseOneOf(A first, A... more) {
        return Choose.chooseOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<A> chooseOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(A first, A... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseSomeOf(A first, A... more) {
        return Choose.chooseSomeOf(first, more);
    }

    @SafeVarargs
    public static <A> Generator<ArrayList<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return Choose.chooseSomeOf(first, more);
    }

    public static <A> Generator<A> chooseOneFrom(Collection<A> items) {
        return Choose.chooseOneFrom(items);
    }

    public static <A> Generator<A> chooseOneFrom(DiscreteDomain<A> domain) {
        return Choose.chooseOneFrom(domain);
    }

    public static <A> Generator<ArrayList<A>> chooseAtLeastOneFrom(Collection<A> items) {
        return Choose.chooseAtLeastOneFrom(items);
    }

    public static <A> Generator<ArrayList<A>> chooseAtLeastOneFrom(DiscreteDomain<A> domain) {
        return Choose.chooseAtLeastOneFrom(domain);
    }

    public static <A> Generator<ArrayList<A>> chooseSomeFrom(Collection<A> items) {
        return Choose.chooseSomeFrom(items);
    }

    public static <A> Generator<ArrayList<A>> chooseSomeFrom(DiscreteDomain<A> domain) {
        return Choose.chooseSomeFrom(domain);
    }

    public static <K, V> Generator<Map.Entry<K, V>> chooseEntryFrom(Map<K, V> map) {
        return Choose.chooseEntryFrom(map);
    }

    public static <K, V> Generator<K> chooseKeyFrom(Map<K, V> map) {
        return Choose.chooseKeyFrom(map);
    }

    public static <K, V> Generator<V> chooseValueFrom(Map<K, V> map) {
        return Choose.chooseValueFrom(map);
    }

    public static <A> Generator<A> frequency(FrequencyMap<A> frequencyMap) {
        return Choose.frequency(frequencyMap);
    }

    @SafeVarargs
    public static <A> Generator<A> frequency(FrequencyEntry<? extends A> first, FrequencyEntry<? extends A>... more) {
        return Choose.frequency(first, more);
    }

    public static <A> Generator<A> frequency(Collection<FrequencyEntry<? extends A>> entries) {
        return Choose.frequency(entries);
    }

    public static <A> Generator<A> sized(Function<Integer, Generator<A>> g) {
        return Generator.generator(Instruction.sized(n -> g.apply(n).getInstruction()));
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

    public static <K, V> Generator<Map<K, V>> generateMap(Generator<K> keyGenerator,
                                                          Generator<V> valueGenerator) {
        return Collections.generateMap(keyGenerator, valueGenerator);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                          Generator<V> valueGenerator) {
        return Collections.generateMap(keys, valueGenerator);
    }

    public static <K, V> Generator<Map<K, V>> generateMap(DiscreteDomain<K> keys,
                                                          Generator<V> valueGenerator) {
        return Collections.generateMap(keys, valueGenerator);
    }

    public static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> keyGenerator,
                                                                  Generator<V> valueGenerator) {
        return Collections.generateNonEmptyMap(keyGenerator, valueGenerator);
    }
}
