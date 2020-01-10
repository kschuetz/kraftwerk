package examples;

import static dev.marksman.kraftwerk.Generators.generateUUID;

public class UUIDExample {

    public static void main(String[] args) {
        generateUUID()
                .run()
                .take(50)
                .forEach(System.out::println);
    }

}
