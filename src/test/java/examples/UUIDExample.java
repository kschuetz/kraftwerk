package examples;

import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static dev.marksman.kraftwerk.Generator.generateUUID;

public class UUIDExample {

    public static void main(String[] args) {
        streamFrom(generateUUID()).next(50).forEach(System.out::println);
    }

}
