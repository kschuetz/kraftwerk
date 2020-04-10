package examples;

import static dev.marksman.kraftwerk.Generators.*;

public class OrderedTuplesExample {

    public static void main(String[] args) {
        tupled(generateOrderedPair(generateIdentifier(10)),
                generateOrderedPair(generateInt(-100, 100)))
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
