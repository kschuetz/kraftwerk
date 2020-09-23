package examples.tutorial;

import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class ArrayListOfSizeRangeExample {
    public static void main(String[] args) {
        Generators.generateArrayListOfSize(IntRange.from(1).to(7),
                generateInt(IntRange.from(1).to(10)))
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //[1, 5, 5, 3, 4, 8]
        //[8, 4, 5, 6, 3]
        //[1, 2, 3]
        //[10, 2, 4, 4, 2]
        //[4, 10, 2, 7, 6, 3, 10]
        //[4, 3, 6, 1, 2, 6]
        //[3]
        //[9, 8, 10]
        //[4, 6, 3, 3, 1, 5, 4]
        //[2]
    }
}
