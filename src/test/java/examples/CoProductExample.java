package examples;

import dev.marksman.kraftwerk.Generators;

import java.time.Year;

import static dev.marksman.kraftwerk.Generators.chooseOneOfValues;
import static dev.marksman.kraftwerk.Generators.generateEither;
import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForYear;
import static dev.marksman.kraftwerk.Generators.generateLong;
import static dev.marksman.kraftwerk.Generators.generateMaybe;
import static dev.marksman.kraftwerk.Generators.generateThese;

public class CoProductExample {
    public static void main(String[] args) {
        Generators.tupled(generateMaybe(generateLocalDateForYear(Year.now())),
                generateEither(generateInt(), Generators.generateDoubleFractional()),
                generateThese(generateLong(), chooseOneOfValues("foo", "bar", "baz")))
                .run()
                .take(50)
                .forEach(System.out::println);
    }
}
