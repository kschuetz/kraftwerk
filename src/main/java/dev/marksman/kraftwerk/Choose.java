package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.frequency.FrequencyMap;
import dev.marksman.kraftwerk.frequency.FrequencyMapBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static dev.marksman.kraftwerk.Generators.buildVector;
import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.ReservoirSample.reservoirSample;
import static dev.marksman.kraftwerk.frequency.FrequencyMapBuilder.frequencyMapBuilder;
import static java.util.Arrays.asList;

class Choose {

    @SafeVarargs
    static <A> Generator<A> chooseOneOfValues(A first, A... more) {
        return chooseOneFromDomain(NonEmptyVector.of(first, more));
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return foldLeft(FrequencyMap::add, FrequencyMap.<A>frequencyMap(first), asList(more)).toGenerator();
    }

    @SafeVarargs
    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return chooseAtLeastOneFromDomain(Vector.of(first, more));
    }

    @SafeVarargs
    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return chooseSomeFromGenerators(1, NonEmptyVector.of(first, more))
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    @SafeVarargs
    static <A> Generator<ImmutableVector<A>> chooseSomeOf(A first, A... more) {
        return chooseSomeFromDomain(Vector.of(first, more));
    }

    @SafeVarargs
    static <A> Generator<ImmutableVector<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return chooseSomeFromGenerators(0, NonEmptyVector.of(first, more));
    }

    static <A> Generator<A> chooseOneFromCollection(Collection<A> items) {
        requireNonEmptyChoices("chooseOneFrom", items);
        return chooseOneFromDomain(NonEmptyVector.copyFromOrThrow(items));
    }

    static <A> Generator<A> chooseOneFromDomain(NonEmptyVector<A> domain) {
        int size = domain.size();
        if (size == 1) {
            return constant(domain.unsafeGet(0));
        } else {
            return Generators.generateIntIndex(size).fmap(domain::unsafeGet);
        }
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromCollection(Collection<A> items) {
        requireNonEmptyChoices("chooseAtLeastOneFrom", items);
        return chooseAtLeastOneFromDomain(NonEmptyVector.copyFromOrThrow(items));
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneFromDomain(NonEmptyVector<A> domain) {
        return chooseSomeFromValues(1, domain).fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    static <A> Generator<ImmutableVector<A>> chooseSomeFromDomain(Collection<A> items) {
        if (items.isEmpty()) {
            return constant(Vector.empty());
        } else {
            return chooseSomeFromDomain(NonEmptyVector.copyFromOrThrow(items));
        }
    }

    static <A> Generator<ImmutableVector<A>> chooseSomeFromDomain(NonEmptyVector<A> domain) {
        return chooseSomeFromValues(0, domain);
    }

    static <K, V> Generator<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        requireNonEmptyChoices("chooseEntryFrom", entries);
        return chooseOneFromCollection(entries);
    }

    static <K, V> Generator<K> chooseKeyFromMap(Map<K, V> map) {
        Set<K> keys = map.keySet();
        requireNonEmptyChoices("chooseKeyFrom", keys);
        return chooseOneFromCollection(keys);
    }

    static <K, V> Generator<V> chooseValueFromMap(Map<K, V> map) {
        Collection<V> values = map.values();
        requireNonEmptyChoices("chooseValueFrom", values);
        return chooseOneFromCollection(values);
    }

    static <A> Generator<A> frequency(FrequencyMap<A> frequencyMap) {
        return frequencyMap.toGenerator();
    }

    @SafeVarargs
    static <A> Generator<A> frequency(Weighted<? extends Generator<? extends A>> first,
                                      Weighted<? extends Generator<? extends A>>... more) {
        return frequencyImpl(cons(first, asList(more)));
    }

    @SafeVarargs
    public static <A> Generator<A> frequencyValues(Weighted<? extends A> first,
                                                   Weighted<? extends A>... more) {
        return frequencyImpl(cons(first.fmap(Generators::constant),
                com.jnape.palatable.lambda.functions.builtin.fn2.Map.map(w -> w.fmap(Generators::constant), asList(more))));
    }

    static <A> Generator<A> frequency(Collection<Weighted<? extends Generator<? extends A>>> entries) {
        return frequencyImpl(entries);
    }

    private static <A> Generator<A> frequencyImpl(Iterable<Weighted<? extends Generator<? extends A>>> entries) {
        return FoldLeft.<Weighted<? extends Generator<? extends A>>, FrequencyMapBuilder<A>>foldLeft(
                FrequencyMapBuilder::add,
                frequencyMapBuilder(), entries)
                .build()
                .toGenerator();
    }

    private static <A> void requireNonEmptyChoices(String methodName, Iterable<A> items) {
        if (!items.iterator().hasNext()) {
            throw new IllegalArgumentException(methodName + " requires at least one choice");
        }
    }

    private static <A> Generator<ImmutableVector<A>> chooseSomeFromValues(int min, NonEmptyVector<A> domain) {
        return Generators.sized(k -> reservoirSample(domain.size(), Math.max(k, min))
                .fmap(indices -> {
                    VectorBuilder<A> builder = Vector.builder(k);
                    for (Integer idx : indices) {
                        builder = builder.add(domain.unsafeGet(idx));
                    }
                    return builder.build();
                }));
    }

    @SuppressWarnings("unchecked")
    private static <A> Generator<ImmutableVector<A>> chooseSomeFromGenerators(int min, NonEmptyVector<Generator<? extends A>> domain) {
        return Generators.sized(k -> reservoirSample(domain.size(), Math.max(k, min))
                .flatMap(indices -> {
                    ArrayList<Generator<A>> generators = new ArrayList<>();
                    for (Integer idx : indices) {
                        generators.add((Generator<A>) domain.unsafeGet(idx));
                    }
                    return buildVector(generators);
                }));
    }

}
