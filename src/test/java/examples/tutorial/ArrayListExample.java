package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateArrayList;
import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListExample {
    public static void main(String[] args) {
        generateArrayList(generateInt(IntRange.from(1).to(10)))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //[3, 3, 8, 4, 4, 3, 7, 9]
        //[3, 2, 7, 3, 9, 1]
        //[9, 8, 10, 4, 3, 4, 9, 3, 1, 6, 7, 6, 3]
        //[3, 3, 10, 10]
        //[8, 1]
        //[6, 3, 7, 3, 5, 2, 3, 3, 6, 8, 1, 5, 9]
        //[]
        //[8, 7, 8]
        //[8, 2, 10, 5]
        //[9, 9, 8, 1, 2, 9]
    }
}
