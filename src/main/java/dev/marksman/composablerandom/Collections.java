package dev.marksman.composablerandom;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;

import java.util.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static dev.marksman.composablerandom.Generate.*;

class Collections {

    static <A> Generate<ArrayList<A>> generateArrayList(Generate<A> gen) {
        return sized(n -> buildArrayList(n, gen));
    }

    static <A> Generate<ArrayList<A>> generateNonEmptyArrayList(Generate<A> gen) {
        return sized(n -> buildArrayList(Math.max(1, n), gen));
    }

    static <A> Generate<ArrayList<A>> generateArrayListOfN(int n, Generate<A> gen) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return buildArrayList(n, gen);
    }

    static <A> Generate<HashSet<A>> generateHashSet(Generate<A> gen) {
        return sized(n -> buildHashSet(n, gen));
    }

    static <A> Generate<HashSet<A>> generateNonEmptyHashSet(Generate<A> gen) {
        return sized(n -> buildHashSet(Math.max(1, n), gen));
    }

    static <A> Generate<ImmutableVector<A>> generateVector(Generate<A> gen) {
        return sized(n -> buildVector(n, gen));
    }

    static <A> Generate<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generate<A> gen) {
        return sized(n -> buildNonEmptyVector(Math.max(1, n), gen));
    }

    static <A> Generate<ImmutableVector<A>> generateVectorOfN(int n, Generate<A> gen) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return buildVector(n, gen);
    }

    static <A> Generate<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfN(int n, Generate<A> gen) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be >= 1");
        }
        return buildNonEmptyVector(n, gen);
    }

    static <K, V> Generate<Map<K, V>> generateMap(Generate<K> generateKey,
                                                  Generate<V> generateValue) {
        return Generate.sized(n -> generateMapOfN(n, generateKey, generateValue));
    }

    static <K, V> Generate<Map<K, V>> generateNonEmptyMap(Generate<K> generateKey,
                                                          Generate<V> generateValue) {
        return Generate.sized(n -> generateMapOfN(Math.max(1, n), generateKey, generateValue));
    }

    static <K, V> Generate<Map<K, V>> generateMap(Collection<K> keys,
                                                  Generate<V> generateValue) {
        return generateMapImpl(keys.size(), keys, generateValue);
    }

    static <K, V> Generate<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                  Generate<V> generateValue) {
        return generateMapImpl((int) keys.size(), keys, generateValue);
    }

    private static <A> Generate<ArrayList<A>> buildArrayList(int n, Generate<A> gen) {
        return buildCollection(ArrayList::new, n, gen);
    }

    private static <A> Generate<HashSet<A>> buildHashSet(int n, Generate<A> gen) {
        return buildCollection(HashSet::new, n, gen);
    }

    private static <K, V> Generate<Map<K, V>> generateMapImpl(int size,
                                                              Iterable<K> keys,
                                                              Generate<V> generateValue) {
        return generateArrayListOfN(size, generateValue)
                .fmap(values -> {
                    HashMap<K, V> result = new HashMap<>();
                    zip(keys, values)
                            .forEach(t -> result.put(t.getKey(), t.getValue()));
                    return result;
                });
    }

    private static <K, V> Generate<Map<K, V>> generateMapOfN(int n, Generate<K> generateKey, Generate<V> generateValue) {
        return generateArrayListOfN(n, generateKey)
                .flatMap(keys -> generateMapImpl(keys.size(), keys, generateValue));
    }

}
