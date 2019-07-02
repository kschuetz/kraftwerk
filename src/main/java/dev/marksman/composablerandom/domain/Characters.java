package dev.marksman.composablerandom.domain;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVectorBuilder;
import dev.marksman.collectionviews.Vector;

import java.util.HashSet;

public class Characters {

    private static final ImmutableNonEmptyVector<Character> ALPHA_LOWER = NonEmptyVector.lazyFill(26, idx -> (char) (97 + idx));
    private static final ImmutableNonEmptyVector<Character> ALPHA_UPPER = NonEmptyVector.lazyFill(26, idx -> (char) (65 + idx));
    private static final ImmutableNonEmptyVector<Character> ALPHA = union(ALPHA_LOWER, ALPHA_UPPER);
    private static final ImmutableNonEmptyVector<Character> NUMERIC = NonEmptyVector.lazyFill(10, idx -> (char) (48 + idx));
    private static final ImmutableNonEmptyVector<Character> ALPHA_NUMERIC = union(ALPHA, NUMERIC);
    private static final ImmutableNonEmptyVector<Character> ASCII_PRINTABLE = NonEmptyVector.lazyFill(95, idx -> (char) (32 + idx));

    public static ImmutableNonEmptyVector<Character> alphaLower() {
        return ALPHA_LOWER;
    }

    public static ImmutableNonEmptyVector<Character> alphaUpper() {
        return ALPHA_UPPER;
    }

    public static ImmutableNonEmptyVector<Character> numeric() {
        return NUMERIC;
    }

    public static ImmutableNonEmptyVector<Character> asciiPrintable() {
        return ASCII_PRINTABLE;
    }

    public static ImmutableNonEmptyVector<Character> alpha() {
        return ALPHA;
    }

    public static ImmutableNonEmptyVector<Character> alphaNumeric() {
        return ALPHA_NUMERIC;
    }

    @SafeVarargs
    public static ImmutableNonEmptyVector<Character> union(ImmutableNonEmptyVector<Character> first,
                                                           ImmutableNonEmptyVector<Character>... more) {
        if (more.length == 0) {
            return first;
        } else {
            HashSet<Character> seen = first.toCollection(HashSet::new);
            NonEmptyVectorBuilder<Character> builder = Vector.<Character>builder().addAll(first);
            for (ImmutableNonEmptyVector<Character> cs : more) {
                for (Character c : cs) {
                    if (!seen.contains(c)) {
                        builder = builder.add(c);
                        seen.add(c);
                    }
                }
            }
            return builder.build();
        }
    }

}
