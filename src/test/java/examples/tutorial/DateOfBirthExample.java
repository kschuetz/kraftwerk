package examples.tutorial;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.time.LocalDate;
import java.time.Year;

import static dev.marksman.kraftwerk.Generators.generateInt;
import static dev.marksman.kraftwerk.Generators.generateLocalDateForYear;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;

public class DateOfBirthExample {
    private static final int currentYear = LocalDate.now().getYear();

    private static final Generator<Integer> generateAge =
            frequencyMap(weighted(1, generateInt(IntRange.from(2).to(9))))
                    .add(weighted(2, generateInt(IntRange.from(10).to(19))))
                    .add(weighted(3, generateInt(IntRange.from(20).to(29))))
                    .add(weighted(3, generateInt(IntRange.from(30).to(39))))
                    .add(weighted(3, generateInt(IntRange.from(40).to(49))))
                    .add(weighted(3, generateInt(IntRange.from(50).to(59))))
                    .add(weighted(2, generateInt(IntRange.from(60).to(69))))
                    .add(weighted(2, generateInt(IntRange.from(70).to(79))))
                    .add(weighted(2, generateInt(IntRange.from(80).to(99))))
                    .toGenerator();

    private static final Generator<LocalDate> generateDateOfBirth =
            generateAge.flatMap(age -> generateLocalDateForYear(Year.of(currentYear - age)));

    public static void main(String[] args) {
        generateDateOfBirth
                .run()
                .take(10)
                .forEach(System.out::println);

        // sample output:
        //1988-10-18
        //2002-05-03
        //1967-01-04
        //1985-12-24
        //1995-04-18
        //1962-11-25
        //1999-04-22
        //1976-02-22
        //1947-08-25
        //1946-03-12
    }
}
