package examples;

import examples.components.Person;

import static examples.components.Person.generatePerson;

public class PersonExample {

    public static void main(String[] args) {
        generatePerson()
                .fmap(Person::pretty)
                .run()
                .take(100)
                .forEach(System.out::println);
    }
}
