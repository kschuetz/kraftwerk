package dev.marksman.composablerandom.legacy;

import dev.marksman.composablerandom.OldGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.composablerandom.OldGenerator.constant;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OldFrequencyEntry<A> {
    private final int weight;
    private final OldGenerator<A> generator;

    public static <A> OldFrequencyEntry<A> entry(int weight, OldGenerator<A> generator) {
        return new OldFrequencyEntry<>(weight, generator);
    }

    public static <A> OldFrequencyEntry<A> entry(int weight, A value) {
        return new OldFrequencyEntry<>(weight, constant(value));
    }
}
