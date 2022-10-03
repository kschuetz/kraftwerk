package examples;

import static software.kes.kraftwerk.Generators.generateDouble;
import static software.kes.kraftwerk.Generators.generateDoubleFractional;
import static software.kes.kraftwerk.Generators.generateFloat;
import static software.kes.kraftwerk.Generators.generateFloatFractional;
import static software.kes.kraftwerk.Generators.generateTuple;

public class FloatingPointExample {
    public static void main(String[] args) {
        generateTuple(generateDoubleFractional(),
                generateDoubleFractional().withNaNs(),
                generateDouble().withInfinities(),
                generateFloatFractional(),
                generateFloatFractional().withNaNs(),
                generateFloat().withInfinities())
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
