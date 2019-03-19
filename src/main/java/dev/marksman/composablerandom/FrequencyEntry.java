package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyEntry<A> {
    private final int weight;
    private final Generate<A> generate;

    public static <A> FrequencyEntry<A> frequencyEntry(int weight, Generate<A> generate) {
        return new FrequencyEntry<>(weight, generate);
    }

    public static <A> FrequencyEntry<A> frequencyEntry(int weight, A value) {
        return new FrequencyEntry<>(weight, Generate.constant(value));
    }
}
