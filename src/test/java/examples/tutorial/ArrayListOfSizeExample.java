package examples.tutorial;

import software.kes.kraftwerk.constraints.IntRange;

import static software.kes.kraftwerk.Generators.generateArrayListOfSize;
import static software.kes.kraftwerk.Generators.generateInt;

public class ArrayListOfSizeExample {
    public static void main(String[] args) {
        generateArrayListOfSize(5, generateInt(IntRange.from(1).to(10)))
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        //[9, 9, 2, 7, 5]
        //[6, 5, 7, 5, 1]
        //[8, 7, 4, 7, 7]
        //[7, 8, 7, 4, 3]
        //[3, 3, 10, 5, 3]
    }
}
