package dev.marksman.kraftwerk;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.enhancediterables.EnhancedIterable;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.domain.Characters;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Intersperse.intersperse;
import static dev.marksman.enhancediterables.EnhancedIterable.enhance;
import static dev.marksman.kraftwerk.Generators.aggregate;
import static dev.marksman.kraftwerk.Generators.constant;
import static dev.marksman.kraftwerk.Primitives.generateSize;
import static dev.marksman.kraftwerk.Sequence.sequence;
import static dev.marksman.kraftwerk.aggregator.Aggregators.charAggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.maybeStringAggregator;
import static dev.marksman.kraftwerk.aggregator.Aggregators.stringAggregator;

final class Strings {

    private Strings() {
    }

    static Generator<String> generateString() {
        return generateStringFromCharacters(Characters.asciiPrintable());
    }

    static Generator<String> generateString(int length) {
        return generateStringFromCharacters(length, Characters.asciiPrintable());
    }

    static Generator<String> generateString(IntRange lengthRange) {
        return generateStringFromCharacters(lengthRange, Characters.asciiPrintable());
    }

    static Generator<String> generateString(int length, Generator<String> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g;
        else {
            return aggregate(stringAggregator(), length, g);
        }
    }

    static Generator<String> generateString(IntRange lengthRange, Generator<String> g) {
        if (lengthRange.minInclusive() <= 0) {
            if (lengthRange.maxInclusive() <= 0) {
                return constant("");
            } else {
                lengthRange = lengthRange.withMinInclusive(0);
            }
        }
        if (lengthRange.minInclusive() == 1 && lengthRange.maxInclusive() == 1) {
            return g;
        } else {
            return aggregate(stringAggregator(), lengthRange, g);
        }
    }

    static Generator<String> generateStringFromCharacters(Generator<Character> g) {
        return Generators.sized(size -> generateStringFromCharacters(size, g));
    }

    static Generator<String> generateStringFromCharacters(NonEmptyVector<Character> characters) {
        return Generators.sized(size -> generateStringFromCharacters(size, Choose.chooseOneValueFromDomain(characters)));
    }

    static Generator<String> generateStringFromCharacters(int length, Generator<Character> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g.fmap(Object::toString);
        else {
            return aggregate(charAggregator(), length, g);
        }
    }

    static Generator<String> generateStringFromCharacters(IntRange lengthRange, Generator<Character> g) {
        if (lengthRange.minInclusive() <= 0) {
            if (lengthRange.maxInclusive() <= 0) {
                return constant("");
            } else {
                lengthRange = lengthRange.withMinInclusive(0);
            }
        }
        if (lengthRange.minInclusive() == 1 && lengthRange.maxInclusive() == 1) {
            return g.fmap(Object::toString);
        } else {
            return aggregate(charAggregator(), lengthRange, g);
        }
    }

    static Generator<String> generateStringFromCharacters(int length, NonEmptyVector<Character> characters) {
        return generateStringFromCharacters(length, Choose.chooseOneValueFromDomain(characters));
    }

    static Generator<String> generateStringFromCharacters(IntRange lengthRange, NonEmptyVector<Character> characters) {
        return generateStringFromCharacters(lengthRange, Choose.chooseOneValueFromDomain(characters));
    }

    @SafeVarargs
    static Generator<String> generateString(Generator<String> first, Generator<String>... more) {
        if (more.length == 0) return first;
        else {
            ArrayList<Generator<String>> generators = new ArrayList<>();
            generators.add(first);
            generators.addAll(Arrays.asList(more));
            return aggregate(stringAggregator(), generators);
        }
    }

    static Generator<String> generateIdentifier() {
        return Generators.sizedMinimum(1, Strings::generateIdentifier);
    }

    static Generator<String> generateIdentifier(int length) {
        if (length < 1) {
            return constant("");
        } else {
            Generator<String> firstChar = generateStringFromCharacters(1, Characters.alphaLower());
            if (length == 1) {
                return firstChar;
            } else {
                return generateString(firstChar, generateStringFromCharacters(length - 1, Characters.alphaNumeric()));
            }
        }
    }

    static Generator<String> generateIdentifier(IntRange lengthRange) {
        return generateLengthForString(lengthRange).flatMap(Strings::generateIdentifier);
    }

    static Generator<String> concatStrings(Generator<String> separator, Iterable<Generator<String>> components) {
        if (!components.iterator().hasNext()) {
            return constant("");
        } else {
            return aggregate(stringAggregator(), intersperse(separator, components));
        }
    }

    static Generator<String> concatStrings(String separator, Iterable<Generator<String>> components) {
        return concatStrings(constant(separator), components);
    }

    static Generator<String> concatStrings(Iterable<Generator<String>> components) {
        if (!components.iterator().hasNext()) {
            return constant("");
        } else {
            return aggregate(stringAggregator(), components);
        }
    }

    static Generator<String> concatMaybeStrings(Generator<String> separator, Iterable<Generator<Maybe<String>>> components) {
        if (!components.iterator().hasNext()) {
            return constant("");
        } else {
            Generator<EnhancedIterable<Generator<String>>> step1 = sequence(components)
                    .fmap(cs ->
                            enhance(CatMaybes.catMaybes(cs))
                                    .fmap(Generators::constant)
                                    .intersperse(separator));

            return step1.flatMap(ss ->
                    aggregate(stringAggregator(), ss));

        }
    }

    static Generator<String> concatMaybeStrings(String separator, Iterable<Generator<Maybe<String>>> components) {
        return concatMaybeStrings(constant(separator), components);
    }

    static Generator<String> concatMaybeStrings(Iterable<Generator<Maybe<String>>> components) {
        if (!components.iterator().hasNext()) {
            return constant("");
        } else {
            return aggregate(maybeStringAggregator(), components);
        }
    }

    static Generator<Integer> generateLengthForString(IntRange lengthRange) {
        return generateSize(lengthRange);
    }
}
