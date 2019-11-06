package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public final class NoBias implements BiasSettings {
    private static final NoBias INSTANCE = new NoBias();

    private NoBias() {

    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Integer>> getIntSpecialValues(int min, int max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Long>> getLongSpecialValues(long min, long max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Float>> getFloatSpecialValues(float min, float max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Double>> getDoubleSpecialValues(double min, double max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Byte>> getByteSpecialValues(byte min, byte max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Short>> getShortSpecialValues(short min, short max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Character>> getCharacterSpecialValues(char min, char max) {
        return nothing();
    }

    @Override
    public Maybe<ImmutableNonEmptyVector<Integer>> getSizeSpecialValues(SizeParameters sizeParameters) {
        return nothing();
    }

    @Override
    public BiasSettings overrideWith(BiasSettings other) {
        return other;
    }

    public static BiasSettings noBias() {
        return INSTANCE;
    }
}
