package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.Generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Collections {

    static <A> Generator<ArrayList<A>> generateList(Generator<A> g) {
        return null;
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyList(Generator<A> g) {
        return null;
    }

    static <A> Generator<ArrayList<A>> generateListOfN(int count, Generator<A> g) {
        return null;
    }

    static <A> Generator<Set<A>> generateSet(Generator<A> g) {
        return null;
    }

    static <A> Generator<Set<A>> generateNonEmptySet(Generator<A> g) {
        return null;
    }

    static <K, V> Generator<Map<K, V>> generateMap(Generator<K> keyGenerator,
                                                   Generator<V> valueGenerator) {
        return null;
    }

    static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                   Generator<V> valueGenerator) {
        return null;
    }

    static <K, V> Generator<Map<K, V>> generateMap(DiscreteDomain<K> keys,
                                                   Generator<V> valueGenerator) {
        return null;
    }

    static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> keyGenerator,
                                                           Generator<V> valueGenerator) {
        return null;
    }

}
