package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class MappingExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(0).to(100))
                .fmap(n -> n * 1000)
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // 64000
        // 34000
        // 60000
        // 58000
        // 61000
    }
}
