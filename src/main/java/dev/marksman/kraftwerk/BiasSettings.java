package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;

public interface BiasSettings {
    Maybe<ImmutableNonEmptyVector<Integer>> getIntSpecialValues(int min, int max);

    Maybe<ImmutableNonEmptyVector<Long>> getLongSpecialValues(long min, long max);

    Maybe<ImmutableNonEmptyVector<Float>> getFloatSpecialValues(float min, float max);

    Maybe<ImmutableNonEmptyVector<Double>> getDoubleSpecialValues(double min, double max);

    Maybe<ImmutableNonEmptyVector<Byte>> getByteSpecialValues(byte min, byte max);

    Maybe<ImmutableNonEmptyVector<Short>> getShortSpecialValues(short min, short max);

    Maybe<ImmutableNonEmptyVector<Character>> getCharacterSpecialValues(char min, char max);

    Maybe<ImmutableNonEmptyVector<Integer>> getSizeSpecialValues(SizeParameters sizeParameters);

    BiasSettings overrideWith(BiasSettings other);
}
