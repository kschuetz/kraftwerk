package examples;

import static software.kes.kraftwerk.Generators.generateIdentifier;
import static software.kes.kraftwerk.Generators.generateInfiniteIterable;

public class InfiniteIterableExample {
    public static void main(String[] args) {
        generateInfiniteIterable(generateIdentifier(5))
                .run()
                .take(50)
                .forEach(items -> {
                    // items is infinite
                    System.out.println(String.join(", ", items.take(16)) + " ...");
                });
    }
}
