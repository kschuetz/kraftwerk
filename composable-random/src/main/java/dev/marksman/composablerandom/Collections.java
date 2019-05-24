package dev.marksman.composablerandom;

import dev.marksman.collectionviews.NonEmptyVector;

import java.util.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static dev.marksman.composablerandom.Generator.buildCollection;
import static dev.marksman.composablerandom.Generator.sized;

class Collections {

    static <A> Generator<ArrayList<A>> generateList(Generator<A> g) {
        return sized(n -> buildArrayList(n, g));
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyList(Generator<A> g) {
        return sized(n -> buildArrayList(Math.max(1, n), g));
    }

    static <A> Generator<ArrayList<A>> generateListOfN(int n, Generator<A> g) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return buildArrayList(n, g);
    }

    static <A> Generator<HashSet<A>> generateSet(Generator<A> g) {
        return sized(n -> buildHashSet(n, g));
    }

    static <A> Generator<HashSet<A>> generateNonEmptySet(Generator<A> g) {
        return sized(n -> buildHashSet(Math.max(1, n), g));
    }

    static <K, V> Generator<Map<K, V>> generateMap(Generator<K> keyGenerator,
                                                   Generator<V> valueGenerator) {
        return Generator.sized(n -> generateMapOfN(n, keyGenerator, valueGenerator));
    }

    static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> keyGenerator,
                                                           Generator<V> valueGenerator) {
        return Generator.sized(n -> generateMapOfN(Math.max(1, n), keyGenerator, valueGenerator));
    }

    static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                   Generator<V> valueGenerator) {
        return generateMapImpl(keys.size(), keys, valueGenerator);
    }

    static <K, V> Generator<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                   Generator<V> valueGenerator) {
        return generateMapImpl((int) keys.size(), keys, valueGenerator);
    }

    private static <A> Generator<ArrayList<A>> buildArrayList(int n, Generator<A> generator) {
        return buildCollection(ArrayList::new, n, generator);
    }

    private static <A> Generator<HashSet<A>> buildHashSet(int n, Generator<A> generator) {
        return buildCollection(HashSet::new, n, generator);
    }

    private static <K, V> Generator<Map<K, V>> generateMapImpl(int size,
                                                               Iterable<K> keys,
                                                               Generator<V> valueGenerator) {
        return generateListOfN(size, valueGenerator)
                .fmap(values -> {
                    HashMap<K, V> result = new HashMap<>();
                    zip(keys, values)
                            .forEach(t -> result.put(t.getKey(), t.getValue()));
                    return result;
                });
    }

    private static <K, V> Generator<Map<K, V>> generateMapOfN(int n, Generator<K> keyGenerator, Generator<V> valueGenerator) {
        return generateListOfN(n, keyGenerator)
                .flatMap(keys -> generateMapImpl(keys.size(), keys, valueGenerator));
    }

}
