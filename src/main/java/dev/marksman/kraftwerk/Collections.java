package dev.marksman.kraftwerk;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.NonEmptyVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;

class Collections {

    static <A> Generator<ArrayList<A>> generateArrayList(Generator<A> gen) {
        return Generators.sized(n -> buildArrayList(n, gen));
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyArrayList(Generator<A> gen) {
        return Generators.sized(n -> buildArrayList(Math.max(1, n), gen));
    }

    static <A> Generator<ArrayList<A>> generateArrayListOfSize(int size, Generator<A> gen) {
        if (size < 0) {
            throw new IllegalArgumentException("size must be >= 0");
        }
        return buildArrayList(size, gen);
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

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVector(Generator<A> gen) {
        return Generators.sized(n -> Generators.buildNonEmptyVector(Math.max(1, n), gen));
    }

    static <A> Generator<ImmutableVector<A>> generateVectorOfSize(int size, Generator<A> gen) {
        if (size < 0) {
            throw new IllegalArgumentException("size must be >= 0");
        }
        return Generators.buildVector(size, gen);
    }

    static <A> Generator<ImmutableNonEmptyVector<A>> generateNonEmptyVectorOfSize(int size, Generator<A> gen) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
        return Generators.buildNonEmptyVector(size, gen);
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

    static <K, V> Generator<Map<K, V>> generateMap(NonEmptyVector<K> keys,
                                                   Generator<V> generateValue) {
        return generateMapImpl(keys.size(), keys, generateValue);
    }

    private static <A> Generator<ArrayList<A>> buildArrayList(int n, Generator<A> gen) {
        return Generators.buildCollection(ArrayList::new, n, gen);
    }

    private static <A> Generator<HashSet<A>> buildHashSet(int n, Generator<A> gen) {
        return Generators.buildCollection(HashSet::new, n, gen);
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
