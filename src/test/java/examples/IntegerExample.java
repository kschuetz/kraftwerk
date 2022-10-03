package examples;

import software.kes.kraftwerk.Generators;

public class IntegerExample {
    public static void main(String[] args) {
        Generators.generateInt()
                .run()
                .take(50)
                .forEach(System.out::println);
    }
}
