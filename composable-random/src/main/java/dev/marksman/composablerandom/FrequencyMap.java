package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static dev.marksman.composablerandom.FrequencyEntry.entry;
import static java.util.Collections.emptyList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyMap<A> {
    private final Iterable<FrequencyEntry<? extends A>> entries;

    public FrequencyMap<A> add(int weight, A value) {
        return add(1, Generator.constant(value));
    }

    public FrequencyMap<A> add(int weight, Generator<? extends A> generator) {
        if (weight > 0) {
            return new FrequencyMap<>(cons(entry(weight, generator), entries));
        } else {
            return this;
        }
    }

    public FrequencyMap<A> add(A value) {
        return add(1, value);
    }

    public FrequencyMap<A> add(Generator<A> generator) {
        return add(1, generator);
    }

    public static <A> FrequencyMap<A> frequencyMap(int weight, A value) {
        checkInitialWeight(weight);
        return new FrequencyMap<A>(emptyList()).add(weight, value);
    }

    public static <A> FrequencyMap<A> frequencyMap(int weight, Generator<A> generator) {
        checkInitialWeight(weight);
        return new FrequencyMap<A>(emptyList()).add(weight, generator);
    }

    public static <A> FrequencyMap<A> frequencyMap(A value) {
        return frequencyMap(1, value);
    }

    public static <A> FrequencyMap<A> frequencyMap(Generator<A> generator) {
        return frequencyMap(1, generator);
    }

    private static void checkInitialWeight(int initialWeight) {
        if (initialWeight < 1) {
            throw new IllegalArgumentException("Initial weight in FrequencyMap must be >= 1");
        }
    }

//    private final long totalWeight;
//    private final long TreeMap<Long, Generator<? extends A>> tree;
}
