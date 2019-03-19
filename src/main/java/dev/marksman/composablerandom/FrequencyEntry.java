package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyEntry<A> {
    private final int weight;
    private final Random<A> random;

    public static <A> FrequencyEntry<A> frequencyEntry(int weight, Random<A> random) {
        return new FrequencyEntry<>(weight, random);
    }

    public static <A> FrequencyEntry<A> frequencyEntry(int weight, A value) {
        return new FrequencyEntry<>(weight, Random.constant(value));
    }
}
