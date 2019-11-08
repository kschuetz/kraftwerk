package examples;

import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.domain.Characters;

import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;

public class WeightedChoiceExample {

    private static void example1() {
        Generator<Choice8<Integer, Double, Float, Boolean, Long, Byte, Short, Character>> primitiveGenerator =
                Generators.choiceBuilder(Generators.generateInt())
                        .or(Generators.generateDouble())
                        .or(Generators.generateFloat())
                        .or(Generators.generateBoolean())
                        .or(Generators.generateLong())
                        .or(Generators.generateByte())
                        .or(Generators.generateShort())
                        .or(Generators.chooseOneFromDomain(Characters.asciiPrintable()))
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
