package examples;

import static software.kes.kraftwerk.Generators.generateUUID;

public class UUIDExample {

    public static void main(String[] args) {
        generateUUID()
                .run()
                .take(50)
                .forEach(System.out::println);
    }

}
