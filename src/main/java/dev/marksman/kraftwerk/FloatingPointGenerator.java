package dev.marksman.kraftwerk;

/**
 * A {@code Generator} with additional methods for specifying floating point behaviors.
 *
 * @param <A> the output type (typically {@link Float} or {@link Double}
 */
public interface FloatingPointGenerator<A> extends Generator<A> {
    /**
     * Creates a new {@code FloatingPointGenerator} that is the same as this one, but with the
     * generation of NaNs enabled (or disabled).
     *
     * @param enabled if true, NaNs are occasionally mixed into the output
     * @return a {@code FloatingPointGenerator<A>}
     */
    FloatingPointGenerator<A> withNaNs(boolean enabled);

    /**
     * Creates a new {@code FloatingPointGenerator} that is the same as this one, but with the
     * generation of infinity values enabled (or disabled).
     *
     * @param enabled if true, infinity values are occasionally mixed into the output
     * @return a {@code FloatingPointGenerator<A>}
     */
    FloatingPointGenerator<A> withInfinities(boolean enabled);

    /**
     * Creates a new {@code FloatingPointGenerator} that is the same as this one, but with the
     * generation of NaNs enabled.
     *
     * @return a {@code FloatingPointGenerator<A>}
     */
    default FloatingPointGenerator<A> withNaNs() {
        return withNaNs(true);
    }

    /**
     * Creates a new {@code FloatingPointGenerator} that is the same as this one, but with the
     * generation of infinity values enabled.
     *
     * @return a {@code FloatingPointGenerator<A>}
     */
    default FloatingPointGenerator<A> withInfinities() {
        return withInfinities(true);
    }
}
