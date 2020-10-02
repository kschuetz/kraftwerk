package dev.marksman.kraftwerk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.marksman.kraftwerk.Generators.generateAlphaChar;
import static dev.marksman.kraftwerk.Generators.generateAlphaLowerChar;
import static dev.marksman.kraftwerk.Generators.generateAlphaUpperChar;
import static dev.marksman.kraftwerk.Generators.generateAlphanumericChar;
import static dev.marksman.kraftwerk.Generators.generateAsciiPrintableChar;
import static dev.marksman.kraftwerk.Generators.generateControlChar;
import static dev.marksman.kraftwerk.Generators.generateNumericChar;
import static dev.marksman.kraftwerk.Generators.generatePunctuationChar;
import static testsupport.Assert.assertForAll;

public class CharactersTest {
    @Nested
    @DisplayName("generateAlphaChar")
    class GenerateAlphaChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateAlphaChar(), c -> isAlphaLower(c) || isAlphaUpper(c));
        }
    }

    @Nested
    @DisplayName("generateAlphaUpperChar")
    class GenerateAlphaUpperChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateAlphaUpperChar(), CharactersTest::isAlphaUpper);
        }
    }

    @Nested
    @DisplayName("generateAlphaLowerChar")
    class GenerateAlphaLowerChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateAlphaLowerChar(), CharactersTest::isAlphaLower);
        }
    }

    @Nested
    @DisplayName("generateAlphanumericChar")
    class GenerateAlphanumericChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateAlphanumericChar(), c -> isAlphaLower(c) || isAlphaUpper(c) || isNumeric(c));
        }
    }

    @Nested
    @DisplayName("generateNumericChar")
    class GenerateNumericChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateNumericChar(), CharactersTest::isNumeric);
        }
    }

    @Nested
    @DisplayName("generatePunctuationChar")
    class GeneratePunctuationChar {
        @Test
        void alwaysInRange() {
            assertForAll(generatePunctuationChar(), c -> !(c == ' ' || isAlphaLower(c) || isAlphaUpper(c) ||
                    isNumeric(c) || isControlChar(c)));
        }
    }

    @Nested
    @DisplayName("generateAsciiPrintableChar")
    class GenerateAsciiPrintableChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateAsciiPrintableChar(), c -> !isControlChar(c));
        }
    }

    @Nested
    @DisplayName("generateControlChar")
    class GenerateControlChar {
        @Test
        void alwaysInRange() {
            assertForAll(generateControlChar(), CharactersTest::isControlChar);
        }
    }

    private static boolean isAlphaLower(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isAlphaUpper(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isControlChar(char c) {
        return c < 32;
    }
}
