package dev.marksman.composablerandom.domain;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;

public class Characters {

    public static ImmutableNonEmptyVector<Character> alphaLower() {
        return NonEmptyVector.lazyFill(26, idx -> (char) (97 + idx));
    }

    public static ImmutableNonEmptyVector<Character> alphaUpper() {
        return NonEmptyVector.lazyFill(26, idx -> (char) (65 + idx));
    }

    public static ImmutableNonEmptyVector<Character> digits() {
        return NonEmptyVector.lazyFill(10, idx -> (char) (48 + idx));
    }

    public static ImmutableNonEmptyVector<Character> asciiPrintable() {
        return NonEmptyVector.lazyFill(95, idx -> (char) (32 + idx));
    }

}
