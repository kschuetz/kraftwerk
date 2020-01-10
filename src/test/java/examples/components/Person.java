package examples.components;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.Year;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
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
                frequencyMap(1, Generators.generateInt(2, 9))
                        .add(2, Generators.generateInt(10, 19))
                        .add(3, Generators.generateInt(20, 29))
                        .add(3, Generators.generateInt(30, 39))
                        .add(3, Generators.generateInt(40, 49))
                        .add(3, Generators.generateInt(50, 59))
                        .add(2, Generators.generateInt(60, 69))
                        .add(2, Generators.generateInt(70, 79))
                        .add(2, Generators.generateInt(80, 99))
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
