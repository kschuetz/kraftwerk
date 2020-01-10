package examples;

import static dev.marksman.kraftwerk.Generators.generateUUID;
import static dev.marksman.kraftwerk.ValueSupplyIterator.streamFrom;

public class UUIDExample {

    public static void main(String[] args) {
        streamFrom(generateUUID()).next(50).forEach(System.out::println);
    }

}
