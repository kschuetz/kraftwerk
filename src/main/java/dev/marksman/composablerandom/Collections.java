package dev.marksman.composablerandom;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;

import java.util.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static dev.marksman.composablerandom.Generator.*;

class Collections {

    static <A> Generator<ArrayList<A>> generateArrayList(Generator<A> gen) {
        return sized(n -> buildArrayList(n, gen));
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyArrayList(Generator<A> gen) {
        return sized(n -> buildArrayList(Math.max(1, n), gen));
    }

    static <A> Generator<ArrayList<A>> generateArrayListOfN(int n, Generator<A> gen) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return buildArrayList(n, gen);
    }

    static <A> Generator<HashSet<A>> generateHashSet(Generator<A> gen) {
        return sized(n -> buildHashSet(n, gen));
    }

    static <A> Generator<HashSet<A>> generateNonEmptyHashSet(Generator<A> gen) {
        return sized(n -> buildHashSet(Math.max(1, n), gen));
    }

    static <A> Generator<ImmutableVector<A>> generateVector(Generator<A> gen) {
        return sized(n -> buildVector(n, gen));
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> gen) {
        return sized(n -> buildNonEmptyVector(Math.max(1, n), gen));
    }

    static <A> Generator<ImmutableVector<A>> generateVectorOfN(int n, Generator<A> gen) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return buildVector(n, gen);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfN(int n, Generator<A> gen) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be >= 1");
        }
        return buildNonEmptyVector(n, gen);
    }

    static <K, V> Generator<Map<K, V>> generateMap(Generator<K> generateKey,
                                                   Generator<V> generateValue) {
        return Generator.sized(n -> generateMapOfN(n, generateKey, generateValue));
    }

    static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> generateKey,
                                                           Generator<V> generateValue) {
        return Generator.sized(n -> generateMapOfN(Math.max(1, n), generateKey, generateValue));
    }

    static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                   Generator<V> generateValue) {
        return generateMapImpl(keys.size(), keys, generateValue);
    }

    static <K, V> Generator<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                   Generator<V> generateValue) {
        return generateMapImpl((int) keys.size(), keys, generateValue);
    }

    private static <A> Generator<ArrayList<A>> buildArrayList(int n, Generator<A> gen) {
        return buildCollection(ArrayList::new, n, gen);
    }

    private static <A> Generator<HashSet<A>> buildHashSet(int n, Generator<A> gen) {
        return buildCollection(HashSet::new, n, gen);
    }

    private static <K, V> Generator<Map<K, V>> generateMapImpl(int size,
                                                               Iterable<K> keys,
                                                               Generator<V> generateValue) {
        return generateArrayListOfN(size, generateValue)
                .fmap(values -> {
                    HashMap<K, V> result = new HashMap<>();
                    zip(keys, values)
                            .forEach(t -> result.put(t.getKey(), t.getValue()));
                    return result;
                });
    }

    private static <K, V> Generator<Map<K, V>> generateMapOfN(int n, Generator<K> generateKey, Generator<V> generateValue) {
        return generateArrayListOfN(n, generateKey)
                .flatMap(keys -> generateMapImpl(keys.size(), keys, generateValue));
    }

}
