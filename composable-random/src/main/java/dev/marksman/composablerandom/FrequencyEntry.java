package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.OldGenerator.constant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrequencyEntry<A> {
    private final int weight;
    private final OldGenerator<A> generator;

    public static <A> FrequencyEntry<A> entry(int weight, OldGenerator<A> generator) {
        return new FrequencyEntry<>(weight, generator);
    }

    public static <A> FrequencyEntry<A> entry(int weight, A value) {
        return new FrequencyEntry<>(weight, constant(value));
    }
}
