package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListPostfixExample {
    public static void main(String[] args) {
        generateInt(IntRange.from(1).to(10))
                .arrayList()
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        //[3, 3, 8, 8, 2, 4, 5, 2, 1, 8, 2, 1, 1, 4, 9]
        //[2, 5, 8]
        //[8, 2, 10, 3, 7, 9, 4, 1]
        //[1, 5, 5, 2]
        //[3, 4, 3, 10, 5, 6]
    }
}
