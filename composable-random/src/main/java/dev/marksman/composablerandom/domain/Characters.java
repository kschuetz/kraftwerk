package dev.marksman.composablerandom.domain;

import dev.marksman.composablerandom.DiscreteDomain;

import static dev.marksman.composablerandom.DiscreteDomain.discreteDomain;

public class Characters {

    public static DiscreteDomain<Character> alphaLower() {
        return discreteDomain(26, idx -> (char) (97 + idx));
    }

    public static DiscreteDomain<Character> alphaUpper() {
        return discreteDomain(26, idx -> (char) (65 + idx));
    }

    public static DiscreteDomain<Character> digits() {
        return discreteDomain(10, idx -> (char) (48 + idx));
    }

    public static DiscreteDomain<Character> asciiPrintable() {
        return discreteDomain(95, idx -> (char) (32 + idx));
    }

}
