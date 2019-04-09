package dev.marksman.discretedomain.builtin;

import dev.marksman.discretedomain.DiscreteDomain;

import static dev.marksman.discretedomain.DiscreteDomain.discreteDomain;

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
