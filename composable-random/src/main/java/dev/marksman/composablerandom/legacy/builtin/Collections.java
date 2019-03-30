package dev.marksman.composablerandom.legacy.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static dev.marksman.composablerandom.Result.result;

class Collections {

    static <A> OldGenerator<ArrayList<A>> generateList(OldGenerator<A> g) {
        return null;
    }

    static <A> OldGenerator<ArrayList<A>> generateNonEmptyList(OldGenerator<A> g) {
        return null;
    }

    static <A> OldGenerator<ArrayList<A>> generateListOfN(int n, OldGenerator<A> g) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return OldGenerator.contextDependent(s0 -> {
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

    static <A> OldGenerator<Set<A>> generateSet(OldGenerator<A> g) {
        return null;
    }

    static <A> OldGenerator<Set<A>> generateNonEmptySet(OldGenerator<A> g) {
        return null;
    }

    static <K, V> OldGenerator<Map<K, V>> generateMap(OldGenerator<K> keyGenerator,
                                                      OldGenerator<V> valueGenerator) {
        return null;
    }

    static <K, V> OldGenerator<Map<K, V>> generateMap(Collection<K> keys,
                                                      OldGenerator<V> valueGenerator) {
        return null;
    }

    static <K, V> OldGenerator<Map<K, V>> generateMap(DiscreteDomain<K> keys,
                                                      OldGenerator<V> valueGenerator) {
        return null;
    }

    static <K, V> OldGenerator<Map<K, V>> generateNonEmptyMap(OldGenerator<K> keyGenerator,
                                                              OldGenerator<V> valueGenerator) {
        return null;
    }

}
