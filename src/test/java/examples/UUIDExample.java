package examples;

import static dev.marksman.composablerandom.Generate.generateUUID;
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;

public class UUIDExample {

    public static void main(String[] args) {
        streamFrom(generateUUID()).next(50).forEach(System.out::println);
    }

}
