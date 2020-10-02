package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.constraints.IntRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.Generators.generateIntRange;
import static dev.marksman.kraftwerk.Generators.generateNothing;
import static dev.marksman.kraftwerk.Strings.concatMaybeStrings;
import static dev.marksman.kraftwerk.Strings.concatStrings;
import static dev.marksman.kraftwerk.Strings.generateAlphaLowerString;
import static dev.marksman.kraftwerk.Strings.generateAlphaString;
import static dev.marksman.kraftwerk.Strings.generateAlphaUpperString;
import static dev.marksman.kraftwerk.Strings.generateAlphanumericString;
import static dev.marksman.kraftwerk.Strings.generateIdentifier;
import static dev.marksman.kraftwerk.Strings.generateString;
import static dev.marksman.kraftwerk.Strings.generateStringFromCharacters;
import static testsupport.Assert.assertForAll;
import static testsupport.StringIterable.stringIterable;

class StringsTest {
    @Nested
    @DisplayName("generateString")
    class GenerateString {
        @Test
        void constantLength() {
            assertForAll(generateString(0), s -> s.length() == 0);
            assertForAll(generateString(1), s -> s.length() == 1);
            assertForAll(generateString(5), s -> s.length() == 5);
            assertForAll(generateString(1000), s -> s.length() == 1000);
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> generateString(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()));
        }

