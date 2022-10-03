package examples;

import static software.kes.kraftwerk.Generators.generateFromEnum;
import static software.kes.kraftwerk.Generators.generateTuple;

public class EnumExample {
    enum Color {RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET}

    enum Animal {DOG, CAT, HORSE, COW, PIG, MOUSE}

    public static void main(String[] args) {
        generateTuple(generateFromEnum(Color.class),
                generateFromEnum(Animal.class))
                .run()
                .take(50)
                .forEach(System.out::println);
    }
}

