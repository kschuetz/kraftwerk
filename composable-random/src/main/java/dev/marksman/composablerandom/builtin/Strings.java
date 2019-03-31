package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.Instruction;

import java.util.ArrayList;

import static dev.marksman.composablerandom.Generator.constant;
import static dev.marksman.composablerandom.Generator.generator;
import static dev.marksman.composablerandom.Instruction.aggregate;

class Strings {

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
