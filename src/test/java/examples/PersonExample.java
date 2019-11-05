package examples;

import examples.components.Person;

import static dev.marksman.kraftwerk.GeneratedStream.streamFrom;
import static examples.components.Person.generatePerson;

public class PersonExample {

    public static void main(String[] args) {
        streamFrom(generatePerson().fmap(Person::pretty)).next(100).forEach(System.out::println);
    }
}
