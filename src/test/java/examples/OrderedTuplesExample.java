package examples;

import software.kes.kraftwerk.constraints.IntRange;

import static software.kes.kraftwerk.Generators.generateIdentifier;
import static software.kes.kraftwerk.Generators.generateInt;
import static software.kes.kraftwerk.Generators.generateOrderedPair;
import static software.kes.kraftwerk.Generators.generateTuple;

public class OrderedTuplesExample {
    public static void main(String[] args) {
        generateTuple(generateOrderedPair(generateIdentifier(10)),
                generateOrderedPair(generateInt(IntRange.from(-100).to(100))))
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
