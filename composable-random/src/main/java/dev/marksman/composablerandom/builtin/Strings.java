package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.DiscreteDomain;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Instruction;
import dev.marksman.composablerandom.domain.Characters;

import java.util.ArrayList;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.Generator.generator;
import static dev.marksman.composablerandom.Instruction.aggregate;
import static dev.marksman.composablerandom.builtin.Choose.chooseOneFrom;

class Strings {

    static Generator<String> generateString() {
        return generateStringFromCharacters(Characters.asciiPrintable());
    }

    static Generator<String> generateString(int length, Generator<String> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g;
        else {
            return generator(aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    length, g.getInstruction()));
        }
    }

    static Generator<String> generateStringFromCharacters(Generator<Character> g) {
        return Generators.sized(size -> generateStringFromCharacters(size, g));
    }

    static Generator<String> generateStringFromCharacters(DiscreteDomain<Character> characters) {
        return Generators.sized(size -> generateStringFromCharacters(size, chooseOneFrom(characters)));
    }

    static Generator<String> generateStringFromCharacters(int length, Generator<Character> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g.fmap(Object::toString);
        else {
            return generator(aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    length, g.getInstruction()));
        }
    }

    @SafeVarargs
    static Generator<String> generateString(Generator<String> first, Generator<String>... more) {
        if (more.length == 0) return first;
        else {
            ArrayList<Instruction<String>> instructions = new ArrayList<>();
            instructions.add(first.getInstruction());
            for (Generator<String> g : more) {
                instructions.add(g.getInstruction());
            }
            return generator(aggregate(StringBuilder::new, StringBuilder::append, StringBuilder::toString,
                    instructions));
        }
    }

}
