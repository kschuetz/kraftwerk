package dev.marksman.composablerandom;

import dev.marksman.collectionviews.NonEmptyVector;
import dev.marksman.composablerandom.domain.Characters;

import java.util.ArrayList;
import java.util.Arrays;

import static dev.marksman.composablerandom.Generate.aggregate;
import static dev.marksman.composablerandom.Generate.constant;

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

}
