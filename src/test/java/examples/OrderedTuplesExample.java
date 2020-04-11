package examples;

import dev.marksman.kraftwerk.constraints.IntRange;

import static dev.marksman.kraftwerk.Generators.*;

public class OrderedTuplesExample {

    public static void main(String[] args) {
        tupled(generateOrderedPair(generateIdentifier(10)),
                generateOrderedPair(generateInt(IntRange.from(-100).to(100))))
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
