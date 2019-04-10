package dev.marksman.discretedomain.builtin;

import dev.marksman.discretedomain.SmallDomain;

import static dev.marksman.discretedomain.SmallDomain.smallDomain;

public class Characters {

    public static SmallDomain<Character> alphaLower() {
        return smallDomain(26, idx -> (char) (97 + idx));
    }

    public static SmallDomain<Character> alphaUpper() {
        return smallDomain(26, idx -> (char) (65 + idx));
    }

    public static SmallDomain<Character> digits() {
        return smallDomain(10, idx -> (char) (48 + idx));
    }

    public static SmallDomain<Character> asciiPrintable() {
        return smallDomain(95, idx -> (char) (32 + idx));
    }

}
