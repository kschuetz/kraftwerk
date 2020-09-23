package examples.tutorial;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class IntegerExample {
    public static void main(String[] args) {
        generateInt()
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // -806894999
        // -2088055255
        // 519165596
        // -247082188
        // 2073514567
    }
}
