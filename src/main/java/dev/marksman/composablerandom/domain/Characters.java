package dev.marksman.composablerandom.domain;

import dev.marksman.composablerandom.Domain;

import static dev.marksman.composablerandom.Domain.domain;

public class Characters {

    public static Domain<Character> alphaLower() {
        return domain(26, idx -> (char) (97 + idx));
    }

    public static Domain<Character> alphaUpper() {
        return domain(26, idx -> (char) (65 + idx));
    }

    public static Domain<Character> digits() {
        return domain(10, idx -> (char) (48 + idx));
    }

    public static Domain<Character> asciiPrintable() {
        return domain(95, idx -> (char) (32 + idx));
    }

}
