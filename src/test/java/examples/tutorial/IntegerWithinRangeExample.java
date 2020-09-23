package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class IntegerWithinRangeExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(1).to(100))
                .run()
                .take(5)
                .forEach(System.out::println);
        // sample output:
        // 48
        // 82
        // 24
        // 41
        // 32
    }
}
