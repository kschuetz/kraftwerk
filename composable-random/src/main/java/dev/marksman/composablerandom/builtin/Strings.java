package dev.marksman.composablerandom.builtin;

import dev.marksman.composablerandom.OldGenerator;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.State;

import static dev.marksman.composablerandom.OldGenerator.constant;
import static dev.marksman.composablerandom.Result.result;

class Strings {

    static OldGenerator<String> generateString(int length, OldGenerator<String> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g;
        else {
            return OldGenerator.contextDependent(s0 -> {
                State current = s0;
                StringBuilder output = new StringBuilder();
                for (int i = 0; i < length; i += 1) {
                    Result<State, String> next = g.run(current);
                    output.append(next.getValue());
                    current = next.getNextState();
                }
                return result(current, output.toString());
            });
        }
    }

    static OldGenerator<String> generateStringFromCharacters(OldGenerator<Character> g) {
        return Generators.sized(size -> generateStringFromCharacters(size, g));
    }

    static OldGenerator<String> generateStringFromCharacters(int length, OldGenerator<Character> g) {
        if (length <= 0) return constant("");
        else if (length == 1) return g.fmap(Object::toString);
        else {
            return OldGenerator.contextDependent(s0 -> {
                State current = s0;
                StringBuilder output = new StringBuilder();
                for (int i = 0; i < length; i += 1) {
                    Result<State, Character> next = g.run(current);
                    output.append(next.getValue());
                    current = next.getNextState();
                }
                return result(current, output.toString());
            });
        }
    }

    @SafeVarargs
    static OldGenerator<String> generateString(OldGenerator<String> first, OldGenerator<String>... more) {
        if (more.length == 0) return first;
        else {
            return OldGenerator.contextDependent(s0 -> {
                Result<State, String> current = first.run(s0);
                StringBuilder output = new StringBuilder(current.getValue());
                for (OldGenerator<String> g : more) {
                    current = g.run(current.getNextState());
                    output.append(current.getValue());
                }
                return result(current.getNextState(), output.toString());
            });
        }
    }

}
