package examples;

import dev.marksman.kraftwerk.Generators;

import java.time.Year;

import static dev.marksman.kraftwerk.Generators.*;

public class CoProductExample {
    public static void main(String[] args) {
        Generators.tupled(generateMaybe(generateLocalDateForYear(Year.now())),
                generateEither(generateInt(), generateDouble()),
                generateThese(generateLong(), chooseOneOfValues("foo", "bar", "baz")))
                .run()
                .take(50)
                .forEach(System.out::println);
    }
}
