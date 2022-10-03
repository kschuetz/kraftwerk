package examples.tutorial;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import software.kes.kraftwerk.Generator;

import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateString;
import static software.kes.kraftwerk.Generators.generateTuple;

public class CombiningTwoGenerators {
    public static void main(String[] args) {
        Generator<Tuple2<Integer, String>> generator = generateTuple(generateInt(), generateString());
        generator.run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // HList{ 1085224429 :: Sp`b}tM#@E|r }
        // HList{ -354995125 :: Zh:b4 }
        // HList{ -41728349 :: C8T[8aD }
        // HList{ 981101761 :: 'z }
        // HList{ -1434780244 :: uX }
    }
}
