package dev.marksman.composablerandom.examples.generators;

import dev.marksman.composablerandom.Generator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.Generator.generateInt;
import static dev.marksman.composablerandom.builtin.Generators.generateLocalDateForYear;
import static dev.marksman.composablerandom.builtin.Generators.tupled;
import static dev.marksman.composablerandom.examples.generators.Address.generateAddress;
import static dev.marksman.composablerandom.examples.generators.Name.generateName;
import static dev.marksman.composablerandom.frequency.FrequencyMap.frequencyMap;

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
                frequencyMap(1, generateInt(2, 9))
                        .add(2, generateInt(10, 19))
                        .add(3, generateInt(20, 29))
                        .add(3, generateInt(30, 39))
                        .add(3, generateInt(40, 49))
                        .add(3, generateInt(50, 59))
                        .add(2, generateInt(60, 69))
                        .add(2, generateInt(70, 79))
                        .add(2, generateInt(80, 99))
                        .toGenerator();

        private static Generator<LocalDate> dateOfBirth =
                age.flatMap(n -> generateLocalDateForYear(currentYear - n));

        static Generator<Person> person = tupled(generateName(),
                generateAddress(),
                dateOfBirth)
                .fmap(into3(Person::person));
    }

    public static Generator<Person> generatePerson() {
        return generators.person;
    }

    public static void main(String[] args) {
        streamFrom(generatePerson().fmap(Person::pretty)).next(100).forEach(System.out::println);
    }
}
