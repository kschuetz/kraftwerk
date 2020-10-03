package examples;

import static dev.marksman.kraftwerk.Generators.generateDouble;
import static dev.marksman.kraftwerk.Generators.generateDoubleFractional;
import static dev.marksman.kraftwerk.Generators.generateFloat;
import static dev.marksman.kraftwerk.Generators.generateFloatFractional;
import static dev.marksman.kraftwerk.Generators.generateTuple;

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
