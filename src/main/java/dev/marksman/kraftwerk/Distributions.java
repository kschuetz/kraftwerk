package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn2;

import java.time.LocalDate;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static java.time.temporal.ChronoUnit.DAYS;

public final class Distributions {
    private Distributions() {
    }

    public static <A extends Comparable<A>> Generator<A> linearRampUp(Generator<A> generator) {
        return generator.pair().fmap(into((a, b) -> (a.compareTo(b) >= 0) ? a : b));
    }

    public static <A extends Comparable<A>> Generator<A> linearRampDown(Generator<A> generator) {
        return generator.pair().fmap(into((a, b) -> (a.compareTo(b) <= 0) ? a : b));
    }

    public static <A> Generator<A> triangular(Fn2<A, A, A> calculateMean, Generator<A> generator) {
        return generator.pair().fmap(into(calculateMean));
    }

    public static Generator<Integer> triangularInt(Generator<Integer> generator) {
        return triangular((a, b) -> (a + b) / 2, generator);
    }

    public static Generator<Double> triangularDouble(Generator<Double> generator) {
        return triangular((a, b) -> (a + b) / 2d, generator);
    }

    public static Generator<Float> triangularFloat(Generator<Float> generator) {
        return triangular((a, b) -> (a + b) / 2f, generator);
    }

    public static Generator<Short> triangularShort(Generator<Short> generator) {
        return triangular((a, b) -> (short) ((a + b) / 2), generator);
    }

    public static Generator<Byte> triangularByte(Generator<Byte> generator) {
        return triangular((a, b) -> (byte) ((a + b) / 2), generator);
    }

    public static Generator<LocalDate> triangularLocalDate(Generator<LocalDate> generator) {
        return triangular((a, b) -> {
            if (a.isBefore(b)) {
                return a.plusDays(DAYS.between(a, b) / 2);
            } else {
                return b.plusDays(DAYS.between(b, a) / 2);
            }
        }, generator);
    }
}
