package examples;

import dev.marksman.kraftwerk.Generators;

import static dev.marksman.kraftwerk.Generators.generateFromEnum;

public class EnumExample {

    enum Color {RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET}

    enum Animal {DOG, CAT, HORSE, COW, PIG, MOUSE}

    public static void main(String[] args) {
        Generators.tupled(generateFromEnum(Color.class),
                generateFromEnum(Animal.class))
                .run()
                .take(50)
                .forEach(System.out::println);
    }

}
