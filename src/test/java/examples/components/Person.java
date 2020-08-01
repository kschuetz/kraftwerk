package examples.components;

import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;
import dev.marksman.kraftwerk.constraints.IntRange;

import java.time.LocalDate;
import java.time.Year;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static dev.marksman.kraftwerk.Weighted.weighted;
import static dev.marksman.kraftwerk.frequency.FrequencyMap.frequencyMap;
import static examples.components.Address.generateAddress;
import static examples.components.Name.generateName;

public final class Person {
    private final Name name;
    private final Address address;
    private final LocalDate dateOfBirth;

    private Person(Name name, Address address, LocalDate dateOfBirth) {
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public static Person person(Name name, Address address, LocalDate dateOfBirth) {
        return new Person(name, address, dateOfBirth);
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

    public String pretty() {
        return "Person {\n" +
                "  Name: " + name.pretty() + "\n" +
                "  Address: " + address.prettySingleLine() + "\n" +
                "  Date of birth: " + dateOfBirth + "\n" +
                "}\n";
    }

    public Name getName() {
        return this.name;
    }

    public Address getAddress() {
        return this.address;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!name.equals(person.name)) return false;
        if (!address.equals(person.address)) return false;
        return dateOfBirth.equals(person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name=" + name +
                ", address=" + address +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    private static class generators {

        private static final int currentYear = LocalDate.now().getYear();

        private static final Generator<Integer> age =
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

        private static final Generator<LocalDate> dateOfBirth =
                age.flatMap(n -> Generators.generateLocalDateForYear(Year.of(currentYear - n)));

        static Generator<Person> person = Generators.tupled(generateName(),
                generateAddress(),
                dateOfBirth)
                .fmap(into3(Person::person));
    }
}
