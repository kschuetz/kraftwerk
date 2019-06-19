package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import dev.marksman.collectionviews.*;
import dev.marksman.composablerandom.frequency.FrequencyMap;
import dev.marksman.composablerandom.frequency.FrequencyMapBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static dev.marksman.composablerandom.Generate.buildVector;
import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.ReservoirSample.reservoirSample;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;
import static dev.marksman.composablerandom.frequency.FrequencyMapBuilder.frequencyMapBuilder;
import static java.util.Arrays.asList;

class Choose {

    @SafeVarargs
    static <A> Generate<A> chooseOneOfValues(A first, A... more) {
        return chooseOneFromDomain(NonEmptyVector.of(first, more));
    }

    @SafeVarargs
    static <A> Generate<A> chooseOneOf(Generate<A> first, Generate<? extends A>... more) {
        return foldLeft(FrequencyMap::add, frequencyMap(first), asList(more)).toGenerate();
    }

    @SafeVarargs
    static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return chooseAtLeastOneFromDomain(Vector.of(first, more));
    }

    @SafeVarargs
    static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneOf(Generate<? extends A> first, Generate<? extends A>... more) {
        return chooseSomeFromGenerators(1, NonEmptyVector.of(first, more))
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    @SafeVarargs
    static <A> Generate<ImmutableVector<A>> chooseSomeOf(A first, A... more) {
        return chooseSomeFromDomain(Vector.of(first, more));
    }

    @SafeVarargs
    static <A> Generate<ImmutableVector<A>> chooseSomeOf(Generate<? extends A> first, Generate<? extends A>... more) {
        return chooseSomeFromGenerators(0, NonEmptyVector.of(first, more));
    }

    static <A> Generate<A> chooseOneFromCollection(Collection<A> items) {
        requireNonEmptyChoices("chooseOneFrom", items);
        return chooseOneFromDomain(NonEmptyVector.copyFromOrThrow(items));
    }

    static <A> Generate<A> chooseOneFromDomain(NonEmptyVector<A> domain) {
        int size = domain.size();
        if (size == 1) {
            return constant(domain.unsafeGet(0));
        } else {
            return Generate.generateIntIndex(size).fmap(domain::unsafeGet);
        }
    }

    static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromCollection(Collection<A> items) {
        requireNonEmptyChoices("chooseAtLeastOneFrom", items);
        return chooseAtLeastOneFromDomain(NonEmptyVector.copyFromOrThrow(items));
    }

    static <A> Generate<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromDomain(NonEmptyVector<A> domain) {
        return chooseSomeFromValues(1, domain).fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    static <A> Generate<ImmutableVector<A>> chooseSomeFromDomain(Collection<A> items) {
        requireNonEmptyChoices("chooseSomeFrom", items);
        return chooseSomeFromDomain(NonEmptyVector.copyFromOrThrow(items));
    }

    static <A> Generate<ImmutableVector<A>> chooseSomeFromDomain(NonEmptyVector<A> domain) {
        return chooseSomeFromValues(0, domain);
    }

    static <K, V> Generate<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        requireNonEmptyChoices("chooseEntryFrom", entries);
        return chooseOneFromCollection(entries);
    }

    static <K, V> Generate<K> chooseKeyFromMap(Map<K, V> map) {
        Set<K> keys = map.keySet();
        requireNonEmptyChoices("chooseKeyFrom", keys);
        return chooseOneFromCollection(keys);
    }

    static <K, V> Generate<V> chooseValueFromMap(Map<K, V> map) {
        Collection<V> values = map.values();
        requireNonEmptyChoices("chooseValueFrom", values);
        return chooseOneFromCollection(values);
    }

    static <A> Generate<A> frequency(FrequencyMap<A> frequencyMap) {
        return frequencyMap.toGenerate();
    }

    @SafeVarargs
    static <A> Generate<A> frequency(FrequencyEntry<A> first, FrequencyEntry<A>... more) {
        return frequencyImpl(cons(first, asList(more)));
    }

    static <A> Generate<A> frequency(Collection<FrequencyEntry<A>> entries) {
        return frequencyImpl(entries);
    }

    private static <A> Generate<A> frequencyImpl(Iterable<FrequencyEntry<A>> entries) {
        return FoldLeft.<FrequencyEntry<A>, FrequencyMapBuilder<A>>foldLeft(
                FrequencyMapBuilder::add,
                frequencyMapBuilder(), entries)
                .build()
                .toGenerate();
    }

    private static <A> void requireNonEmptyChoices(String methodName, Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException(methodName + " requires at least one choice");
        }
    }

    private static <A> Generate<ImmutableVector<A>> chooseSomeFromValues(int min, NonEmptyVector<A> domain) {
        return Generate.sized(k -> reservoirSample(domain.size(), Math.max(k, min))
                .fmap(indices -> {
                    VectorBuilder<A> builder = Vector.builder(k);
                    for (Integer idx : indices) {
                        builder = builder.add(domain.unsafeGet(idx));
                    }
                    return builder.build();
                }));
    }

    @SuppressWarnings("unchecked")
    private static <A> Generate<ImmutableVector<A>> chooseSomeFromGenerators(int min, NonEmptyVector<Generate<? extends A>> domain) {
        return Generate.sized(k -> reservoirSample(domain.size(), Math.max(k, min))
                .flatMap(indices -> {
                    ArrayList<Generate<A>> generators = new ArrayList<>();
                    for (Integer idx : indices) {
                        generators.add((Generate<A>) domain.unsafeGet(idx));
                    }
                    return buildVector(generators);
                }));
    }

}
