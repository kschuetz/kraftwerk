package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.frequency.FrequencyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static dev.marksman.kraftwerk.Distributions.linearRampDown;
import static dev.marksman.kraftwerk.Generators.buildVector;
import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.ReservoirSample.reservoirSample;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static java.util.Arrays.asList;

final class Choose {
    private Choose() {
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOfValues(A first, A... more) {
        return chooseOneValueFromDomain(NonEmptyVector.of(first, more));
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return foldLeft(FrequencyMap::add, FrequencyMap.<A>frequencyMap(first), asList(more)).toGenerator();
    }

    @SafeVarargs
    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOfValues(A first, A... more) {
        return chooseAtLeastOneValueFromDomain(Vector.of(first, more));
    }

    @SafeVarargs
    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return chooseSomeFromGenerators(1, NonEmptyVector.of(first, more))
                .fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    @SafeVarargs
    static <A> Generator<ImmutableVector<A>> chooseSomeOfValues(A first, A... more) {
        return chooseSomeValuesFromDomain(Vector.of(first, more));
    }

    @SafeVarargs
    static <A> Generator<ImmutableVector<A>> chooseSomeOf(Generator<? extends A> first, Generator<? extends A>... more) {
        return chooseSomeFromGenerators(0, NonEmptyVector.of(first, more));
    }

    static <A> Generator<A> chooseOneValueFromCollection(Iterable<A> candidates) {
        requireNonEmptyCandidates("chooseOneFrom", candidates);
        return chooseOneValueFromDomain(NonEmptyVector.copyFromOrThrow(candidates));
    }

    static <A> Generator<A> chooseOneValueFromDomain(NonEmptyVector<A> domain) {
        int size = domain.size();
        if (size == 1) {
            return constant(domain.unsafeGet(0));
        } else {
            return Generators.generateIntIndex(size).fmap(domain::unsafeGet);
        }
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneValueFromCollection(Iterable<A> candidates) {
        requireNonEmptyCandidates("chooseAtLeastOneFrom", candidates);
        return chooseAtLeastOneValueFromDomain(NonEmptyVector.copyFromOrThrow(candidates));
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> chooseAtLeastOneValueFromDomain(NonEmptyVector<A> domain) {
        return chooseSomeFromValues(1, domain).fmap(ImmutableVector::toNonEmptyOrThrow);
    }

    static <A> Generator<ImmutableVector<A>> chooseSomeValuesFromCollection(Collection<A> candidates) {
        if (candidates.isEmpty()) {
            return constant(Vector.empty());
        } else {
            return chooseSomeValuesFromDomain(NonEmptyVector.copyFromOrThrow(candidates));
        }
    }

    static <A> Generator<ImmutableVector<A>> chooseSomeValuesFromDomain(NonEmptyVector<A> domain) {
        return chooseSomeFromValues(0, domain);
    }

    static <K, V> Generator<Map.Entry<K, V>> chooseEntryFromMap(Map<K, V> map) {
        Set<Map.Entry<K, V>> entries = map.entrySet();
        requireNonEmptyCandidates("chooseEntryFrom", entries);
        return chooseOneValueFromCollection(entries);
    }

    static <K, V> Generator<K> chooseKeyFromMap(Map<K, V> map) {
        Set<K> keys = map.keySet();
        requireNonEmptyCandidates("chooseKeyFrom", keys);
        return chooseOneValueFromCollection(keys);
    }

    static <K, V> Generator<V> chooseValueFromMap(Map<K, V> map) {
        Collection<V> values = map.values();
        requireNonEmptyCandidates("chooseValueFrom", values);
        return chooseOneValueFromCollection(values);
    }

    static <A> Generator<A> frequency(FrequencyMap<A> frequencyMap) {
        return frequencyMap.toGenerator();
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOfWeighted(Weighted<? extends Generator<? extends A>> first,
                                                Weighted<? extends Generator<? extends A>>... more) {
        return chooseOneFromCollectionWeighted(cons(first, asList(more)));
    }

    @SafeVarargs
    static <A> Generator<A> chooseOneOfWeightedValues(Weighted<? extends A> first,
                                                      Weighted<? extends A>... more) {
        return chooseOneFromCollectionWeighted(cons(first.fmap(Generators::constant),
                com.jnape.palatable.lambda.functions.builtin.fn2.Map.map(w -> w.fmap(Generators::constant), asList(more))));
    }

    static <A> Generator<A> chooseOneFromCollection(Iterable<Generator<? extends A>> candidates) {
        requireNonEmptyCandidates("chooseOneFromCollection", candidates);
        return FoldLeft.<Generator<? extends A>, FrequencyMap<A>>foldLeft(
                FrequencyMap::add,
                frequencyMap(), candidates)
                .toGenerator();
    }

    static <A> Generator<A> chooseOneFromCollectionWeighted(Iterable<Weighted<? extends Generator<? extends A>>> entries) {
        return FoldLeft.<Weighted<? extends Generator<? extends A>>, FrequencyMap<A>>foldLeft(
                FrequencyMap::add,
                frequencyMap(), entries)
                .toGenerator();
    }

    private static <A> void requireNonEmptyCandidates(String methodName, Iterable<A> candidates) {
        if (!candidates.iterator().hasNext()) {
            throw new IllegalArgumentException(methodName + " requires at least one candidate");
        }
    }

    private static <A> Generator<ImmutableVector<A>> chooseSomeFromValues(int min, NonEmptyVector<A> domain) {
        IntRange sizeRange = IntRange.from(min).to(Math.max(min, domain.size()));
        return chooseSize(sizeRange).flatMap(k -> reservoirSample(domain.size(), Math.max(k, min))
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
        IntRange sizeRange = IntRange.from(min).to(Math.max(min, domain.size()));
        return chooseSize(sizeRange).flatMap(k -> reservoirSample(domain.size(), Math.max(k, min))
                .flatMap(indices -> {
                    ArrayList<Generator<A>> generators = new ArrayList<>();
                    for (Integer idx : indices) {
                        generators.add((Generator<A>) domain.unsafeGet(idx));
                    }
                    return buildVector(generators);
                }));
    }

    private static Generator<Integer> chooseSize(IntRange sizeRange) {
        return linearRampDown(generateInt(sizeRange));
    }
}
