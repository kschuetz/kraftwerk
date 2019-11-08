package dev.marksman.kraftwerk;

public abstract class FloatingPointGenerator<A> extends Generator<A> {

    public abstract FloatingPointGenerator<A> withNaNs(boolean enabled);

    public abstract FloatingPointGenerator<A> withInfinities(boolean enabled);

    public FloatingPointGenerator<A> withNaNs() {
        return withNaNs(true);
    }

    public FloatingPointGenerator<A> withInfinities() {
        return withInfinities(true);
    }

}
