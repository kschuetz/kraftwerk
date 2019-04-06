package dev.marksman.composablerandom.examples;

import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.composablerandom.Generator;
import dev.marksman.composablerandom.domain.Characters;

import static dev.marksman.composablerandom.builtin.Generators.*;
import static dev.marksman.composablerandom.legacy.OldGeneratedStream.streamFrom;

public class WeightedChoiceExample {

    private static void example1() {
        Generator<Choice8<Integer, Double, Float, Boolean, Long, Byte, Short, Character>> primitiveGenerator =
                choiceBuilder(generateInt())
                        .or(generateDouble())
                        .or(generateFloat())
                        .or(generateBoolean())
                        .or(generateLong())
                        .or(generateByte())
                        .or(generateShort())
                        .or(chooseOneFromDomain(Characters.asciiPrintable()))
                        .toGenerator();

        streamFrom(primitiveGenerator).next(100).forEach(System.out::println);
    }

    public static void main(String[] args) {
        example1();

//        ChoiceBuilder2<Integer, Boolean> c1 = choiceBuilder(generateInt()).or(generateBoolean());
//
//        ChoiceBuilder3<Integer, Boolean, Float> c2 = c1.or(1, generateFloat());
//
//        ChoiceBuilder4<Integer, Boolean, Float, Long> c3 = c2.or(1, generateLong());
//
//        streamFrom(c3.toGenerator()).next(100).forEach(System.out::println);
    }
}
