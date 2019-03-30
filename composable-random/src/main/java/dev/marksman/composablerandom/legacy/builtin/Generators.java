package dev.marksman.composablerandom.legacy.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.*;
import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.legacy.OldFrequencyEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Generators {

    public static OldGenerator<Boolean> generateBoolean() {
        return Primitives.generateBoolean();
    }

    public static OldGenerator<Boolean> generateBoolean(int trueWeight, int falseWeight) {
        return Primitives.generateBoolean(trueWeight, falseWeight);
    }

    public static OldGenerator<Double> generateDouble() {
        return Primitives.generateDouble();
    }

    public static OldGenerator<Integer> generateInt() {
        return Primitives.generateInt();
    }

    public static OldGenerator<Integer> generateInt(int min, int max) {
        return Primitives.generateInt(min, max);
    }

    public static OldGenerator<Integer> generateIntExclusive(int bound) {
        return Primitives.generateIntExclusive(bound);
    }

    public static OldGenerator<Integer> generateIntExclusive(int origin, int bound) {
        return Primitives.generateIntExclusive(origin, bound);
    }

    public static OldGenerator<Float> generateFloat() {
        return Primitives.generateFloat();
    }

    public static OldGenerator<Long> generateLong() {
        return Primitives.generateLong();
    }

    public static OldGenerator<Long> generateLong(long min, long max) {
        return Primitives.generateLong(min, max);
    }

    public static OldGenerator<Long> generateLongExclusive(long bound) {
        return Primitives.generateLongExclusive(bound);
    }

    public static OldGenerator<Long> generateLongExclusive(long origin, long bound) {
        return Primitives.generateLongExclusive(origin, bound);
    }

    public static OldGenerator<Double> generateGaussian() {
        return Primitives.generateGaussian();
    }

    public static OldGenerator<Byte[]> generateBytes(int count) {
        return Primitives.generateBytes(count);
    }

    public static OldGenerator<Byte> generateByte() {
        return Primitives.generateByte();
    }

    public static OldGenerator<Short> generateShort() {
        return Primitives.generateShort();
    }

    public static OldGenerator<Unit> generateUnit() {
        return CoProducts.generateUnit();
    }

    public static OldGenerator<String> generateString(int length, OldGenerator<String> g) {
        return Strings.generateString(length, g);
    }

    public static OldGenerator<String> generateStringFromCharacters(int length, OldGenerator<Character> g) {
        return Strings.generateStringFromCharacters(length, g);
    }

    public static OldGenerator<String> generateStringFromCharacters(OldGenerator<Character> g) {
        return Strings.generateStringFromCharacters(g);
    }

    @SafeVarargs
    public static OldGenerator<String> generateString(OldGenerator<String> first, OldGenerator<String>... more) {
        return Strings.generateString(first, more);
    }

    public static <A> OldGenerator<Maybe<A>> generateMaybe(int nothingWeight, int justWeight, OldGenerator<A> g) {
        return CoProducts.generateMaybe(nothingWeight, justWeight, g);
    }

    public static <A> OldGenerator<Maybe<A>> generateMaybe(int justWeight, OldGenerator<A> g) {
        return CoProducts.generateMaybe(justWeight, g);
    }

    public static <A> OldGenerator<Maybe<A>> generateMaybe(OldGenerator<A> g) {
        return CoProducts.generateMaybe(g);
    }

    public static <A> OldGenerator<Maybe<A>> generateJust(OldGenerator<A> g) {
        return CoProducts.generateJust(g);
    }

    public static <A> OldGenerator<Maybe<A>> generateNothing() {
        return CoProducts.generateNothing();
    }

    public static <L, R> OldGenerator<Either<L, R>> generateEither(int leftWeight, int rightWeight, OldGenerator<L> leftGenerator, OldGenerator<R> rightGenerator) {
        return CoProducts.generateEither(leftWeight, rightWeight, leftGenerator, rightGenerator);
    }

    public static <L, R> OldGenerator<Either<L, R>> generateEither(int rightWeight, OldGenerator<L> leftGenerator, OldGenerator<R> rightGenerator) {
        return CoProducts.generateEither(rightWeight, leftGenerator, rightGenerator);
    }

    public static <L, R> OldGenerator<Either<L, R>> generateEither(OldGenerator<L> leftGenerator, OldGenerator<R> rightGenerator) {
        return CoProducts.generateEither(leftGenerator, rightGenerator);
    }

    public static <L, R> OldGenerator<Either<L, R>> generateLeft(OldGenerator<L> g) {
        return CoProducts.generateLeft(g);
    }

    public static <L, R> OldGenerator<Either<L, R>> generateRight(OldGenerator<R> g) {
        return CoProducts.generateRight(g);
    }

    public static <A> OldGenerator<Tuple2<A, A>> pair(OldGenerator<A> g) {
        return tupled(g, g);
    }

    public static <A> OldGenerator<Tuple3<A, A, A>> triple(OldGenerator<A> g) {
        return tupled(g, g, g);
    }

    public static <A, B> OldGenerator<Tuple2<A, B>> tupled(OldGenerator<A> ga, OldGenerator<B> gb) {
        return Tuples.tupled(ga, gb);
    }

    public static <A, B, C> OldGenerator<Tuple3<A, B, C>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc) {
        return Tuples.tupled(ga, gb, gc);
    }

    public static <A, B, C, D> OldGenerator<Tuple4<A, B, C, D>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc, OldGenerator<D> gd) {
        return Tuples.tupled(ga, gb, gc, gd);
    }

    public static <A, B, C, D, E> OldGenerator<Tuple5<A, B, C, D, E>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                             OldGenerator<D> gd, OldGenerator<E> ge) {
        return Tuples.tupled(ga, gb, gc, gd, ge);
    }

    public static <A, B, C, D, E, F> OldGenerator<Tuple6<A, B, C, D, E, F>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                                   OldGenerator<D> gd, OldGenerator<E> ge, OldGenerator<F> gf) {
        return Tuples.tupled(ga, gb, gc, gd, ge, gf);
    }

    public static <A, B, C, D, E, F, G> OldGenerator<Tuple7<A, B, C, D, E, F, G>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                                         OldGenerator<D> gd, OldGenerator<E> ge, OldGenerator<F> gf,
                                                                                         OldGenerator<G> gg) {
        return Tuples.tupled(ga, gb, gc, gd, ge, gf, gg);
    }

    public static <A, B, C, D, E, F, G, H> OldGenerator<Tuple8<A, B, C, D, E, F, G, H>> tupled(OldGenerator<A> ga, OldGenerator<B> gb, OldGenerator<C> gc,
                                                                                               OldGenerator<D> gd, OldGenerator<E> ge, OldGenerator<F> gf,
                                                                                               OldGenerator<G> gg, OldGenerator<H> rh) {
        return Tuples.tupled(ga, gb, gc, gd, ge, gf, gg, rh);
    }

    @SafeVarargs
    public static <A> OldGenerator<A> chooseOneOf(A first, A... more) {
        return Choose.chooseOneOf(first, more);
    }

    @SafeVarargs
    public static <A> OldGenerator<A> chooseOneOf(OldGenerator<? extends A> first, OldGenerator<? extends A>... more) {
        return Choose.chooseOneOf(first, more);
    }

    @SafeVarargs
    public static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneOf(A first, A... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneOf(OldGenerator<? extends A> first, OldGenerator<? extends A>... more) {
        return Choose.chooseAtLeastOneOf(first, more);
    }

    @SafeVarargs
    public static <A> OldGenerator<ArrayList<A>> chooseSomeOf(A first, A... more) {
        return Choose.chooseSomeOf(first, more);
    }

    @SafeVarargs
    public static <A> OldGenerator<ArrayList<A>> chooseSomeOf(OldGenerator<? extends A> first, OldGenerator<? extends A>... more) {
        return Choose.chooseSomeOf(first, more);
    }

    public static <A> OldGenerator<A> chooseOneFrom(Collection<A> items) {
        return Choose.chooseOneFrom(items);
    }

    public static <A> OldGenerator<A> chooseOneFrom(DiscreteDomain<A> domain) {
        return Choose.chooseOneFrom(domain);
    }

    public static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneFrom(Collection<A> items) {
        return Choose.chooseAtLeastOneFrom(items);
    }

    public static <A> OldGenerator<ArrayList<A>> chooseAtLeastOneFrom(DiscreteDomain<A> domain) {
        return Choose.chooseAtLeastOneFrom(domain);
    }

    public static <A> OldGenerator<ArrayList<A>> chooseSomeFrom(Collection<A> items) {
        return Choose.chooseSomeFrom(items);
    }

    public static <A> OldGenerator<ArrayList<A>> chooseSomeFrom(DiscreteDomain<A> domain) {
        return Choose.chooseSomeFrom(domain);
    }

    public static <K, V> OldGenerator<Map.Entry<K, V>> chooseEntryFrom(Map<K, V> map) {
        return Choose.chooseEntryFrom(map);
    }

    public static <K, V> OldGenerator<K> chooseKeyFrom(Map<K, V> map) {
        return Choose.chooseKeyFrom(map);
    }

    public static <K, V> OldGenerator<V> chooseValueFrom(Map<K, V> map) {
        return Choose.chooseValueFrom(map);
    }

    @SafeVarargs
    public static <A> OldGenerator<A> frequency(OldFrequencyEntry<? extends A> first, OldFrequencyEntry<? extends A>... more) {
        return Choose.frequency(first, more);
    }

    public static <A> OldGenerator<A> frequency(Collection<OldFrequencyEntry<? extends A>> entries) {
        return Choose.frequency(entries);
    }

    public static <A> OldGenerator<A> sized(Function<Integer, OldGenerator<A>> g) {
        return Sized.sized(g);
    }

    public static <A> OldGenerator<ArrayList<A>> generateList(OldGenerator<A> g) {
        return Collections.generateList(g);
    }

    public static <A> OldGenerator<ArrayList<A>> generateNonEmptyList(OldGenerator<A> g) {
        return Collections.generateNonEmptyList(g);
    }

    public static <A> OldGenerator<ArrayList<A>> generateListOfN(int n, OldGenerator<A> g) {
        return Collections.generateListOfN(n, g);
    }

    public static <A> OldGenerator<Set<A>> generateSet(OldGenerator<A> g) {
        return Collections.generateSet(g);
    }

    public static <A> OldGenerator<Set<A>> generateNonEmptySet(OldGenerator<A> g) {
        return Collections.generateNonEmptySet(g);
    }

    public static <K, V> OldGenerator<Map<K, V>> generateMap(OldGenerator<K> keyGenerator,
                                                             OldGenerator<V> valueGenerator) {
        return Collections.generateMap(keyGenerator, valueGenerator);
    }

    public static <K, V> OldGenerator<Map<K, V>> generateMap(Collection<K> keys,
                                                             OldGenerator<V> valueGenerator) {
        return Collections.generateMap(keys, valueGenerator);
    }

    public static <K, V> OldGenerator<Map<K, V>> generateMap(DiscreteDomain<K> keys,
                                                             OldGenerator<V> valueGenerator) {
        return Collections.generateMap(keys, valueGenerator);
    }

    public static <K, V> OldGenerator<Map<K, V>> generateNonEmptyMap(OldGenerator<K> keyGenerator,
                                                                     OldGenerator<V> valueGenerator) {
        return Collections.generateNonEmptyMap(keyGenerator, valueGenerator);
    }

}
