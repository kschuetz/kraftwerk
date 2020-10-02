package dev.marksman.kraftwerk.domain;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.collectionviews.NonEmptyVectorBuilder;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generators;

import java.util.HashSet;

/**
 * A collection of useful domains of characters.  These can be used as inputs to {@link Generators#chooseOneValueFromDomain(NonEmptyVector)},
 * among other methods.
 */
public final class Characters {
    private Characters() {

    }

    private static final ImmutableNonEmptyVector<Character> ALPHA_LOWER = NonEmptyVector.lazyFill(26, idx -> (char) (97 + idx));
    private static final ImmutableNonEmptyVector<Character> ALPHA_UPPER = NonEmptyVector.lazyFill(26, idx -> (char) (65 + idx));
    private static final ImmutableNonEmptyVector<Character> ALPHA = union(ALPHA_LOWER, ALPHA_UPPER);
    private static final ImmutableNonEmptyVector<Character> NUMERIC = NonEmptyVector.lazyFill(10, idx -> (char) (48 + idx));
    private static final ImmutableNonEmptyVector<Character> ALPHA_NUMERIC = union(ALPHA, NUMERIC);
    private static final ImmutableNonEmptyVector<Character> ASCII_PRINTABLE = NonEmptyVector.lazyFill(95, idx -> (char) (32 + idx));
    private static final ImmutableNonEmptyVector<Character> PUNCTUATION = Vector.of('!', '"', '#', '$', '%', '&',
            '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_',
            '`', '{', '|', '}');
    private static final ImmutableNonEmptyVector<Character> CONTROL_CHAR = NonEmptyVector.lazyFill(32, idx -> (char) (0 + idx));

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

    public static ImmutableNonEmptyVector<Character> alphanumeric() {
        return ALPHA_NUMERIC;
    }

    public static ImmutableNonEmptyVector<Character> punctuation() {
        return PUNCTUATION;
    }

    public static ImmutableNonEmptyVector<Character> controlChar() {
        return CONTROL_CHAR;
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
