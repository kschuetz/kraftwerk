package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static dev.marksman.composablerandom.Result.result;

class Collections {

    static <A> Generator<ArrayList<A>> generateList(Generator<A> g) {
        return null;
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyList(Generator<A> g) {
        return null;
    }

    static <A> Generator<ArrayList<A>> generateListOfN(int n, Generator<A> g) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return Generator.contextDependent(s0 -> {
            State current = s0;
            ArrayList<A> result = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                Result<State, A> next = g.run(current);
                current = next._1();
                result.add(next._2());
            }
            return result(current, result);
        });
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
