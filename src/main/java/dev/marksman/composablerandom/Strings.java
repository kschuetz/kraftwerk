package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes;
import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.composablerandom.domain.Characters;
import dev.marksman.enhancediterables.EnhancedIterable;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Intersperse.intersperse;
import static dev.marksman.composablerandom.Generate.aggregate;
import static dev.marksman.composablerandom.Generate.constant;
import static dev.marksman.composablerandom.Sequence.sequence;
import static dev.marksman.enhancediterables.EnhancedIterable.enhance;

class Strings {

    static Generate<String> generateString() {
        return generateStringFromCharacters(Characters.asciiPrintable());
    }

    static Generate<String> generateString(int length, Generate<String> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g;
        else {
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    length, g);
        }
    }

    static Generate<String> generateStringFromCharacters(Generate<Character> g) {
        return Generate.sized(size -> generateStringFromCharacters(size, g));
    }

    static Generate<String> generateStringFromCharacters(NonEmptyVector<Character> characters) {
        return Generate.sized(size -> generateStringFromCharacters(size, Choose.chooseOneFromDomain(characters)));
    }

    static Generate<String> generateStringFromCharacters(int length, Generate<Character> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g.fmap(Object::toString);
        else {
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    length, g);
        }
    }

    static Generate<String> generateStringFromCharacters(int length, NonEmptyVector<Character> characters) {
        return generateStringFromCharacters(length, Choose.chooseOneFromDomain(characters));
    }

    @SafeVarargs
    static Generate<String> generateString(Generate<String> first, Generate<String>... more) {
        if (more.length == 0) return first;
        else {
            ArrayList<Generate<String>> generators = new ArrayList<>();
            generators.add(first);
            generators.addAll(Arrays.asList(more));
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    generators);
        }
    }

    static Generate<String> concatStrings(Generate<String> separator, Iterable<Generate<String>> components) {
        if (!components.iterator().hasNext()) {
            return constant("");
        } else {
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    intersperse(separator, components));
        }
    }

    static Generate<String> concatStrings(String separator, Iterable<Generate<String>> components) {
        return concatStrings(constant(separator), components);
    }

    static Generate<String> concatStrings(Iterable<Generate<String>> components) {
        return concatStrings(constant(""), components);
    }

    static Generate<String> concatMaybeStrings(Generate<String> separator, Iterable<Generate<Maybe<String>>> components) {
        if (!components.iterator().hasNext()) {
            return constant("");
        } else {
            Generate<EnhancedIterable<Generate<String>>> step1 = sequence(components)
                    .fmap(cs ->
                            enhance(CatMaybes.catMaybes(cs))
                                    .fmap(Generate::constant)
                                    .intersperse(separator));

            return step1.flatMap(ss ->
                    aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString, ss));

        }
    }

    public static Generate<String> concatMaybeStrings(String separator, Iterable<Generate<Maybe<String>>> components) {
        return concatMaybeStrings(constant(separator), components);
    }

    public static Generate<String> concatMaybeStrings(Iterable<Generate<Maybe<String>>> components) {
        return concatMaybeStrings(constant(""), components);
    }

}
