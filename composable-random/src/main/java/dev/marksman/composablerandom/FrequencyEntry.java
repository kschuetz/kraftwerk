package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyEntry<A> {
    private final int weight;
    private final Generator<A> generator;

    public static <A> FrequencyEntry<A> frequencyEntry(int weight, Generator<A> generator) {
        return new FrequencyEntry<>(weight, generator);
    }

    public static <A> FrequencyEntry<A> frequencyEntry(int weight, A value) {
        return new FrequencyEntry<>(weight, Generator.constant(value));
    }
}
