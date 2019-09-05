package examples;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Generator.generateUUID;

public class UUIDExample {

    public static void main(String[] args) {
        streamFrom(generateUUID()).next(50).forEach(System.out::println);
    }

}
