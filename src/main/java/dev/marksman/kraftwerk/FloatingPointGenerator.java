package dev.marksman.kraftwerk;

public interface FloatingPointGenerator<A> extends Generator<A> {

    FloatingPointGenerator<A> withNaNs(boolean enabled);

    FloatingPointGenerator<A> withInfinities(boolean enabled);

    default FloatingPointGenerator<A> withNaNs() {
        return withNaNs(true);
    }

    default FloatingPointGenerator<A> withInfinities() {
        return withInfinities(true);
    }

}
