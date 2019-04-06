package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.domain.Characters;

import java.util.ArrayList;
import java.util.Arrays;

import static dev.marksman.composablerandom.Generator.aggregate;
import static dev.marksman.composablerandom.Generator.constant;

class Strings {

    static Generator<String> generateString() {
        return generateStringFromCharacters(Characters.asciiPrintable());
    }

    static Generator<String> generateString(int length, Generator<String> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g;
        else {
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    length, g);
        }
    }

    static Generator<String> generateStringFromCharacters(Generator<Character> g) {
        return Generators.sized(size -> generateStringFromCharacters(size, g));
    }

    static Generator<String> generateStringFromCharacters(DiscreteDomain<Character> characters) {
        return Generators.sized(size -> generateStringFromCharacters(size, Choose.chooseOneFromDomain(characters)));
    }

    static Generator<String> generateStringFromCharacters(int length, Generator<Character> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g.fmap(Object::toString);
        else {
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    length, g);
        }
    }

    @SafeVarargs
    static Generator<String> generateString(Generator<String> first, Generator<String>... more) {
        if (more.length == 0) return first;
        else {
            ArrayList<Generator<String>> generators = new ArrayList<>();
            generators.add(first);
            generators.addAll(Arrays.asList(more));
            return aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    generators);
        }
    }

}
