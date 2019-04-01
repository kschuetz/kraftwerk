package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Instruction;

import java.util.*;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static dev.marksman.composablerandom.Generator.generator;
import static dev.marksman.composablerandom.Instruction.buildCollection;
import static dev.marksman.composablerandom.Instruction.sized;

class Collections {

    static <A> Generator<ArrayList<A>> generateList(Generator<A> g) {
        return generator(sized(n -> buildArrayList(n, g.getInstruction())));
    }

    static <A> Generator<ArrayList<A>> generateNonEmptyList(Generator<A> g) {
        return generator(sized(n -> buildArrayList(Math.max(1, n), g.getInstruction())));
    }

    static <A> Generator<ArrayList<A>> generateListOfN(int n, Generator<A> g) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        return generator(buildArrayList(n, g.getInstruction()));
    }

    static <A> Generator<HashSet<A>> generateSet(Generator<A> g) {
        return generator(sized(n -> buildHashSet(n, g.getInstruction())));
    }

    static <A> Generator<HashSet<A>> generateNonEmptySet(Generator<A> g) {
        return generator(sized(n -> buildHashSet(Math.max(1, n), g.getInstruction())));
    }

    static <K, V> Generator<Map<K, V>> generateMap(Generator<K> keyGenerator,
                                                   Generator<V> valueGenerator) {
        return Generators.sized(n -> generateMapOfN(n, keyGenerator, valueGenerator));
    }

    static <K, V> Generator<Map<K, V>> generateNonEmptyMap(Generator<K> keyGenerator,
                                                           Generator<V> valueGenerator) {
        return Generators.sized(n -> generateMapOfN(Math.max(1, n), keyGenerator, valueGenerator));
    }

    static <K, V> Generator<Map<K, V>> generateMap(Collection<K> keys,
                                                   Generator<V> valueGenerator) {
        return generateMapImpl(keys.size(), keys, valueGenerator);
    }

    static <K, V> Generator<Map<K, V>> generateMap(DiscreteDomain<K> keys,
                                                   Generator<V> valueGenerator) {
        return generateMapImpl((int) keys.getSize(), keys, valueGenerator);
    }

    private static <A> Instruction.Aggregate<A, ArrayList<A>, ArrayList<A>> buildArrayList(int n, Instruction<A> instruction) {
        return buildCollection(ArrayList::new, n, instruction);
    }

    private static <A> Instruction.Aggregate<A, HashSet<A>, HashSet<A>> buildHashSet(int n, Instruction<A> instruction) {
        return buildCollection(HashSet::new, n, instruction);
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
