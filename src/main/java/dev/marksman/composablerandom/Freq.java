package dev.marksman.composablerandom;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Freq<A> {
    private final int weight;
    private final Random<A> random;

    public static <A> Freq<A> freq(int weight, Random<A> random) {
        return new Freq<>(weight, random);
    }

    public static <A> Freq<A> freq(int weight, A value) {
        return new Freq<>(weight, Random.constant(value));
    }
}
