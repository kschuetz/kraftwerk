package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;

final class Collections {
    private Collections() {
    }

    static <A> Generator<ArrayList<A>> generateArrayList(Generator<A> gen) {
        return Generators.sized(n -> buildArrayList(n, gen));
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyArrayList(Generator<A> gen) {
        return Generators.sized(n -> buildArrayList(Math.max(1, n), gen));
    }

    static <A> Generator<ArrayList<A>> generateArrayListOfSize(int size, Generator<A> gen) {
        Preconditions.requireNaturalSize(size);
        return buildArrayList(size, gen);
    }

    static <A> Generator<ArrayList<A>> generateArrayListOfSize(IntRange sizeRange, Generator<A> gen) {
        Preconditions.requireNaturalSize(sizeRange);
        return generateCollectionSize(sizeRange).flatMap(size -> buildArrayList(size, gen));
    }

    static <A> Generator<HashSet<A>> generateHashSet(Generator<A> gen) {
        return Generators.sized(n -> buildHashSet(n, gen));
    }

    static <A> Generator<HashSet<A>> generateNonEmptyHashSet(Generator<A> gen) {
        return Generators.sized(n -> buildHashSet(Math.max(1, n), gen));
    }

    static <A> Generator<ImmutableVector<A>> generateVector(Generator<A> gen) {
        return Generators.sized(n -> Generators.buildVector(n, gen));
    }

    static <A> Generator<ImmutableVector<A>> generateVectorOfSize(int size, Generator<A> gen) {
        return Generators.buildVector(size, gen);
    }

    static <A> Generator<ImmutableVector<A>> generateVectorOfSize(IntRange sizeRange, Generator<A> gen) {
        return Generators.buildVector(sizeRange, gen);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> gen) {
        return Generators.sized(n -> Generators.buildNonEmptyVector(Math.max(1, n), gen));
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(int size, Generator<A> gen) {
        return Generators.buildNonEmptyVector(size, gen);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(IntRange sizeRange, Generator<A> gen) {
        return Generators.buildNonEmptyVector(sizeRange, gen);
    }

    static <K, V> Generator<Map<K, V>> generateMap(Generator<K> generateKey,
                                                   Generator<V> generateValue) {
        return Generators.sized(n -> generateMapOfSize(n, generateKey, generateValue));
    }

    static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> generateKey,
                                                           Generator<V> generateValue) {
        return Generators.sized(n -> generateMapOfSize(Math.max(1, n), generateKey, generateValue));
    }

    static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                   Generator<V> generateValue) {
        return generateMapImpl(keys.size(), keys, generateValue);
    }

    static <K, V> Generator<Map<K, V>> generateMap(Vector<K> keys,
                                                   Generator<V> generateValue) {
        return generateMapImpl(keys.size(), keys, generateValue);
    }

    static Generator<Integer> generateCollectionSize(IntRange sizeRange) {
        return Generators.generateInt(sizeRange);
    }

    private static <A> Generator<ArrayList<A>> buildArrayList(int size, Generator<A> gen) {
        Preconditions.requireNaturalSize(size);
        return Generators.buildCollection(ArrayList::new, size, gen);
    }

    private static <A> Generator<HashSet<A>> buildHashSet(int size, Generator<A> gen) {
        return Generators.buildCollection(HashSet::new, size, gen);
    }

    private static <K, V> Generator<Map<K, V>> generateMapImpl(int size,
                                                               Iterable<K> keys,
                                                               Generator<V> generateValue) {
        return generateArrayListOfSize(size, generateValue)
                .fmap(values -> {
                    HashMap<K, V> result = new HashMap<>();
                    zip(keys, values)
                            .forEach(t -> result.put(t.getKey(), t.getValue()));
                    return result;
                });
    }

    private static <K, V> Generator<Map<K, V>> generateMapOfSize(int size, Generator<K> generateKey, Generator<V> generateValue) {
        return generateArrayListOfSize(size, generateKey)
                .flatMap(keys -> generateMapImpl(keys.size(), keys, generateValue));
    }
}
