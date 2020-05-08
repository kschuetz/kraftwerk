package examples.components;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.Year;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static examples.components.Address.generateAddress;
import static examples.components.Name.generateName;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Person {
    private final Name name;
    private final Address address;
    private final LocalDate dateOfBirth;

    public String pretty() {
        return "Person {\n" +
                "  Name: " + name.pretty() + "\n" +
                "  Address: " + address.prettySingleLine() + "\n" +
                "  Date of birth: " + dateOfBirth + "\n" +
                "}\n";
    }

    public static Person person(Name name, Address address, LocalDate dateOfBirth) {
        return new Person(name, address, dateOfBirth);
    }

    private static class generators {

        private static int currentYear = LocalDate.now().getYear();

        private static Generator<Integer> age =
                frequencyMap(weighted(1, Generators.generateInt(IntRange.from(2).to(9))))
                        .add(weighted(2, Generators.generateInt(IntRange.from(10).to(19))))
                        .add(weighted(3, Generators.generateInt(IntRange.from(20).to(29))))
                        .add(weighted(3, Generators.generateInt(IntRange.from(30).to(39))))
                        .add(weighted(3, Generators.generateInt(IntRange.from(40).to(49))))
                        .add(weighted(3, Generators.generateInt(IntRange.from(50).to(59))))
                        .add(weighted(2, Generators.generateInt(IntRange.from(60).to(69))))
                        .add(weighted(2, Generators.generateInt(IntRange.from(70).to(79))))
                        .add(weighted(2, Generators.generateInt(IntRange.from(80).to(99))))
                        .toGenerator();

        private static Generator<LocalDate> dateOfBirth =
                age.flatMap(n -> Generators.generateLocalDateForYear(Year.of(currentYear - n)));

        static Generator<Person> person = Generators.tupled(generateName(),
                generateAddress(),
                dateOfBirth)
                .fmap(into3(Person::person));
    }

    public static Generator<Person> generatePerson() {
        return generators.person;
    }

    public static void main(String[] args) {
        generatePerson()
                .fmap(Person::pretty)
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
