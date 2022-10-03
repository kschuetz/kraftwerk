package software.kes.kraftwerk;

import com.jnape.palatable.lambda.functions.Fn2;

import java.time.LocalDate;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Various transformation functions for affecting the distributions of {@link Generator}s.
 */
public final class Distributions {
    private Distributions() {
    }

    /**
     * Creates a new {@link Generator} that spreads the outputs of another {@code Generator} into
     * a linear ramp up distribution.
     *
     * @param generator the original {@code Generator}
     * @param <A>       the output type (must be {@link Comparable})
     * @return a {@code Generator<A>}
     */
    public static <A extends Comparable<A>> Generator<A> linearRampUp(Generator<A> generator) {
        return generator.pair().fmap(into((a, b) -> (a.compareTo(b) >= 0) ? a : b));
    }

    /**
     * Creates a new {@link Generator} that spreads the outputs of another {@code Generator} into
     * a linear ramp down distribution.
     *
     * @param generator the original {@code Generator}
     * @param <A>       the output type (must be {@link Comparable})
     * @return a {@code Generator<A>}
     */
    public static <A extends Comparable<A>> Generator<A> linearRampDown(Generator<A> generator) {
        return generator.pair().fmap(into((a, b) -> (a.compareTo(b) <= 0) ? a : b));
    }

    /**
     * Creates a new {@link Generator} that spreads the outputs of another {@code Generator} into
     * a triangular distribution.
     *
     * @param calculateMean a function to calculate the mean of two values of type {@code A}
     * @param generator     the original {@code Generator}
     * @param <A>           the output type
     * @return a {@code Generator<A>}
     */
    public static <A> Generator<A> triangular(Fn2<A, A, A> calculateMean, Generator<A> generator) {
        return generator.pair().fmap(into(calculateMean));
    }

    /**
     * Creates a new {@code Generator} of {@link Integer}s that spreads the outputs of another {@code Generator}
     * into a triangular distribution.
     *
     * @param generator the original {@code Generator}
     * @return a {@code Generator<Integer>}
     */
    public static Generator<Integer> triangularInt(Generator<Integer> generator) {
        return triangular((a, b) -> (a + b) / 2, generator);
    }


    /**
     * Creates a new {@code Generator} of {@link Double}s that spreads the outputs of another {@code Generator}
     * into a triangular distribution.
     *
     * @param generator the original {@code Generator}
     * @return a {@code Generator<Double>}
     */
    public static Generator<Double> triangularDouble(Generator<Double> generator) {
        return triangular((a, b) -> (a + b) / 2d, generator);
    }

    /**
     * Creates a new {@code Generator} of {@link Float}s that spreads the outputs of another {@code Generator}
     * into a triangular distribution.
     *
     * @param generator the original {@code Generator}
     * @return a {@code Generator<Float>}
     */
    public static Generator<Float> triangularFloat(Generator<Float> generator) {
        return triangular((a, b) -> (a + b) / 2f, generator);
    }

    /**
     * Creates a new {@code Generator} of {@link Short}s that spreads the outputs of another {@code Generator}
     * into a triangular distribution.
     *
     * @param generator the original {@code Generator}
     * @return a {@code Generator<Short>}
     */
    public static Generator<Short> triangularShort(Generator<Short> generator) {
        return triangular((a, b) -> (short) ((a + b) / 2), generator);
    }

    /**
     * Creates a new {@code Generator} of {@link Byte}s that spreads the outputs of another {@code Generator}
     * into a triangular distribution.
     *
     * @param generator the original {@code Generator}
     * @return a {@code Generator<Byte>}
     */
    public static Generator<Byte> triangularByte(Generator<Byte> generator) {
        return triangular((a, b) -> (byte) ((a + b) / 2), generator);
    }

    /**
     * Creates a new {@code Generator} of {@link LocalDate}s that spreads the outputs of another {@code Generator}
     * into a triangular distribution.
     *
     * @param generator the original {@code Generator}
     * @return a {@code Generator<LocalDate>}
     */
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
