package examples.tutorial;

import dev.marksman.kraftwerk.constraints.IntRange;

import java.time.LocalDate;

import static dev.marksman.kraftwerk.Generators.generateInt;

public class MappingToADifferentType {
    public static void main(String[] args) {
        generateInt(IntRange.from(0).to(100))
                .fmap(n -> LocalDate.of(2020, 1, 1).plusDays(n))
                .run()
                .take(5)
                .forEach(System.out::println);

        // sample output:
        // 2020-02-27
        // 2020-03-08
        // 2020-01-19
        // 2020-04-09
        // 2020-01-03
    }
}
