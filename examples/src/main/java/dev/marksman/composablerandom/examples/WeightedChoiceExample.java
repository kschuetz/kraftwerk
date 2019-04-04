package dev.marksman.composablerandom.examples;

import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.domain.Characters;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.builtin.Generators.*;

public class WeightedChoiceExample {

    public static void main(String[] args) {
        Generator<Choice8<Integer, Double, Float, Boolean, Long, Byte, Short, Character>> primitiveGenerator =
                choiceBuilder(generateInt())
                        .or(generateDouble())
                        .or(1, generateFloat())
                        .or(1, generateBoolean())
                        .or(1, generateLong())
                        .or(1, generateByte())
                        .or(1, generateShort())
                        .or(1, chooseOneFrom(Characters.asciiPrintable()))
                        .toGenerator();

        streamFrom(primitiveGenerator).next(100).forEach(System.out::println);
    }
}