        @Test
        void numberOfChunksRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> generateString(lengthRange, constant("x")).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()));
        }

        @Test
        void concatChunks() {
            assertForAll(1, generateString(constant("abc"), constant("def"), constant("ghi")), s -> s.equals("abcdefghi"));
        }
    }

    @Nested
    @DisplayName("generateStringFromCharacters")
    class GenerateStringFromCharacters {
        private final Generator<Character> charGen = chooseOneOfValues('a', 'b', 'c');
        private final ImmutableNonEmptyVector<Character> charDomain = Vector.of('a', 'b', 'c');

        private boolean allCharsInRange(String s) {
            return all(c -> c == 'a' || c == 'b' || c == 'c', stringIterable(s));
        }

        @Test
        void fromCharacterGenerator() {
            assertForAll(generateStringFromCharacters(charGen), this::allCharsInRange);
        }

        @Test
        void fromCharacterGeneratorConstantLength() {
            assertForAll(generateStringFromCharacters(0, charGen), s -> s.length() == 0);
            assertForAll(generateStringFromCharacters(1, charGen), s -> s.length() == 1 && allCharsInRange(s));
            assertForAll(generateStringFromCharacters(5, charGen), s -> s.length() == 5 && allCharsInRange(s));
            assertForAll(generateStringFromCharacters(100, charGen), s -> s.length() == 100 && allCharsInRange(s));
        }

        @Test
        void fromCharacterGeneratorLengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> generateStringFromCharacters(lengthRange, charGen).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && allCharsInRange(testCase._2()));
        }

        @Test
        void fromCharacterDomain() {
            assertForAll(generateStringFromCharacters(charDomain), this::allCharsInRange);
        }

        @Test
        void fromCharacterDomainConstantLength() {
            assertForAll(generateStringFromCharacters(0, charDomain), s -> s.length() == 0);
            assertForAll(generateStringFromCharacters(1, charDomain), s -> s.length() == 1 && allCharsInRange(s));
            assertForAll(generateStringFromCharacters(5, charDomain), s -> s.length() == 5 && allCharsInRange(s));
            assertForAll(generateStringFromCharacters(100, charDomain), s -> s.length() == 100 && allCharsInRange(s));
        }

        @Test
        void fromCharacterDomainLengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> generateStringFromCharacters(lengthRange, charDomain).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && allCharsInRange(testCase._2()));
        }
    }

    @Nested
    @DisplayName("generateAlphaString")
    class GenerateAlphaString {
        @Test
        void variableLength() {
            assertForAll(generateAlphaString(), this::isValidAlphaString);
        }

        @Test
        void constantLength() {
            assertForAll(generateAlphaString(0), s -> s.length() == 0);
            assertForAll(generateAlphaString(1), s -> s.length() == 1 && isValidAlphaString(s));
            assertForAll(generateAlphaString(5), s -> s.length() == 5 && isValidAlphaString(s));
            assertForAll(generateAlphaString(100), s -> s.length() == 100 && isValidAlphaString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> generateAlphaString(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && isValidAlphaString(testCase._2()));
        }

        private boolean isValidAlphaString(String s) {
            return all(c -> (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'), stringIterable(s));
        }
    }

    @Nested
    @DisplayName("generateAlphaUpperString")
    class GenerateAlphaUpperString {
        @Test
        void variableLength() {
            assertForAll(generateAlphaUpperString(), this::isValidAlphaUpperString);
        }

        @Test
        void constantLength() {
            assertForAll(generateAlphaUpperString(0), s -> s.length() == 0);
            assertForAll(generateAlphaUpperString(1), s -> s.length() == 1 && isValidAlphaUpperString(s));
            assertForAll(generateAlphaUpperString(5), s -> s.length() == 5 && isValidAlphaUpperString(s));
            assertForAll(generateAlphaUpperString(100), s -> s.length() == 100 && isValidAlphaUpperString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> generateAlphaUpperString(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && isValidAlphaUpperString(testCase._2()));
        }

        private boolean isValidAlphaUpperString(String s) {
            return all(c -> (c >= 'A' && c <= 'Z'), stringIterable(s));
        }
    }

    @Nested
    @DisplayName("generateAlphaLowerString")
    class GenerateAlphaLowerString {
        @Test
        void variableLength() {
            assertForAll(generateAlphaLowerString(), this::isValidAlphaLowerString);
        }

        @Test
        void constantLength() {
            assertForAll(generateAlphaLowerString(0), s -> s.length() == 0);
            assertForAll(generateAlphaLowerString(1), s -> s.length() == 1 && isValidAlphaLowerString(s));
            assertForAll(generateAlphaLowerString(5), s -> s.length() == 5 && isValidAlphaLowerString(s));
            assertForAll(generateAlphaLowerString(100), s -> s.length() == 100 && isValidAlphaLowerString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> generateAlphaLowerString(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && isValidAlphaLowerString(testCase._2()));
        }

        private boolean isValidAlphaLowerString(String s) {
            return all(c -> (c >= 'a' && c <= 'z'), stringIterable(s));
        }
    }

    @Nested
    @DisplayName("generateAlphaNumericString")
    class GenerateAlphanumericString {
        @Test
        void variableLength() {
            assertForAll(Strings.generateAlphanumericString(), this::isValidAlphaNumericString);
        }

        @Test
        void constantLength() {
            assertForAll(Strings.generateAlphanumericString(0), s -> s.length() == 0);
            assertForAll(Strings.generateAlphanumericString(1), s -> s.length() == 1 && isValidAlphaNumericString(s));
            assertForAll(Strings.generateAlphanumericString(5), s -> s.length() == 5 && isValidAlphaNumericString(s));
            assertForAll(Strings.generateAlphanumericString(100), s -> s.length() == 100 && isValidAlphaNumericString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> generateAlphanumericString(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && isValidAlphaNumericString(testCase._2()));
        }

        private boolean isValidAlphaNumericString(String s) {
            return all(c -> (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'), stringIterable(s));
        }
    }

    @Nested
    @DisplayName("generateIdentifier")
    class GenerateIdentifier {
        @Test
        void variableLength() {
            assertForAll(generateIdentifier(), this::isValidIdentifier);
        }

        @Test
        void constantLength() {
            assertForAll(generateIdentifier(0), s -> s.length() == 0);
            assertForAll(generateIdentifier(1), s -> s.length() == 1 && isValidIdentifier(s));
            assertForAll(generateIdentifier(5), s -> s.length() == 5 && isValidIdentifier(s));
            assertForAll(generateIdentifier(100), s -> s.length() == 100 && isValidIdentifier(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> generateIdentifier(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && isValidIdentifier(testCase._2()));
        }

        private boolean isValidIdentifier(String s) {
            if (s.length() < 1) return false;
            return Character.isJavaIdentifierStart(s.charAt(0)) &&
                    all(Character::isJavaIdentifierPart, stringIterable(s.substring(1)));
        }
    }

    @Nested
    @DisplayName("concatStrings")
    class ConcatStrings {
        @Test
        void noSeparator() {
            assertForAll(1, concatStrings(Vector.of(constant("abc"), constant("def"), constant("ghi"))),
                    s -> s.equals("abcdefghi"));
        }

        @Test
        void constantSeparator() {
            assertForAll(1, concatStrings(",", Vector.of(constant("abc"), constant("def"), constant("ghi"))),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, concatStrings("", Vector.of(constant("abc"), constant("def"), constant("ghi"))),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, concatStrings(",", Vector.of(constant(""), constant(""), constant(""))),
                    s -> s.equals(",,"));
            assertForAll(1, concatStrings(",", Vector.of(constant("abc"))),
                    s -> s.equals("abc"));
            assertForAll(1, concatStrings(",", Vector.empty()),
                    s -> s.equals(""));
        }

        @Test
        void generatedSeparator() {
            assertForAll(1, concatStrings(constant(","), Vector.of(constant("abc"), constant("def"), constant("ghi"))),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, concatStrings(constant(""), Vector.of(constant("abc"), constant("def"), constant("ghi"))),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, concatStrings(constant(","), Vector.of(constant(""), constant(""), constant(""))),
                    s -> s.equals(",,"));
            assertForAll(1, concatStrings(constant(","), Vector.of(constant("abc"))),
                    s -> s.equals("abc"));
            assertForAll(1, concatStrings(constant(","), Vector.empty()),
                    s -> s.equals(""));
        }
    }

    @Nested
    @DisplayName("concatMaybeStrings")
    class ConcatMaybeStrings {
        @Test
        void noSeparator() {
            assertForAll(1, concatMaybeStrings(Vector.of(constant("abc").just(), constant("def").just(), constant("ghi").just())),
                    s -> s.equals("abcdefghi"));
        }

        @Test
        void constantSeparator() {
            assertForAll(1, concatMaybeStrings(",", Vector.of(constant("abc").just(), constant("def").just(), constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, concatMaybeStrings("", Vector.of(constant("abc").just(), constant("def").just(), constant("ghi").just())),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, concatMaybeStrings(",", Vector.of(constant("").just(), constant("").just(), constant("").just())),
                    s -> s.equals(",,"));
            assertForAll(1, concatMaybeStrings(",", Vector.of(constant("abc").just())),
                    s -> s.equals("abc"));
            assertForAll(1, concatMaybeStrings(",", Vector.empty()),
                    s -> s.equals(""));
            assertForAll(1, concatMaybeStrings(",", Vector.of(constant("abc").just(), generateNothing(), constant("def").just(),
                    generateNothing(), constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, concatMaybeStrings(",", Vector.of(generateNothing())),
                    s -> s.equals(""));
        }

        @Test
        void generatedSeparator() {
            assertForAll(1, concatMaybeStrings(constant(","), Vector.of(constant("abc").just(), constant("def").just(), constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, concatMaybeStrings(constant(""), Vector.of(constant("abc").just(), constant("def").just(), constant("ghi").just())),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, concatMaybeStrings(constant(","), Vector.of(constant("").just(), constant("").just(), constant("").just())),
                    s -> s.equals(",,"));
            assertForAll(1, concatMaybeStrings(constant(","), Vector.of(constant("abc").just())),
                    s -> s.equals("abc"));
            assertForAll(1, concatMaybeStrings(constant(","), Vector.empty()),
                    s -> s.equals(""));
            assertForAll(1, concatMaybeStrings(constant(","), Vector.of(constant("abc").just(), generateNothing(), constant("def").just(),
                    generateNothing(), constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, concatMaybeStrings(constant(","), Vector.of(generateNothing())),
                    s -> s.equals(""));
        }
    }
}
