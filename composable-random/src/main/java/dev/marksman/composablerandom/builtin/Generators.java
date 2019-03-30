package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.FrequencyEntry;
import dev.marksman.composablerandom.Generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

    @SafeVarargs
    public static <A> Generator<A> frequency(FrequencyEntry<? extends A> first, FrequencyEntry<? extends A>... more) {
        return Choose.frequency(first, more);
    }

    public static <A> Generator<A> frequency(Collection<FrequencyEntry<? extends A>> entries) {
        return Choose.frequency(entries);
    }
}
