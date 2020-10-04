package examples.tutorial;

import com.jnape.palatable.lambda.adt.choice.Choice8;
import dev.marksman.kraftwerk.Generator;

import static dev.marksman.kraftwerk.Generators.choiceBuilder;
import static dev.marksman.kraftwerk.Generators.generateAsciiPrintableChar;
import static dev.marksman.kraftwerk.Generators.generateBoolean;
import static dev.marksman.kraftwerk.Generators.generateByte;
import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateFloatFractional;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateShort;

public class WeightedChoiceExample {
    public static void main(String[] args) {
        Generator<Choice8<Integer, Double, Float, Boolean, Long, Byte, Short, Character>> primitiveGenerator =
                choiceBuilder(generateInt().weighted(2))
                        .or(generateDoubleFractional())
                        .or(generateFloatFractional())
                        .or(generateBoolean())
                        .or(generateLong().weighted(2))
                        .or(generateByte())
                        .or(generateShort())
                        .or(generateAsciiPrintableChar())
                        .toGenerator();

        primitiveGenerator
                .run()
                .take(20)
                .forEach(System.out::println);

        // sample output:
        //Choice8{a=67463646}
        //Choice8{c=0.3798052}
        //Choice8{c=0.5343417}
        //Choice8{e=8542724707820389757}
        //Choice8{e=7723229941486178995}
        //Choice8{a=983401700}
        //Choice8{a=2065183438}
        //Choice8{e=4697885099367790210}
        //Choice8{b=0.42306257966421357}
        //Choice8{e=8807970083702066543}
        //Choice8{a=-1526959351}
        //Choice8{f=-105}
        //Choice8{d=true}
        //Choice8{g=26791}
        //Choice8{f=-81}
        //Choice8{h=0}
        //Choice8{h=#}
        //Choice8{e=6994438299090618730}
        //Choice8{h=,}
        //Choice8{a=-99648217}
    }
}
