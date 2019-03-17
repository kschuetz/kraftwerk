package dev.marksman.random;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Supplier;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Freq<A> {
    private final int weight;
    private final Supplier<A> valueSupplier;

    public static <A> Freq<A> freq(int weight, Supplier<A> valueSupplier) {
        return new Freq<>(weight, valueSupplier);
    }

    public static <A> Freq<A> freq(int weight, A value) {
        return new Freq<>(weight, () -> value);
    }
}
