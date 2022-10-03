package software.kes.kraftwerk;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.kraftwerk.constraints.IntRange;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static testsupport.Assert.assertForAll;
import static testsupport.StringIterable.stringIterable;

class StringsTest {
    @Nested
    @DisplayName("generateString")
    class GenerateString {
        @Test
        void constantLength() {
            assertForAll(Generators.generateString(0), s -> s.length() == 0);
            assertForAll(Generators.generateString(1), s -> s.length() == 1);
            assertForAll(Generators.generateString(5), s -> s.length() == 5);
            assertForAll(Generators.generateString(1000), s -> s.length() == 1000);
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> Generators.generateString(lengthRange).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()));
        }

        @Test
        void numberOfChunksRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> Generators.generateString(lengthRange, Generators.constant("x")).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()));
        }

        @Test
        void concatChunks() {
            assertForAll(1, Generators.generateString(Generators.constant("abc"), Generators.constant("def"), Generators.constant("ghi")), s -> s.equals("abcdefghi"));
        }
    }

    @Nested
    @DisplayName("generateStringFromCharacters")
    class GenerateStringFromCharacters {
        private final Generator<Character> charGen = Generators.chooseOneOfValues('a', 'b', 'c');
        private final ImmutableNonEmptyVector<Character> charDomain = Vector.of('a', 'b', 'c');

        private boolean allCharsInRange(String s) {
            return all(c -> c == 'a' || c == 'b' || c == 'c', stringIterable(s));
        }

        @Test
        void fromCharacterGenerator() {
            assertForAll(Generators.generateStringFromCharacters(charGen), this::allCharsInRange);
        }

        @Test
        void fromCharacterGeneratorConstantLength() {
            assertForAll(Generators.generateStringFromCharacters(0, charGen), s -> s.length() == 0);
            assertForAll(Generators.generateStringFromCharacters(1, charGen), s -> s.length() == 1 && allCharsInRange(s));
            assertForAll(Generators.generateStringFromCharacters(5, charGen), s -> s.length() == 5 && allCharsInRange(s));
            assertForAll(Generators.generateStringFromCharacters(100, charGen), s -> s.length() == 100 && allCharsInRange(s));
        }

        @Test
        void fromCharacterGeneratorLengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> Generators.generateStringFromCharacters(lengthRange, charGen).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && allCharsInRange(testCase._2()));
        }

        @Test
        void fromCharacterDomain() {
            assertForAll(Generators.generateStringFromCharacters(charDomain), this::allCharsInRange);
        }

        @Test
        void fromCharacterDomainConstantLength() {
            assertForAll(Generators.generateStringFromCharacters(0, charDomain), s -> s.length() == 0);
            assertForAll(Generators.generateStringFromCharacters(1, charDomain), s -> s.length() == 1 && allCharsInRange(s));
            assertForAll(Generators.generateStringFromCharacters(5, charDomain), s -> s.length() == 5 && allCharsInRange(s));
            assertForAll(Generators.generateStringFromCharacters(100, charDomain), s -> s.length() == 100 && allCharsInRange(s));
        }

        @Test
        void fromCharacterDomainLengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(0).to(100))
                    .flatMap(lengthRange -> Generators.generateStringFromCharacters(lengthRange, charDomain).fmap(s -> tuple(lengthRange, s)));
            assertForAll(generateTestCase, testCase -> testCase._1().includes(testCase._2().length()) && allCharsInRange(testCase._2()));
        }
    }

    @Nested
    @DisplayName("generateAlphaString")
    class GenerateAlphaString {
        @Test
        void variableLength() {
            assertForAll(Generators.generateAlphaString(), this::isValidAlphaString);
        }

        @Test
        void constantLength() {
            assertForAll(Generators.generateAlphaString(0), s -> s.length() == 0);
            assertForAll(Generators.generateAlphaString(1), s -> s.length() == 1 && isValidAlphaString(s));
            assertForAll(Generators.generateAlphaString(5), s -> s.length() == 5 && isValidAlphaString(s));
            assertForAll(Generators.generateAlphaString(100), s -> s.length() == 100 && isValidAlphaString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> Generators.generateAlphaString(lengthRange).fmap(s -> tuple(lengthRange, s)));
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
            assertForAll(Generators.generateAlphaUpperString(), this::isValidAlphaUpperString);
        }

        @Test
        void constantLength() {
            assertForAll(Generators.generateAlphaUpperString(0), s -> s.length() == 0);
            assertForAll(Generators.generateAlphaUpperString(1), s -> s.length() == 1 && isValidAlphaUpperString(s));
            assertForAll(Generators.generateAlphaUpperString(5), s -> s.length() == 5 && isValidAlphaUpperString(s));
            assertForAll(Generators.generateAlphaUpperString(100), s -> s.length() == 100 && isValidAlphaUpperString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> Generators.generateAlphaUpperString(lengthRange).fmap(s -> tuple(lengthRange, s)));
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
            assertForAll(Generators.generateAlphaLowerString(), this::isValidAlphaLowerString);
        }

        @Test
        void constantLength() {
            assertForAll(Generators.generateAlphaLowerString(0), s -> s.length() == 0);
            assertForAll(Generators.generateAlphaLowerString(1), s -> s.length() == 1 && isValidAlphaLowerString(s));
            assertForAll(Generators.generateAlphaLowerString(5), s -> s.length() == 5 && isValidAlphaLowerString(s));
            assertForAll(Generators.generateAlphaLowerString(100), s -> s.length() == 100 && isValidAlphaLowerString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> Generators.generateAlphaLowerString(lengthRange).fmap(s -> tuple(lengthRange, s)));
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
            assertForAll(Generators.generateAlphanumericString(), this::isValidAlphaNumericString);
        }

        @Test
        void constantLength() {
            assertForAll(Generators.generateAlphanumericString(0), s -> s.length() == 0);
            assertForAll(Generators.generateAlphanumericString(1), s -> s.length() == 1 && isValidAlphaNumericString(s));
            assertForAll(Generators.generateAlphanumericString(5), s -> s.length() == 5 && isValidAlphaNumericString(s));
            assertForAll(Generators.generateAlphanumericString(100), s -> s.length() == 100 && isValidAlphaNumericString(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> Generators.generateAlphanumericString(lengthRange).fmap(s -> tuple(lengthRange, s)));
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
            assertForAll(Generators.generateIdentifier(), this::isValidIdentifier);
        }

        @Test
        void constantLength() {
            assertForAll(Generators.generateIdentifier(0), s -> s.length() == 0);
            assertForAll(Generators.generateIdentifier(1), s -> s.length() == 1 && isValidIdentifier(s));
            assertForAll(Generators.generateIdentifier(5), s -> s.length() == 5 && isValidIdentifier(s));
            assertForAll(Generators.generateIdentifier(100), s -> s.length() == 100 && isValidIdentifier(s));
        }

        @Test
        void lengthRange() {
            Generator<Tuple2<IntRange, String>> generateTestCase = Generators.generateIntRange(IntRange.from(1).to(20))
                    .flatMap(lengthRange -> Generators.generateIdentifier(lengthRange).fmap(s -> tuple(lengthRange, s)));
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
            assertForAll(1, Generators.concatStrings(Vector.of(Generators.constant("abc"), Generators.constant("def"), Generators.constant("ghi"))),
                    s -> s.equals("abcdefghi"));
        }

        @Test
        void constantSeparator() {
            assertForAll(1, Generators.concatStrings(",", Vector.of(Generators.constant("abc"), Generators.constant("def"), Generators.constant("ghi"))),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, Generators.concatStrings("", Vector.of(Generators.constant("abc"), Generators.constant("def"), Generators.constant("ghi"))),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, Generators.concatStrings(",", Vector.of(Generators.constant(""), Generators.constant(""), Generators.constant(""))),
                    s -> s.equals(",,"));
            assertForAll(1, Generators.concatStrings(",", Vector.of(Generators.constant("abc"))),
                    s -> s.equals("abc"));
            assertForAll(1, Generators.concatStrings(",", Vector.empty()),
                    s -> s.equals(""));
        }

        @Test
        void generatedSeparator() {
            assertForAll(1, Generators.concatStrings(Generators.constant(","), Vector.of(Generators.constant("abc"), Generators.constant("def"), Generators.constant("ghi"))),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, Generators.concatStrings(Generators.constant(""), Vector.of(Generators.constant("abc"), Generators.constant("def"), Generators.constant("ghi"))),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, Generators.concatStrings(Generators.constant(","), Vector.of(Generators.constant(""), Generators.constant(""), Generators.constant(""))),
                    s -> s.equals(",,"));
            assertForAll(1, Generators.concatStrings(Generators.constant(","), Vector.of(Generators.constant("abc"))),
                    s -> s.equals("abc"));
            assertForAll(1, Generators.concatStrings(Generators.constant(","), Vector.empty()),
                    s -> s.equals(""));
        }
    }

    @Nested
    @DisplayName("concatMaybeStrings")
    class ConcatMaybeStrings {
        @Test
        void noSeparator() {
            assertForAll(1, Generators.concatMaybeStrings(Vector.of(Generators.constant("abc").just(), Generators.constant("def").just(), Generators.constant("ghi").just())),
                    s -> s.equals("abcdefghi"));
        }

        @Test
        void constantSeparator() {
            assertForAll(1, Generators.concatMaybeStrings(",", Vector.of(Generators.constant("abc").just(), Generators.constant("def").just(), Generators.constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, Generators.concatMaybeStrings("", Vector.of(Generators.constant("abc").just(), Generators.constant("def").just(), Generators.constant("ghi").just())),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, Generators.concatMaybeStrings(",", Vector.of(Generators.constant("").just(), Generators.constant("").just(), Generators.constant("").just())),
                    s -> s.equals(",,"));
            assertForAll(1, Generators.concatMaybeStrings(",", Vector.of(Generators.constant("abc").just())),
                    s -> s.equals("abc"));
            assertForAll(1, Generators.concatMaybeStrings(",", Vector.empty()),
                    s -> s.equals(""));
            assertForAll(1, Generators.concatMaybeStrings(",", Vector.of(Generators.constant("abc").just(), Generators.generateNothing(), Generators.constant("def").just(),
                            Generators.generateNothing(), Generators.constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, Generators.concatMaybeStrings(",", Vector.of(Generators.generateNothing())),
                    s -> s.equals(""));
        }

        @Test
        void generatedSeparator() {
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(","), Vector.of(Generators.constant("abc").just(), Generators.constant("def").just(), Generators.constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(""), Vector.of(Generators.constant("abc").just(), Generators.constant("def").just(), Generators.constant("ghi").just())),
                    s -> s.equals("abcdefghi"));
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(","), Vector.of(Generators.constant("").just(), Generators.constant("").just(), Generators.constant("").just())),
                    s -> s.equals(",,"));
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(","), Vector.of(Generators.constant("abc").just())),
                    s -> s.equals("abc"));
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(","), Vector.empty()),
                    s -> s.equals(""));
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(","), Vector.of(Generators.constant("abc").just(), Generators.generateNothing(), Generators.constant("def").just(),
                            Generators.generateNothing(), Generators.constant("ghi").just())),
                    s -> s.equals("abc,def,ghi"));
            assertForAll(1, Generators.concatMaybeStrings(Generators.constant(","), Vector.of(Generators.generateNothing())),
                    s -> s.equals(""));
        }
    }
}
